package com.ngsky.ice.data.service.impl;

import com.ngsky.ice.comm.bean.CellReqBody;
import com.ngsky.ice.comm.utils.Base64Util;
import com.ngsky.ice.comm.utils.SHA256Util;
import com.ngsky.ice.data.exception.ParamErrorException;
import com.ngsky.ice.data.service.ChunkedService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * <dl>
 * <dt>ChunkedServiceImpl</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 7/9/2019 2:34 PM</dd>
 * </dl>
 *
 * @author ngsky
 */
@Slf4j
@Service(value = "chunkedService")
public class ChunkedServiceImpl implements ChunkedService {

    @Value("${cell.points}")
    private String cellPoints;

    @Override
    public void checkReqBody(CellReqBody.Request cellReqBody) throws Exception {
        if (null == cellReqBody) throw new ParamErrorException("参数错误");
        // 对象 SHA-256 校验
        byte[] buff =  cellReqBody.getObjData().toByteArray();
        String digest = "SHA-256=" + Base64Util.encode(SHA256Util.SHA256(buff));
//        if (!StringUtils.equals(digest, cellReqBody.getDigest())) {
//            throw new ForbiddenException("数据格式错误，校验失败");
//        }
//        if (buff.length != cellReqBody.getContentLen()) {
//            throw new ForbiddenException("数据长度错误，校验失败");
//        }
    }

    @Override
    public void storage(CellReqBody.Request cellReqBody) throws Exception {
        if (null == cellReqBody) return;
        String objKey = cellReqBody.getObjKey();
        if(StringUtils.isEmpty(objKey)) return;
        File cellFile = new File(cellPoints);
        if (!cellFile.exists()) {
            cellFile.mkdir();
        }
        String objPath = cellPoints + objKey;
        log.info("storage object, path: {}", objPath);
        File file = new File(objPath);

        FileUtils.writeByteArrayToFile(file, cellReqBody.getObjData().toByteArray());
        log.info("写入文件成功-----");
    }

    @Override
    public byte[] extractive(@NonNull String objKey) throws Exception {
        File file = new File("cellPoints" + objKey);
        if (!file.exists() || file.isDirectory()) {
            return null;
        }
        return FileUtils.readFileToByteArray(file);
    }

}
