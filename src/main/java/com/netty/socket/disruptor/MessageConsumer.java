package com.netty.socket.disruptor;

import com.lmax.disruptor.WorkHandler;
import com.netty.socket.core.RequestWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 消息消费者抽象类
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/8 23:54
 **/
@Setter
@Getter
@AllArgsConstructor
public abstract class MessageConsumer implements WorkHandler<RequestWrapper> {
    protected String consumerId;
}
