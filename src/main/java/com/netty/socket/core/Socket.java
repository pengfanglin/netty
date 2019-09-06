package com.netty.socket.core;

import com.netty.enums.socket.ResponseEventType;
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
public class Socket {
    /**
     * 状态码  200:成功  400:失败
     */
    private int code;
    /**
     * 响应事件类型
     */
    private ResponseEventType type;
    /**
     * 结果集
     */
    private Object data;

    public static Socket ok() {
        return new Socket(200, ResponseEventType.RESPONSE, null);
    }

    public static Socket ok(Object data) {
        return new Socket(200, ResponseEventType.RESPONSE, data);
    }

    public static Socket ok(ResponseEventType type, Object data) {
        return new Socket(200, type, data);
    }

    public static Socket error() {
        return new Socket(400, ResponseEventType.RESPONSE, null);
    }

    public static Socket error(ResponseEventType type, Object data) {
        return new Socket(400, type, data);
    }

    public static Socket error(Object data) {
        return new Socket(400, ResponseEventType.RESPONSE, data);
    }

    public static Socket code(int code, ResponseEventType type, Object data) {
        return new Socket(code, type, data);
    }
}
