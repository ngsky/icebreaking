package com.ngsky.ice.rest.exception;

/**
 * <dl>
 * <dt>NotExistedException</dt>
 * <dd>Description: 信息不存在异常</dd>
 * <dd>CreateDate: 4/5/2019 9:50 PM</dd>
 * </dl>
 *
 * @author ngsky
 */
public class NotExistedException extends NoStackTraceException {
    public NotExistedException(){}

    public NotExistedException(String errorMsg){
        super(errorMsg);
    }

}
