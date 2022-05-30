package com.bxx.loan.core.controller.web;


import com.alibaba.fastjson.JSON;
import com.bxx.loan.base.util.JWTUtils;
import com.bxx.loan.common.result.ResponseEnum;
import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.hfb.RequestHelper;
import com.bxx.loan.core.pojo.entity.UserBind;
import com.bxx.loan.core.pojo.vo.UserBindVO;
import com.bxx.loan.core.service.UserBindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 前端控制器
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
//@CrossOrigin
@Api(tags = "会员账号绑定")
@RestController
@Slf4j
@RequestMapping("/web/core/userBind")
public class WebUserBindController {

    @Resource
    private UserBindService userBindService;


    @ApiOperation(value = "绑定汇付包账号")
    @PostMapping("/auth/bind")
    public Result bind(@RequestBody UserBindVO userBindVO, HttpServletRequest request){
        //获取userId
        String token = request.getHeader("token");
        Long userId = JWTUtils.getUserId(token);
        String formStr = userBindService.bindHfb(userBindVO, userId);
        return Result.success().data(formStr);
    }

    @ApiOperation(value = "异步修改绑定状态")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request){
        //得到汇付包异步发送的请求参数
        Map<String, Object> param = RequestHelper.switchMap(request.getParameterMap());
        log.info("从汇付宝获取的参数信息" + JSON.toJSONString(param));

        boolean result = RequestHelper.isSignEquals(param);
        if (!result){
            log.error("签名错误:" + JSON.toJSONString(param));
            return "fail";
        }else{
            if("0001".equals(param.get("resultCode"))) {
                log.info("绑定成功");
                userBindService.notifyUserBind(param);
            }else{
                log.error("异步绑定失败:" + JSON.toJSONString(param));
                return "fail";
            }
        }
        return "success";
    }
}

