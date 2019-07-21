package com.ngsky.ice.data.exception;

import com.ngsky.ice.data.pojo.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <dl>
 * <dt>GlobalExceptionHandler</dt>
 * <dd>Description: 全局异常拦截(拦截所有控制器@RequestMapping)</dd>
 * <dd>CreateDate: 4/10/2019 11:31 PM</dd>
 * </dl>
 *
 * @author ngsky
 */
@RestControllerAdvice
@Order(-1)
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Message conflictExceptionHandler(ConflictException e) {
        log.info("--------------- EXCEPTION ----------------");
        log.error("--- ConflictException: {}", e);
        return new Message().error(1111, "资源发送冲突");
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Message forbiddenExceptionHandler(ForbiddenException e) {
        log.info("--------------- EXCEPTION ----------------");
        log.error("--- ForbiddenException: {}", e);
        return new Message().error(1111, "禁止操作");
    }

    @ExceptionHandler(NotExistedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Message notExistedExceptionHandler(NotExistedException e) {
        log.info("--------------- EXCEPTION ----------------");
        log.error("--- NotExistedException: {}", e);
        return new Message().error(1111, "资源不存在");
    }

    @ExceptionHandler(ParamErrorException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_REQUIRED)
    public Message paramErrorExceptionHandler(ParamErrorException e) {
        log.info("--------------- EXCEPTION ----------------");
        log.error("--- ParamErrorException: {}", e);
        return new Message().error(1111, "参数错误");
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Message conflictExceptionHandler(UnAuthorizedException e) {
        log.info("--------------- EXCEPTION ----------------");
        log.error("--- UnauthenticatedException: {}", e);
        return new Message().error(1111, "未授权");
    }

    @ExceptionHandler(NoStackTraceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Message conflictExceptionHandler(NoStackTraceException e) {
        log.info("--------------- EXCEPTION ----------------");
        log.error("--- NoStackTraceException: {}", e);
        return new Message().error(1111, "运行时异常");
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Message conflictExceptionHandler(Throwable e) {
        log.info("--------------- EXCEPTION ----------------");
        log.error("--- Throwable: {}", e);
        return new Message().error(1111, "未知错误");
    }
}
