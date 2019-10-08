package com.netty.socket.core;

import com.fanglin.common.util.JsonUtils;
import com.netty.enums.socket.EventType;
import com.netty.socket.disruptor.MessageConsumer;
import com.netty.socket.handler.TextWebSocketFrameHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/6 23:10
 **/
@Slf4j
public class SocketCenter extends MessageConsumer {

    public SocketCenter(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(RequestWrapper event) throws Exception {
        Request request = event.getRequest();
        EventType type = request.getType();
        Response response;
        switch (type) {
            case HEARTBEAT:
                response = Response.ok(type);
                break;
            case ID_LIST:
                Set<String> idList = new HashSet<>(ChannelCenter.channels.size());
                ChannelCenter.channels.forEach((userId, ctx1) -> idList.add(ctx1.channel().id().asLongText()));
                response = Response.ok(type, idList);
                break;
            case ONLINE_NUMBER:
                response = Response.ok(type, TextWebSocketFrameHandler.getChannelGroup().size());
                break;
            case BIND:
                Integer userId;
                try {
                    userId = Integer.parseInt(request.getData().toString());
                } catch (NumberFormatException e) {
                    response = Response.error(type, "参数非法");
                    break;
                }
                if (ChannelCenter.channels.containsKey(userId)) {
                    ChannelHandlerContext ctx1 = ChannelCenter.channels.get(userId);
                    ctx1.channel().writeAndFlush(Response.error(EventType.REPEAT_BIND, "重复登录"));
                    ctx1.close();
                }
                ChannelCenter.channels.put(userId, event.getCtx());
                response = Response.ok(type);
                break;
            default:
                response = Response.error(type, "请求类型未知");
        }
        event.getCtx().channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.toJson(response)));
    }
}
