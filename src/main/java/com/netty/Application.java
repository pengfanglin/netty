package com.netty;

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
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
