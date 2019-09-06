package com.netty.socket.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 初始化管道处理器
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/6 0:14
 **/
@Component
public class WebSocketChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    TextWebSocketFrameHandler textWebSocketFrameHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //HttpServerCodec: 针对http协议进行编解码
        pipeline.addLast(new HttpServerCodec());
        //ChunkedWriteHandler分块写处理，文件过大会将内存撑爆
        pipeline.addLast(new ChunkedWriteHandler());
        //将一个Http的消息组装成一个完成的HttpRequest或者HttpResponse,必须放在HttpServerCodec后的后面
        pipeline.addLast(new HttpObjectAggregator(8192));
        //用于处理websocket, /ws为访问websocket时的uri
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        //自定义的处理器
        pipeline.addLast(textWebSocketFrameHandler);
    }
}
