package com.ngsky.ice.data.exception;

/**
 * <dl>
 * <dt>ParamErrorException</dt>
 * <dd>Description: 参数错误异常</dd>
 * <dd>CreateDate: 4/5/2019 9:50 PM</dd>
 * </dl>
 *
 * @author ngsky
 */
public class ParamErrorException extends NoStackTraceException {
    public ParamErrorException(){}

    public ParamErrorException(String errorMsg){
        super(errorMsg);
    }

    public ParamErrorException(String errorMsg, Object... params) {
        super(errorMsg, params);
    }
}
