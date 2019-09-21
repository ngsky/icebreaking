package com.ngsky.ice.rest.shard.slicing;

import com.google.protobuf.ByteString;
import com.ngsky.ice.comm.bean.CellReqBody;
import com.ngsky.ice.comm.utils.Base64Util;
import com.ngsky.ice.comm.utils.SHA256Util;
import com.ngsky.ice.rest.conf.Constant;
import com.ngsky.ice.rest.exception.ConflictException;
import com.ngsky.ice.rest.utils.SizeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: ngsky
 * @CreateDate: 2019/8/8 13:07
 */
@Slf4j
public class NormalSlicing {

    /**
     * @param file 待分片文件对象
     */
    public List<CellReqBody.Request> shard(File file) throws ConflictException {
        try {
            long fileSize = file.length();
            if (0 == fileSize) {
                return null;
            }
            InputStream is = new FileInputStream(file);
            byte[] buff;
            int len = 0;
            int countChunk = 1;
            if (fileSize > Constant.CHUNK_SIZE) {
                countChunk = SizeUtil.coundChunked((int) fileSize, Constant.CHUNK_SIZE);
                log.info("上传文件大于 4M, 需要切块, 切块数量：{}", countChunk);
                buff = new byte[Constant.CHUNK_SIZE];
            } else {
                log.info("上传文件小于等于 4M, 不需要切块");
                buff = new byte[(int) fileSize];
            }
            List<CellReqBody.Request> cellList = new ArrayList<>();
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
                cellList.add(cellReqBody);
            }
            return cellList;
        } catch(Exception e){
            e.printStackTrace();
            throw new ConflictException("file shard failed!");
        }
    }
}
