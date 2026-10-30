package com.zmx.config;

import com.alibaba.cloud.ai.toolcalling.time.GetCurrentTimeByTimeZoneIdService;
import com.zmx.component.TimeFunction;
import com.zmx.component.TimeTools;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class ToolCallAutoConfig {

    @Bean
    @Description("按照时区ID获取当前时间的服务")
    public TimeFunction getTimeByZoneId(){
        return new TimeFunction();
    }

    /*@Bean
    public TimeTools timeTools(GetCurrentTimeByTimeZoneIdService timeService){
        return new TimeTools(timeService);
    }*/
}
