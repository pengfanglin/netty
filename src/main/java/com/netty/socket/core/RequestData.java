package com.netty.socket.core;

import com.netty.enums.socket.RequestEventType;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/6 23:37
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RequestData {
    /**
     * 事件类型
     */
    private RequestEventType type;
    /**
     * channel
     */
    private ChannelHandlerContext ctx;
    /**
     * 请求数据
     */
    private Object data;
}
