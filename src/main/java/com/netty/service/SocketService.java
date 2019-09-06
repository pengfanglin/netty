package com.netty.service;

/**
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/6 18:22
 **/
public interface SocketService {
    /**
     * 数据刷新
     *
     * @param id
     * @param content
     */
    void sendMessage(Integer userId, String message);
}
