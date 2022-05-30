package com.bxx.loan.core.controller.web;


import com.bxx.loan.base.util.JWTUtils;
import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.pojo.vo.BorrowInfoVO;
import com.bxx.loan.core.service.BorrowInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * <p>
 * 借款信息表 前端控制器
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
//@CrossOrigin
@Api(tags = "借款信息")
@RestController
@RequestMapping("/web/core/borrowInfo")
public class WebBorrowInfoController {

    @Resource
    private BorrowInfoService borrowInfoService;

    @ApiOperation(value = "获取借款信息审核状态")
    @GetMapping("/auth/getBorrowInfoStatus")
    public Result getBorrowInfoStatus(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JWTUtils.getUserId(token);
        Integer status = borrowInfoService.getBorrowInfoStatus(userId);
        return Result.success().data(status);
    }

    @ApiOperation(value = "=获取借款做最大金额")
    @GetMapping("/auth/getBorrowAmount")
    public Result getBorrowAmount(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JWTUtils.getUserId(token);
        BigDecimal amount = borrowInfoService.getBorrowAmount(userId);
        return Result.success().data(amount);
    }


    @ApiOperation(value = "提交借款申请")
    @PostMapping("/auth/save")
    public Result save(@RequestBody BorrowInfoVO borrowInfoVO, HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JWTUtils.getUserId(token);
        borrowInfoService.save(borrowInfoVO, userId);
        return Result.success().message("提交借款申请成功");
    }
}

