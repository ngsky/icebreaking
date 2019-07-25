package com.ngsky.ice.rest.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ngsky.ice.comm.bean.CellDownReq;
import com.ngsky.ice.comm.bean.CellDownResp;
import com.ngsky.ice.rest.client.CellClient;
import com.ngsky.ice.rest.exception.NotExistedException;
import com.ngsky.ice.rest.service.FileDownloadService;
import io.netty.channel.ChannelFuture;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;

/**
 * @Description:
 * @Author: sunyx@lenovocloud.com
 * @CreateDate: 2019/7/25 13:32
 */
@Slf4j
@Service
public class FileDownlaodServiceImpl implements FileDownloadService {

    @Value("${cell.server.host}")
    private String cellServerHost;

    @Value("${cell.server.port}")
    private int cellServerPort;

    @Value("${cell.metadata.testdir}")
    private String cellMetadataDir;

    @Override
    public void download(@NonNull String fileHash, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 读取元数据信息
        File file = new File(cellMetadataDir + fileHash);
        if (!file.exists() || file.isDirectory()) {
            log.info("file not found");
            throw new NotExistedException("file is not found!");
        }
        log.info("读取文件元数据...");
        String metadata = FileUtils.readFileToString(file, "UTF-8");
        JSONObject metaJson = JSONObject.parseObject(metadata);
        if (null == metaJson) return;
        // 向netty 请求数据块
//        log.info("文件元数据:fileHash:{}, metadata:{}", fileHash, metadata);

        // 封装文件数据
        int countChunk = metaJson.getInteger("countChunk");
        String chunks = metaJson.getString("chunks");
        String fileName = metaJson.getString("fileName");
        String fileSize = metaJson.getString("fileSize");
        String[] chunkStr = chunks.split(",");

        String userAgent = request.getHeader("User-Agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }

        // 返回数据流
        response.reset();
        response.setContentType("application/x-download");
//        response.addHeader("Content-Disposition","attachment;filename="+ new String(fileName.getBytes(),"UTF-8"));
        response.setHeader("Content-Disposition",
                String.format("attachment; filename=\"%s\"", fileName));

        response.addHeader("Content-Length", fileSize);
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");

        CellClient cellClient = new CellClient();
        ChannelFuture future;
        try {
            future = cellClient.connectCellServer(cellServerHost, cellServerPort);
            int offset = 0;
            for (int i = 0; i < chunkStr.length; i++) {
                CellDownReq.ReqDown reqDown = CellDownReq.ReqDown.newBuilder()
                        .setObjKey(chunkStr[i])
                        .setWhatChunk(i + 1)
                        .setCountChunk(chunkStr.length)
                        .setFileHash(fileHash)
                        .setMethod(CellDownReq.ReqDown.RequestType.GET)
                        .build();
                CellDownResp.RespDown respDown = cellClient.sendDownMsg(future, reqDown);
                if(null != respDown){
                    log.info("正在下载第:{}块数据块:whatCount:{}, countChunk:{}, objKey:{}", (i + 1), reqDown.getWhatChunk(), reqDown.getCountChunk(), reqDown.getObjKey());
                    byte[] data = respDown.getObjData().toByteArray();
                    out.write(data, 0, data.length);
                    log.info("长度:{},总长度:{}", data.length, offset);
                } else {
                    log.info("正在下载第:{}块数据块:null", (i + 1));
                }
            }
            out .flush();
            out .close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
