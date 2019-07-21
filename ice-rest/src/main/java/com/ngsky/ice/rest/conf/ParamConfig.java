package com.ngsky.ice.rest.conf;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: 动态配置项
 * @Author: sunyx@lenovocloud.com
 * @CreateDate: 2019/6/20 17:40
 */
@ConfigurationProperties("config")
@Data
@ToString
@Component
public class ParamConfig {
    private String ip;
    private String url;
    private String moduleName;
    private String moduleDesc;
}
