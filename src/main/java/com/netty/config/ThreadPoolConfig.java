package com.netty.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/9 0:18
 **/
@Configuration
public class ThreadPoolConfig {

    @Bean("disruptorThreadPool")
    public ThreadPoolTaskExecutor disruptorThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        /** 线程池维护线程的最小数量*/
        executor.setCorePoolSize(10);
        /** 线程池维护线程的最大数量*/
        executor.setMaxPoolSize(20);
        /** 允许的空闲时间s*/
        executor.setKeepAliveSeconds(200);
        /** 缓存队列*/
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("disruptor-");
        /** 线程对拒绝任务的处理策略；*/
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        /** 等待所有线程执行完毕，默认false*/
        executor.setWaitForTasksToCompleteOnShutdown(true);
        /** 等待时间*/
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }
}
