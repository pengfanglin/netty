package com.netty.enums.socket;

import com.fanglin.common.core.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户端事件类型
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/9/6 0:35
 **/
@AllArgsConstructor
public enum ClientEventType implements CodeEnum {

    /**
     * 开奖通知
     */
    LOTTERY(1);

    @Getter
    int code;
}
