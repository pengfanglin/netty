package com.netty.service.impl;

import com.netty.enums.socket.ResponseEventType;
import com.netty.service.SocketService;
import com.netty.socket.core.Socket;
import com.netty.socket.core.SocketCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * socket服务类
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/6 18:25
 **/
@Component
public class SocketServiceImpl implements SocketService {

    @Autowired
    SocketCenter socketCenter;

    @Override
    public void sendMessage(Integer userId, String message) {
        socketCenter.response(userId, Socket.ok(ResponseEventType.NEW_MESSAGE, message));
    }
}
