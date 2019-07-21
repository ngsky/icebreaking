package com.ngsky.ice.rest.exception;

/**
 * <dl>
 * <dt>ConflictException</dt>
 * <dd>Description: 冲突异常</dd>
 * <dd>CreateDate: 4/5/2019 9:49 PM</dd>
 * </dl>
 *
 * @author ngsky
 */
public class ConflictException extends NoStackTraceException {
    public ConflictException(){}

    public ConflictException(String errorMsg){
        super(errorMsg);
    }
}
