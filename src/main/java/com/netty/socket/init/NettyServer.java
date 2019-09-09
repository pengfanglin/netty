package com.netty.socket.init;

import com.netty.properties.NettyProperties;
import com.netty.socket.handler.WebSocketChannelHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class NettyServer {

    public NettyServer(NettyProperties nettyProperties) {
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap
                .group(boosGroup, workerGroup)
                .option(ChannelOption.SO_BACKLOG, nettyProperties.getBacklog())
                //缓冲区池化
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                //缓冲区大小自适应
                .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator())
                //防止数据传输延迟 如果false的话会缓冲数据达到一定量在flush,降低系统网络调用（具体场景）
                .childOption(ChannelOption.TCP_NODELAY, true)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WebSocketChannelHandlerInitializer());
            if (nettyProperties.getLogLevel() != null) {
                serverBootstrap.handler(new LoggingHandler(nettyProperties.getLogLevel()));
            }
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(nettyProperties.getPort())).sync();
            log.info("nettyServer启动成功.......");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            log.info("nettyServer关闭成功.......");
        }
    }
}
