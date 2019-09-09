package com.netty.socket.core;

import com.netty.enums.socket.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * socket返回结果
 *
 * @author 彭方林
 * @date 2018年4月2日
 */
@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    /**
     * 状态码  200:成功  400:失败
     */
    private int code;
    /**
     * 响应事件类型
     */
    private EventType type;
    /**
     * 结果集
     */
    private Object data;

    public static Response ok() {
        return new Response(200, EventType.RESPONSE, null);
    }

    public static Response ok(EventType type, Object data) {
        return new Response(200, type, data);
    }

    public static Response ok(EventType type) {
        return new Response(200, type, null);
    }

    public static Response ok(Object data) {
        return new Response(200, EventType.RESPONSE, data);
    }

    public static Response error() {
        return new Response(400, EventType.RESPONSE, null);
    }

    public static Response error(EventType type, Object data) {
        return new Response(400, type, data);
    }

    public static Response error(EventType type) {
        return new Response(400, type, null);
    }

    public static Response error(Object data) {
        return new Response(400, EventType.RESPONSE, data);
    }

    public static Response code(int code, EventType type, Object data) {
        return new Response(code, type, data);
    }

    public static Response code(int code, EventType type) {
        return new Response(code, type, null);
    }
}
