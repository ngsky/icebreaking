package com.ngsky.ice.comm.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * <dl>
 * <dt>HttpResult</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 4/29/2019 11:20 PM</dd>
 * </dl>
 *
 * @author daxiong
 */
@Setter
@Getter
public class HttpResult {
    private int httpCode;
    private String result;
    private Map<String, String> headers;
}
