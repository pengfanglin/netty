package com.netty.socket.core;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 请求事件包装类
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/6 23:37
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RequestWrapper {
    private Request request;
    private ChannelHandlerContext ctx;
}
