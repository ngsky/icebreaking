package com.ngsky.ice.data.pojo;

import lombok.Getter;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * <dl>
 * <dt>Message</dt>
 * <dd>Description:
 * 前后端统一消息定义协议: Message
 * {
 * meta: {"code": code, "success": true/false, "msg": message, "timestamp": timestamp},
 * data: {...}
 * }
 *
 *
 * </dd>
 * <dd>CreateDate: 4/10/2019 11:36 PM</dd>
 * </dl>
 *
 * @author ngsky
 */
@Getter
public class Message {

    // 无cookie
    public final static int AUTH_NU_COOKIE = 4001;
    // 未授权
    public final static int AUTH_NU_FAILED = 4002;
    // 注册成功
    public final static int USER_REGISTER_SUCCEED = 2000;

    // 响应处理成功
    public final static int DEAL_SUCCEED = 2000;

    // 响应处理失败
    public final static int DEAL_FAILED = 5000;

    /**
     * 消息头 meta 存放状态信息 meta: {"code": code, "msg": message}
     */
    private Map<String, Object> meta = new HashMap<>();

    /**
     * 消息体 data 存放实体交互数据
     */
    private Object data = null;

    public Message setMeta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public Message setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }

     public Message setData(Object data) {
        this.data = data;
        return this;
    }

    public Message addMeta(String key, Object value) {
        this.meta.put(key, value);
        return this;
    }

    public Message addData(String key, Object value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        this.data = map;
        return this;
    }

    public Message addData(Object value) {
        this.data = value;
        return this;
    }

    public Message ok() {
        this.ok(DEAL_SUCCEED, "SUCCESS");
        return this;
    }

    public Message ok(int statusCode, String statusMsg) {
        this.addMeta("code", statusCode);
        this.addMeta("msg", statusMsg);
        this.addMeta("success", Boolean.TRUE);
        this.addMeta("timestamp", new Timestamp(System.currentTimeMillis()));
        return this;
    }

    public Message error(int statusCode, String statusMsg) {
        this.addMeta("code", statusCode);
        this.addMeta("msg", statusMsg);
        this.addMeta("success", Boolean.FALSE);
        this.addMeta("timestamp", new Timestamp(System.currentTimeMillis()));
        return this;
    }
}
