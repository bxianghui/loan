package com.bxx.loan.sms.fallback;

import com.bxx.loan.common.result.Result;
import com.bxx.loan.sms.service.SendMessageService;
import org.springframework.stereotype.Component;

/**
 * @author : bu
 * @date : 2022/5/20  13:05
 */
@Component
public class SendMessageServiceFallBack implements SendMessageService {

    @Override
    public Result getByMobile(String mobile) {
        return Result.error().message("服务器繁忙请稍后重试！");
    }

}
