package com.netty.socket.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 初始化管道处理器
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/6 0:14
 **/
public class WebSocketChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
            //HTTP请求的解码和编码
            .addLast(new HttpServerCodec())
            //分块写处理，否则文件过大会将内存撑爆
            .addLast(new ChunkedWriteHandler())
            //将一个Http的消息组装成一个完成的HttpRequest或者HttpResponse,必须放在HttpServerCodec后的后面
            .addLast(new HttpObjectAggregator(65536))
            //用于处理websocket, /ws为访问websocket时的uri
            .addLast(new WebSocketServerProtocolHandler("/ws"))
            //自定义的处理器
            .addLast(new TextWebSocketFrameHandler());
    }
}
