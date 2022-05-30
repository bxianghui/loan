package com.bxx.loan.core.controller.web;


import com.bxx.loan.base.util.JWTUtils;
import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.pojo.entity.TransFlow;
import com.bxx.loan.core.service.TransFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 交易流水表 前端控制器
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Api(tags = "流水单管理")
//@CrossOrigin
@RestController
@RequestMapping("/web/core/transFlow")
public class WebTransFlowController {

    @Resource
    private TransFlowService transFlowService;


    @ApiOperation("获取流水纪录列表")
    @GetMapping("/list")
    public Result list(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JWTUtils.getUserId(token);
        List<TransFlow> list = transFlowService.list(userId);
        return Result.success().data(list);
    }

}

