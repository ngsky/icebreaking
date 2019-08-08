package com.ngsky.ice.rest.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.ngsky.ice.comm.bean.CellReqBody;
import com.ngsky.ice.comm.utils.Base64Util;
import com.ngsky.ice.comm.utils.SHA256Util;
import com.ngsky.ice.rest.client.CellClient;
import com.ngsky.ice.rest.conf.Constant;
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

    @Value("${cell.meta.testdir}")
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
                        .setContentLen(len)
                        .setObjData(ByteString.copyFrom(buff, 0, len))
                        .build();

                log.info("rest 正在发送 第{}块对象，共{}块对象", whatChunk, countChunk);
                cellClient.sendMessage(future, cellReqBody);
                chunks.append(Base64Util.encode(objKey)).append(",");
                whatChunk++;
            }
            future.channel().closeFuture().sync();

            // 存储元数据信息
            JSONObject fileInfo = new JSONObject();
            String fileHash = SHA256Util.SHA256(file.getBytes());
            chunks.deleteCharAt(chunks.length() - 1);

            fileInfo.put("fileHash", fileHash);
            fileInfo.put("countChunk", countChunk);
            fileInfo.put("fileName", fileName);
            fileInfo.put("chunks", chunks.toString());
            fileInfo.put("fileSize", String.valueOf(fileSize));

            File metadataFile = new File(cellMetadataDir + fileHash);
            FileUtils.writeStringToFile(metadataFile, fileInfo.toJSONString(), "UTF-8", false);
            log.info("rest cell 元数据存储成功：fileInfo:{}", fileInfo.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 分片

    // 存储元数据

    // 存储数据对象
}
