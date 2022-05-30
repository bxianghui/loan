package com.bxx.loan.sms.service.impl;

import com.bxx.loan.sms.service.ISendMessageService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author : bu
 * @date : 2022/5/20  12:36
 */
@Service
public class SendMessageServiceImpl implements ISendMessageService {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public String send(String mobile) {
        String validCode = null;

        validCode = (String) redisTemplate.opsForValue().get("valid:code:" + mobile);
        if (validCode == null) {
            validCode = mobile.substring(0, 4);
        } else {
            return "请在1分钟后再获取验证码。";
        }

        redisTemplate.opsForValue().set("valid:code:" + mobile, validCode, 1, TimeUnit.MINUTES);
        return "验证码发送成功，请注意查收。";
    }

}
