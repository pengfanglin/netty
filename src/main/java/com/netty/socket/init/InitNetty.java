package com.netty.socket.init;

import com.netty.properties.NettyProperties;
import com.netty.socket.handler.WebSocketChannelHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * netty初始化
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/5 23:46
 **/
@Component
public class InitNetty implements DisposableBean {

    EventLoopGroup boosGroup;
    EventLoopGroup workerGroup;

    public InitNetty(NettyProperties nettyProperties, WebSocketChannelHandlerInitializer webSocketChannelHandlerInitializer) {
        boosGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
            .group(boosGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(webSocketChannelHandlerInitializer);
        ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(nettyProperties.getPort()));
        channelFuture.channel().closeFuture();
    }

    @Override
    public void destroy() {
        if (boosGroup != null) {
            boosGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }
}
