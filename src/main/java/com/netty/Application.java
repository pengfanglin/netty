package com.netty;

import com.fanglin.common.util.SpringUtils;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.netty.properties.NettyProperties;
import com.netty.socket.core.SocketCenter;
import com.netty.socket.disruptor.MessageConsumer;
import com.netty.socket.disruptor.RingBufferWorkerPoolFactory;
import com.netty.socket.init.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用程序启动类
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:28
 **/
@SpringBootApplication(scanBasePackages = {"com.netty", "com.fanglin"})
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        initDisruptor();
        new NettyServer(SpringUtils.getBean(NettyProperties.class));
    }

    private static void initDisruptor() {
        MessageConsumer[] consumers = new MessageConsumer[10];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new SocketCenter("consumerId:" + i);
        }
        RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI, 1024 * 1024, new BlockingWaitStrategy(), consumers);
    }
}
