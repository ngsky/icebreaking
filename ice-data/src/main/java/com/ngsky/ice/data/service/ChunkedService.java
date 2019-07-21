package com.ngsky.ice.data.service;

import com.ngsky.ice.comm.bean.CellReqBody;

/**
 * <dl>
 * <dt>ChunkedService</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 7/9/2019 2:31 PM</dd>
 * </dl>
 *
 * @author ngsky
 */
public interface ChunkedService {
    /**
     * 数据格式校验
     */
    void checkReqBody(CellReqBody.Request cellReqBody) throws Exception;

    /**
     * 数据块存储
     */
    void storage(CellReqBody.Request cellReqBody) throws Exception;

    /**
     * 读取数据块
     */
    byte[] extractive(String objKey) throws Exception;

}
