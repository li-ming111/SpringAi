package com.zmx.component;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class TimeFunction implements Function<TimeFunction.Resquest,TimeFunction.Response> {
    @JsonClassDescription("请求参数,包含时区ID")
    public record Resquest(@JsonProperty(required = true,value="zoneId")
                           @JsonPropertyDescription("时区ID 例如：Asia/Shanghai") String zoneId) {
    }

    @JsonClassDescription("响应参数,包含当前时间")
    public record Response( @JsonPropertyDescription("当前时间") String time) {
    }

    @Override
    public Response apply(Resquest resquest) {
        ZoneId zid = ZoneId.of(resquest.zoneId());
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zid);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new Response(zonedDateTime.format(formatter));
    }
}



























