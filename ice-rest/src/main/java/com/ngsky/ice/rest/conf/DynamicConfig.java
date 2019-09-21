package com.ngsky.ice.rest.conf;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @Description: 动态配置项
 * @Author: ngsky
 * @CreateDate: 2019/6/20 17:25
 */
@EnableConfigurationProperties(ParamConfig.class)
public class DynamicConfig {
    @Bean
//    @ConfigurationProperties(prefix = "config")
    public ParamConfig config(){
        return new ParamConfig();
    }


}
