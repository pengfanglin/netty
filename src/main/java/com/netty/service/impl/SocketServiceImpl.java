package com.netty.service.impl;

import com.fanglin.common.core.others.BusinessException;
import com.fanglin.common.utils.JsonUtils;
import com.netty.enums.socket.EventType;
import com.netty.service.SocketService;
import com.netty.socket.core.ChannelCenter;
import com.netty.socket.core.Response;
import com.netty.socket.core.SocketCenter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * socket服务类
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/6 18:25
 **/
@Component
public class SocketServiceImpl implements SocketService {

    @Override
    public void sendMessage(Integer userId, String message) {
        response(userId, Response.ok(EventType.NEW_MESSAGE, message));
    }

    public void response(Integer userId, Response response) {
        for (Map.Entry<Integer, ChannelHandlerContext> entry : ChannelCenter.channels.entrySet()) {
            if (entry.getKey().equals(userId)) {
                entry.getValue().channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(response)));
                return;
            }
        }
        throw new BusinessException("用户不在线");
    }

    public void response(String id, Response response) {
        for (Map.Entry<Integer, ChannelHandlerContext> entry : ChannelCenter.channels.entrySet()) {
            if (entry.getValue().channel().id().asLongText().equals(id)) {
                entry.getValue().channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(response)));
                return;
            }
        }
        throw new BusinessException("用户不在线");
    }
}
