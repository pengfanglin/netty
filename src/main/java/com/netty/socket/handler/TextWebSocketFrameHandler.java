package com.netty.socket.handler;

import com.fanglin.common.utils.JsonUtils;
import com.fanglin.common.utils.OthersUtils;
import com.netty.service.SocketService;
import com.netty.socket.core.RequestData;
import com.netty.socket.core.Socket;
import com.netty.socket.core.SocketCenter;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * webSocket事件处理
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/6 0:18
 **/
@Slf4j
@Component
@ChannelHandler.Sharable
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    SocketCenter socketCenter;
    @Autowired
    SocketService socketService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        if (OthersUtils.isEmpty(msg.text())) {
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(Socket.error("请求体不能为空"))));
            return;
        }
        RequestData request = JsonUtils.jsonToObject(msg.text(), RequestData.class);
        if (request.getType() == null) {
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(Socket.error("请求类型不能为空"))));
            return;
        }
        if (request.getData() == null) {
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(Socket.error("请求数据不能为空"))));
            return;
        }
        request.setCtx(ctx);
        Socket response = socketCenter.request(request);
        if (response != null) {
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(response)));
        }
    }

    /**
     * 连接建立时
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        log.info("连接{}建立成功", ctx.channel().id().asLongText());
        socketCenter.getOnlineNumber().incrementAndGet();
    }

    /**
     * 连接关闭时
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        log.info("连接{}断开", ctx.channel().id().asLongText());
        socketCenter.getOnlineNumber().decrementAndGet();
    }

    /**
     * 异常发生时
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        String error;
        if (cause.getCause() != null) {
            error = cause.getCause().getMessage();
        } else {
            error = cause.getMessage();
        }
        log.warn("连接{}异常:{}", ctx.channel().id().asLongText(), error);
        ctx.close();
        socketCenter.getOnlineNumber().decrementAndGet();
    }
}
