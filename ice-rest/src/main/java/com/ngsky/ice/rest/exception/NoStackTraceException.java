package com.ngsky.ice.rest.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * <dl>
 * <dt>NoStackTraceException</dt>
 * <dd>Description: 不追踪信息异常 </dd>
 * <dd>CreateDate: 4/5/2019 9:50 PM</dd>
 * </dl>
 *
 * @author ngsky
 */
@Getter
@Setter
public class NoStackTraceException extends IceException {
    private String errorMsg;
    private Object[] params;

    public NoStackTraceException(){
        this(null, null, false, true);
    }

    public NoStackTraceException(String errorMsg) {
        this(errorMsg, null, false, true);
        this.errorMsg = errorMsg;
    }

    public NoStackTraceException(String errorMsg, Object... params) {
        this(errorMsg, null, false, true);
        this.errorMsg = errorMsg;
        this.params = params;
    }

    public NoStackTraceException(Throwable cause) {
        this(null, cause, false, true);
    }

    protected NoStackTraceException(String errorMsg, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(errorMsg, cause, enableSuppression, writableStackTrace);
    }
}
