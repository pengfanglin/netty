package com.netty.enums.socket;

import lombok.AllArgsConstructor;

/**
 * 客户端事件类型
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/6 0:35
 **/
@AllArgsConstructor
public enum RequestEventType {

    /**
     * 心跳检测
     */
    HEARTBEAT,
    /**
     * 在线用户列表
     */
    ID_LIST(),
    /**
     * 在线数
     */
    ONLINE_NUMBER,
    /**
     * 绑定用户
     */
    BIND;
}
