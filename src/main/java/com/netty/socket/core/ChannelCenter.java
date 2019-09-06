package com.netty.socket.core;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理所有的channel
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/7 1:33
 **/
public class ChannelCenter {

    private ChannelCenter() {
    }

    public static Map<Integer, ChannelHandlerContext> channels = new ConcurrentHashMap<>();
}
