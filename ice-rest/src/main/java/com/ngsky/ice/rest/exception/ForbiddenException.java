package com.ngsky.ice.rest.exception;

/**
 * <dl>
 * <dt>ForbiddenException</dt>
 * <dd>Description: 禁止操作异常</dd>
 * <dd>CreateDate: 4/5/2019 9:49 PM</dd>
 * </dl>
 *
 * @author ngsky
 */
public class ForbiddenException extends NoStackTraceException {
    public ForbiddenException(){}

    public ForbiddenException(String errorMsg){
        super(errorMsg);
    }

    public ForbiddenException(String errorMsg, Object... params){
        super(errorMsg, params);
    }
}
