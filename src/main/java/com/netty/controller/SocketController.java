package com.netty.controller;

import com.fanglin.common.annotation.Token;
import com.fanglin.common.core.others.Ajax;
import com.fanglin.common.utils.UploadUtils;
import com.netty.service.SocketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * socket
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:36
 **/
@RestController
@RequestMapping("/socket/common/")
@Api(value = "/socket/common/", tags = {"APP-其他"})
public class SocketController {

    @Autowired
    SocketService socketService;

    @ApiOperation("发送消息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户id", required = true)
    })
    @Token
    @PostMapping("sendMessage")
    public Ajax refresh(@RequestParam Integer userId, @RequestParam String message) {
        socketService.sendMessage(userId, message);
        return Ajax.ok();
    }

}
