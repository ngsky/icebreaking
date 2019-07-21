package com.ngsky.ice.rest.exception;

/**
 * <dl>
 * <dt>IceException</dt>
 * <dd>Description: 异常基类</dd>
 * <dd>CreateDate: 4/5/2019 9:44 PM</dd>
 * </dl>
 *
 * @author ngsky
 */
public class IceException extends Exception {
    public IceException(){}

    public IceException(String message){
        super(message);
    }

    public IceException(String message, Throwable cause) {
        super(message, cause);
    }

    public IceException(Throwable cause){
        super(cause);
    }

    protected IceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
