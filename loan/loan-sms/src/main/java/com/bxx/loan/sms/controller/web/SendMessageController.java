package com.bxx.loan.sms.controller.web;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.bxx.loan.common.result.ResponseEnum;
import com.bxx.loan.common.result.Result;
import com.bxx.loan.sms.fallback.ISendMessageFallBack;
import com.bxx.loan.sms.service.ISendMessageService;
import com.bxx.loan.sms.service.SendMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : bu
 * @date : 2022/5/20  12:32
 */
//@CrossOrigin
@Api(tags = "注册时发送短信验证码")
@RestController
@RequestMapping("/web/sms")
public class SendMessageController {

    @Resource
    private ISendMessageService iSendMessageService;

    @Resource
    private SendMessageService sendMessageService;


    @ApiOperation("获取验证码")
    @GetMapping("/send/{mobile}")
    @SentinelResource(value = "sendValidCode", fallbackClass = ISendMessageFallBack.class, fallback = "sendFallBack")
    public Result getValidCode(
            @ApiParam(value = "电话号码", required = true)
            @PathVariable("mobile") String mobile
    ) {

        boolean data = (boolean) sendMessageService.getByMobile(mobile).getData();
        if (!data) {
            return Result.setResult(ResponseEnum.MOBILE_EXIST_ERROR);
        }
        String message = iSendMessageService.send(mobile);
        return Result.success().message(message);
    }

}
