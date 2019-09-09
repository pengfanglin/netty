package com.netty.socket.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import com.netty.config.ThreadPoolConfig;
import com.netty.socket.core.Request;
import com.netty.socket.core.RequestWrapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 环形缓冲区池
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/8 23:48
 **/
public class RingBufferWorkerPoolFactory {
    private static class SingletonHolder {
        static final RingBufferWorkerPoolFactory instance = new RingBufferWorkerPoolFactory();
    }

    private RingBufferWorkerPoolFactory() {

    }

    public static RingBufferWorkerPoolFactory getInstance() {
        return SingletonHolder.instance;
    }

    private static Map<String, MessageProducer> producers = new ConcurrentHashMap<>();
    private static Map<String, MessageConsumer> consumers = new ConcurrentHashMap<>();

    private RingBuffer<RequestWrapper> ringBuffer;
    private SequenceBarrier sequenceBarrier;
    private WorkerPool<RequestWrapper> workerPool;

    public void initAndStart(ProducerType type, int bufferSize, WaitStrategy waitStrategy, MessageConsumer[] messageConsumers) {
        //构建ringBuffer对象
        this.ringBuffer = RingBuffer.create(type,
            RequestWrapper::new,
            bufferSize, waitStrategy);
        //设置序号栅栏
        this.sequenceBarrier = this.ringBuffer.newBarrier();
        //设置工作池
        this.workerPool = new WorkerPool<>(this.ringBuffer, this.sequenceBarrier, new EventExceptionHandler(), messageConsumers);
        //构建的消费者放入池中
        for (MessageConsumer consumer : messageConsumers) {
            consumers.put(consumer.getConsumerId(), consumer);
        }
        //添加sequences
        this.ringBuffer.addGatingSequences(this.workerPool.getWorkerSequences());
        //启动工作池
        this.workerPool.start(new ThreadPoolConfig().disruptorThreadPool());
    }

    public MessageProducer getMessageProducer(String producerId) {
        MessageProducer producer = producers.get(producerId);
        if (producer == null) {
            producer = new MessageProducer(producerId, this.ringBuffer);
            producers.put(producerId, producer);
        }
        return producer;
    }

    static class EventExceptionHandler implements ExceptionHandler<RequestWrapper> {

        @Override
        public void handleEventException(Throwable ex, long sequence, RequestWrapper event) {

        }

        @Override
        public void handleOnStartException(Throwable ex) {

        }

        @Override
        public void handleOnShutdownException(Throwable ex) {

        }
    }
}
