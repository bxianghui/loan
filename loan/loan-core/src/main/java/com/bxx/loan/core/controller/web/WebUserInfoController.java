package com.bxx.loan.core.controller.web;


import com.bxx.loan.common.result.ResponseEnum;
import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.pojo.entity.UserInfo;
import com.bxx.loan.core.pojo.vo.RegisterVO;
import com.bxx.loan.core.pojo.vo.UserIndexVO;
import com.bxx.loan.core.pojo.vo.UserInfoVo;
import com.bxx.loan.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
//@CrossOrigin
@Api(tags = "用户注册功能")
@RestController
@RequestMapping("web/core/userInfo")
public class WebUserInfoController {

    @Resource
    private UserInfoService userInfoService;

    /**
     * 服务调用 查询是否该手手机号已被注册
     */
    @ApiOperation("查询手机号是否已被使用")
    @GetMapping("/get/{mobile}")
    public Result getByMobile(
            @ApiParam(value = "手机号", required = true)
            @PathVariable("mobile") String mobile){

        boolean result = userInfoService.getByMobile(mobile);
        return Result.success().data(result);
    }

    @ApiOperation("注册账号")
    @PostMapping("/register")
    public Result register(
            @ApiParam(value = "注册人信息")
            @RequestBody RegisterVO registerVO){
        return userInfoService.register(registerVO);
    }


    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(
            @ApiParam(value = "登录信息")
            @RequestBody UserInfo userInfo, HttpServletRequest request){
        String ip = request.getRemoteAddr();
        return userInfoService.login(userInfo, ip);
    }

    @ApiOperation("判断cookie的token是否合法")
    @GetMapping("/checkToken")
    public Result checkToken(HttpServletRequest request){
        userInfoService.checkToken(request);
        return Result.success();
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/auth/getIndexUserInfo")
    public Result getIndexUserInfo(HttpServletRequest request){
        UserIndexVO userIndexVO = userInfoService.getIndexUserInfo(request);
        if (userIndexVO == null) return Result.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
        else return Result.success().data(userIndexVO);
    }

}

