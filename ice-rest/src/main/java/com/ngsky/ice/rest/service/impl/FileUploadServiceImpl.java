package com.ngsky.ice.rest.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.ngsky.ice.comm.bean.CellDownReq;
import com.ngsky.ice.comm.bean.CellDownResp;
import com.ngsky.ice.comm.bean.CellReqBody;
import com.ngsky.ice.comm.utils.Base64Util;
import com.ngsky.ice.comm.utils.SHA256Util;
import com.ngsky.ice.rest.client.CellClient;
import com.ngsky.ice.rest.conf.Constant;
import com.ngsky.ice.rest.exception.NotExistedException;
import com.ngsky.ice.rest.exception.ParamErrorException;
import com.ngsky.ice.rest.service.FileUploadService;
import com.ngsky.ice.rest.utils.SizeUtil;
import io.netty.channel.ChannelFuture;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * <dl>
 * <dt>FileUploadServiceImpl</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 7/12/2019 12:37 AM</dd>
 * </dl>
 *
 * @author ngsky
 */
@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${cell.server.host}")
    private String cellServerHost;

    @Value("${cell.server.port}")
    private int cellServerPort;

    @Value("${cell.metadata.testdir}")
    private String cellMetadataDir;

    @Override
    public void upload(@NonNull MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new ParamErrorException("文件不存在,请重新上传");
        }
        try {
            String fileName = file.getOriginalFilename();
            String fileType = file.getContentType();
            long fileSize = file.getSize();
            if (0 == fileSize) return;
            log.info("fileName:{}, fileType:{}, fileSize:{}", fileName, fileType, fileSize);
            InputStream is = file.getInputStream();

            byte[] buff;
            int len = 0;

            int countChunk = 1;
            if (fileSize > Constant.CHUNK_SIZE) {
                countChunk = SizeUtil.coundChunked((int) fileSize, Constant.CHUNK_SIZE);
                log.info("上传文件大于 4M, 需要切块, 切块数量：{}", countChunk);
                buff = new byte[Constant.CHUNK_SIZE];
            } else {
                buff = new byte[(int) fileSize];
            }

            CellClient cellClient = new CellClient();
            ChannelFuture future = cellClient.connectCellServer(cellServerHost, cellServerPort);

            // fileHash:123 : 文件唯一标识
            // countChunk: 3 : 数据块数量
            // fileName:...: 文件名称
            // chunks:objKey1,objKey2,objKey3 : 数据块唯一标识
            JSONObject fileInfo = new JSONObject();
            String fileHash = SHA256Util.SHA256(file.getBytes());

            fileInfo.put("fileHash", fileHash);
            fileInfo.put("countChunk", countChunk);
            fileInfo.put("fileName", fileName);
            StringBuilder chunks = new StringBuilder();

            // 每个文件，需要确定共要发送 数据块的个数，当所有数据库发送成功并得到返回后，证明数据存储成功，可回收netty连接
            int whatChunk = 1;
            while ((len = is.read(buff)) != -1) {
                log.info("-- 切块 -> 4M: len:{}", len);
                String objKey = SHA256Util.SHA256(buff);
                String digest = "SHA-256=" + Base64Util.encode(objKey);
                CellReqBody.Request cellReqBody = CellReqBody.Request.newBuilder()
                        .setObjKey(Base64Util.encode(objKey))
                        .setMethod(CellReqBody.Request.RequestType.PUT)
                        .setDigest(Base64Util.encode(digest))
                        .setWhatChunk(whatChunk)
                        .setCountChunk(countChunk)
                        .setContentLen(buff.length)
                        .setObjData(ByteString.copyFrom(buff))
                        .build();
                log.info("rest 正在发送 第{}块对象，共{}块对象", whatChunk, countChunk);
                cellClient.sendMessage(future, cellReqBody);
                chunks.append(Base64Util.encode(objKey)).append(",");
                whatChunk++;
            }
            future.channel().closeFuture().sync();
            // 存储元数据信息
            chunks.substring(0, chunks.length() - 1);
            fileInfo.put("chunks", chunks.toString());
            File metadataFile = new File(cellMetadataDir + fileHash);
            FileUtils.writeStringToFile(metadataFile, fileInfo.toJSONString(), "UTF-8", false);
            log.info("rest cell 元数据存储成功：fileInfo:{}", fileInfo.toJSONString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void download(@NonNull String fileHash, HttpServletResponse response) throws Exception {
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
        String[] chunkStr = chunks.split(",");

        // 返回数据流
        response.reset();
        response.setContentType("application/x-download");
        response.addHeader("Content-Disposition","attachment;filename="+ new String(fileName.getBytes(),"UTF-8"));
//        response.addHeader("Content-Length", "" + file.length());
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");

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
//                    offset += data.length;
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
