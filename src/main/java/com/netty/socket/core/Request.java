package com.netty.socket.core;

import com.netty.enums.socket.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/6 23:37
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Request {
    /**
     * 事件类型
     */
    private EventType type;
    /**
     * 请求数据
     */
    private Object data;
}
