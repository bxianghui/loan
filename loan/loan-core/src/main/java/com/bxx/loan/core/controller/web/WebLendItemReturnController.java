package com.bxx.loan.core.controller.web;


import com.bxx.loan.base.util.JWTUtils;
import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.pojo.entity.LendItemReturn;
import com.bxx.loan.core.service.LendItemReturnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 标的出借回款记录表 前端控制器
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Api(tags = "回款计划管理")
//@CrossOrigin
@RestController
@RequestMapping("/web/core/lendItemReturn")
public class WebLendItemReturnController {

    @Resource
    private LendItemReturnService lendItemReturnService;


    @ApiOperation("回款计划列表")
    @GetMapping("/list/{lendId}")
    public Result listById(
            @ApiParam(value = "lendId", example = "4", required = true)
            @PathVariable("lendId") Long lendId,
            HttpServletRequest request
    ){
        String token = request.getHeader("token");
        Long userId = JWTUtils.getUserId(token);
        List<LendItemReturn> data = lendItemReturnService.listById(userId, lendId);
        return Result.success().data(data);
    }

    @ApiOperation("回款计划")
    @GetMapping("/list")
    public Result list(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JWTUtils.getUserId(token);
        List<LendItemReturn> data = lendItemReturnService.listByUserId(userId);
        return Result.success().data(data);
    }
}

