package com.bxx.loan.sms.fallback;

import com.bxx.loan.common.result.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author : bu
 * @date : 2022/5/20  13:01
 */
@Component
public class ISendMessageFallBack {

    public static Result sendFallBack(@PathVariable("mobile") String mobile){
        return Result.error();
    }

}
