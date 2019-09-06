package com.netty.enums.others;

import lombok.Getter;

/**
 * redis前缀
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/8/28 14:06
 **/
public enum RedisKeyEnum {

    /**
     * socketId
     */
    CODE("socket_id");

    @Getter
    private String key;

    RedisKeyEnum(String key) {
        this.key = key;
    }
}
