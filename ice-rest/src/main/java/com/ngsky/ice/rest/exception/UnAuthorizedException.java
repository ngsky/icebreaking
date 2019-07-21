package com.ngsky.ice.rest.exception;

/**
 * <dl>
 * <dt>UnAuthorizedException</dt>
 * <dd>Description: 未授权异常</dd>
 * <dd>CreateDate: 4/5/2019 9:49 PM</dd>
 * </dl>
 *
 * @author ngsky
 */
public class UnAuthorizedException extends NoStackTraceException {
    public UnAuthorizedException(){}

    public UnAuthorizedException(String errorMsg){
        super(errorMsg);
    }
}
