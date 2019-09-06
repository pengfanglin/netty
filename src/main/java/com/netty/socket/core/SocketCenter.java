package com.netty.socket.core;

import com.fanglin.common.core.others.BusinessException;
import com.fanglin.common.utils.JsonUtils;
import com.netty.socket.handler.TextWebSocketFrameHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/6 23:10
 **/
@Component
@Slf4j
public class SocketCenter {

    @Getter
    private AtomicInteger onlineNumber = new AtomicInteger(0);


    @Autowired
    TextWebSocketFrameHandler handler;

    public Socket request(RequestData request) {
        Socket response;
        switch (request.getType()) {
            case HEARTBEAT:
                response = Socket.ok();
                break;
            case ID_LIST:
                Set<String> idList = new HashSet<>(ChannelCenter.channels.size());
                ChannelCenter.channels.forEach((userId, ctx) -> idList.add(ctx.channel().id().asLongText()));
                response = Socket.ok(idList);
                break;
            case ONLINE_NUMBER:
                response = Socket.ok(onlineNumber.intValue());
                break;
            case BIND:
                Integer userId;
                try {
                    userId = Integer.parseInt(request.getData().toString());
                } catch (NumberFormatException e) {
                    return Socket.error(onlineNumber.intValue());
                }
                ChannelCenter.channels.put(userId, request.getCtx());
                response = Socket.ok();
                break;
            default:
                response = Socket.error("请求类型未知");
        }
        return response;
    }

    public void response(Integer userId, Socket socket) {
        for (Map.Entry<Integer, ChannelHandlerContext> entry : ChannelCenter.channels.entrySet()) {
            if (entry.getKey().equals(userId)) {
                entry.getValue().channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(socket)));
                return;
            }
        }
        throw new BusinessException("用户不在线");
    }

    public void response(String id, Socket socket) {
        for (Map.Entry<Integer, ChannelHandlerContext> entry : ChannelCenter.channels.entrySet()) {
            if (entry.getValue().channel().id().asLongText().equals(id)) {
                entry.getValue().channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(socket)));
                return;
            }
        }
        throw new BusinessException("用户不在线");
    }
}
