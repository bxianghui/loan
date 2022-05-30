package com.bxx.loan.core.controller.web;


import com.alibaba.fastjson.JSON;
import com.bxx.loan.base.util.JWTUtils;
import com.bxx.loan.common.result.ResponseEnum;
import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.hfb.RequestHelper;
import com.bxx.loan.core.service.UserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 前端控制器
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
//@CrossOrigin
@Api(tags = "账户充值信息")
@RestController
@Slf4j
@RequestMapping("/web/core/userAccount")
public class WebUserAccountController {

    @Resource
    private UserAccountService userAccountService;


    @ApiOperation(value = "充值")
    @PostMapping("/auth/commitCharge/{chargeAmt}")
    public Result commitCharge(
            @ApiParam(value = "充值金额", required = true, example = "200")
            @PathVariable("chargeAmt") BigDecimal chargeAmt, HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JWTUtils.getUserId(token);
        String formStr = userAccountService.commitCharge(chargeAmt, userId);
        return Result.success().data(formStr);
    }

    @ApiOperation(value = "充值接口")
    @PostMapping("/notify")
    public String notifyCharge(HttpServletRequest request){
        Map<String, Object> param = RequestHelper.switchMap(request.getParameterMap());
        log.info("用户充值数据更新" + JSON.toJSONString(param));

        boolean result = RequestHelper.isSignEquals(param);
        if (!result){
            log.error("签名错误:" + JSON.toJSONString(param));
            return "fail";
        }else {
            if("0001".equals(param.get("resultCode"))) {
                log.info("充值数据更新成功");
                userAccountService.notifyCharge(param);
            } else {
                log.info("充值数据更新失败：" + JSON.toJSONString(param));
                return "fail";
            }
        }
        return "success";
    }

    @ApiOperation(value = "提现")
    @PostMapping("/auth/commitWithdraw/{fetchAmt}")
    public Result commitWithdraw(
            @ApiParam(value = "充值金额", required = true, example = "200")
            @PathVariable("fetchAmt") BigDecimal fetchAmt, HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JWTUtils.getUserId(token);
        String formStr = userAccountService.commitWithdraw(fetchAmt, userId);
        if(formStr.equals("fail")){
            return Result.setResult(ResponseEnum.NOT_SUFFICIENT_FUNDS_ERROR);
        }
        return Result.success().data(formStr);
    }

    @ApiOperation(value = "提现接口")
    @PostMapping("/notifyWithdraw")
    public String notifyWithdraw(HttpServletRequest request){
        Map<String, Object> param = RequestHelper.switchMap(request.getParameterMap());
        log.info("用户提现请求" + JSON.toJSONString(param));

        boolean result = RequestHelper.isSignEquals(param);
        if (!result){
            log.error("签名错误:" + JSON.toJSONString(param));
            return "fail";
        }else {
            if("0001".equals(param.get("resultCode"))) {
                log.info("提现数据更新成功");
                userAccountService.notifyWithDraw(param);
            } else {
                log.info("提现数据更新失败：" + JSON.toJSONString(param));
                return "fail";
            }
        }
        return "success";
    }

    @ApiOperation(value = "查询余额")
    @GetMapping("/auth/getAccount")
    public Result getAccount(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JWTUtils.getUserId(token);
        BigDecimal amount = userAccountService.getAccount(userId);
        return Result.success().data(amount);
    }
}

