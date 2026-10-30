package com.zmx.component;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;

public class UserInfoTools {

    @Tool(name = "获取用户信息", description = "获取用户信息")
    public String getUserInfo(ToolContext context) {
        String userId = context.getContext().get("userId").toString();
        return userId;
    }
}
