package com.netty.socket.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.netty.socket.core.Request;
import com.netty.socket.core.RequestWrapper;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 消息提供者
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/8 23:54
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageProducer {
    private String producerId;
    private RingBuffer<RequestWrapper> ringBuffer;

    public void onData(ChannelHandlerContext ctx, Request request) {
        long sequence = ringBuffer.next();
        try {
            RequestWrapper wrapper = ringBuffer.get(sequence);
            wrapper.setCtx(ctx);
            wrapper.setRequest(request);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
