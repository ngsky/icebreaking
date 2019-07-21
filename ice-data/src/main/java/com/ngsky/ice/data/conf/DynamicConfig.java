package com.ngsky.ice.data.conf;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @Description: 动态配置项
 * @Author: sunyx@lenovocloud.com
 * @CreateDate: 2019/6/20 17:25
 */
@EnableConfigurationProperties(ParamConfig.class)
public class DynamicConfig {
    @Bean
    public ParamConfig config(){
        return new ParamConfig();
    }


}
