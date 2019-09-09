package com.netty.properties;

import io.netty.handler.logging.LogLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * common包配置信息
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/2 14:08
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "netty")
@Component
public class NettyProperties {
    /**
     * 端口号
     */
    private Integer port = 9090;
    /**
     * 等待连接队列大小
     */
    private Integer backlog = 1024;
    /**
     * 日志级别
     */
    private LogLevel logLevel = null;
}
