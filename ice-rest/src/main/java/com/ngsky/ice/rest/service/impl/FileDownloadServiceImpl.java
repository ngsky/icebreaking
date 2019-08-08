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
public class FileDownloadServiceImpl implements FileDownloadService {

    @Value("${cell.server.host}")
    private String cellServerHost;

    @Value("${cell.server.port}")
    private int cellServerPort;

    @Value("${cell.meta.testdir}")
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

        // 封装文件数据
        int countChunk = metaJson.getInteger("countChunk");
        String chunks = metaJson.getString("chunks");
        String fileName = metaJson.getString("fileName");
        String fileSize = metaJson.getString("fileSize");
        String[] chunkStr = chunks.split(",");

        // 返回数据
        setHttpResponseHeader(request, response, fileName, fileSize);
        OutputStream out = new BufferedOutputStream(response.getOutputStream());

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

    /**
     * 文件下载响应头封装
     */
    private void setHttpResponseHeader(HttpServletRequest request, HttpServletResponse response, String fileName, String fileSize) {
        try {
            String userAgent = request.getHeader("User-Agent");
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }

            response.reset();
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));

            response.addHeader("Content-Length", fileSize);
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
