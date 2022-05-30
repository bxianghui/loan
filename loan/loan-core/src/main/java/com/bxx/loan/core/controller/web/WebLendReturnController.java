package com.bxx.loan.core.controller.web;


import com.alibaba.fastjson.JSON;
import com.bxx.loan.base.util.JWTUtils;
import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.hfb.RequestHelper;
import com.bxx.loan.core.pojo.entity.LendItem;
import com.bxx.loan.core.pojo.entity.LendReturn;
import com.bxx.loan.core.service.LendReturnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 还款记录表 前端控制器
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
//@CrossOrigin
@Api(tags = "回款计划表")
@RestController
@Slf4j
@RequestMapping("/web/core/lendReturn")
public class WebLendReturnController {

    @Resource
    private LendReturnService lendReturnService;

    @ApiOperation(value = "还款计划表")
    @GetMapping("/list")
    public Result getList(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JWTUtils.getUserId(token);
        List<LendReturn> list = lendReturnService.getListByUserId(userId);
        return Result.success().data(list);
    }

    @ApiOperation(value = "单个lendId的回款记录")
    @GetMapping("/list/{lendId}")
    public Result getDetailById(
            @ApiParam(value = "lendId", example = "1", required = true)
            @PathVariable("lendId") Long lendId
    ){
        List<LendReturn> lendReturns = lendReturnService.getListById(lendId);
        return Result.success().data(lendReturns);
    }

    @ApiOperation(value = "还款")
    @PostMapping("/auth/commitReturn/{lendReturnId}")
    public Result commitReturn(
            @ApiParam(value = "lendReturnId", example = "1", required = true)
            @PathVariable("lendReturnId") Long lendReturnId
    ){
        String formStr = lendReturnService.commitReturn(lendReturnId);
        return Result.success().data(formStr);
    }

    @ApiOperation("还款回调")
    @PostMapping("/notifyUrl")
    public String notifyUrl(HttpServletRequest request) {

        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("还款异步回调：" + JSON.toJSONString(paramMap));

        //校验签名
        if(RequestHelper.isSignEquals(paramMap)) {
            if("0001".equals(paramMap.get("resultCode"))) {
                lendReturnService.notify(paramMap);
            } else {
                log.info("还款异步回调失败：" + JSON.toJSONString(paramMap));
                return "fail";
            }
        } else {
            log.info("还款异步回调签名错误：" + JSON.toJSONString(paramMap));
            return "fail";
        }
        return "success";
    }
}

