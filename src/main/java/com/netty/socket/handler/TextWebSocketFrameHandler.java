package com.netty.socket.handler;

import com.fanglin.common.util.JsonUtils;
import com.fanglin.common.util.OthersUtils;
import com.fanglin.common.util.UUIDUtils;
import com.netty.enums.socket.EventType;
import com.netty.socket.core.Request;
import com.netty.socket.core.Response;
import com.netty.socket.disruptor.MessageProducer;
import com.netty.socket.disruptor.RingBufferWorkerPoolFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * webSocket事件处理
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/6 0:18
 **/
@Slf4j
@ChannelHandler.Sharable
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Getter
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        if (OthersUtils.isEmpty(msg.text())) {
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.toJson(Response.error("请求体不能为空"))));
            return;
        }
        Request request = JsonUtils.toObject(msg.text(), Request.class);
        if (request.getType() == null) {
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.toJson(Response.error("请求类型不能为空"))));
            return;
        }
        if (request.getData() == null) {
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.toJson(Response.error("请求数据不能为空"))));
            return;
        }
        String producerId = "producerId:" + UUIDUtils.nextId();
        MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
        messageProducer.onData(ctx, request);
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
        channelGroup.add(ctx.channel());
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
        String error = cause.getMessage() == null ? "空指针异常" : cause.getMessage();
        log.warn("连接{}异常:{}", ctx.channel().id().asLongText(), error);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.toJson(Response.error(EventType.RESPONSE, error))));
    }
}
