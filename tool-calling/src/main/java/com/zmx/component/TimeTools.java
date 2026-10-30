package com.zmx.component;

import com.alibaba.cloud.ai.toolcalling.time.GetCurrentTimeByTimeZoneIdService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * 自定时间的组件
 */
public class TimeTools {

    private final GetCurrentTimeByTimeZoneIdService timeService;
    public TimeTools(GetCurrentTimeByTimeZoneIdService timeService) {
        this.timeService = timeService;
    }

    public TimeTools() {
        this.timeService = new GetCurrentTimeByTimeZoneIdService();
    }

    // 获取当前城市的时间
    @Tool(name = "获取当前城市的时间", description = "根据指定的时区ID获取当前城市的时间")
    public String getCityTime(@ToolParam( description = "时区ID，例如：Asia/Shanghai") String timeZoneId) {
        return timeService.apply(new GetCurrentTimeByTimeZoneIdService.Request(timeZoneId))
                .description();
    }
}























