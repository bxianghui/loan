package com.bxx.loan.sms.service;

import com.bxx.loan.common.result.Result;
import com.bxx.loan.sms.fallback.SendMessageServiceFallBack;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author : bu
 * @date : 2022/5/20  12:34
 */
@FeignClient(name = "loan-core", fallback = SendMessageServiceFallBack.class)
public interface SendMessageService {

    @GetMapping("/web/core/userInfo/get/{mobile}")
    public Result getByMobile(@ApiParam(value = "手机号", required = true) @PathVariable("mobile") String mobile);

}
