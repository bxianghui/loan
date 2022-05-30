package com.bxx.loan.core.controller.web;


import com.bxx.loan.base.util.JWTUtils;
import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.pojo.vo.BorrowerVO;
import com.bxx.loan.core.service.BorrowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 借款人 前端控制器
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
//@CrossOrigin
@Api(tags = "借款人")
@RestController
@RequestMapping("/web/core/borrower")
public class WebBorrowerController {

    @Resource
    private BorrowerService borrowerService;


    @ApiOperation(value = "获取当前用户的借款状态")
    @GetMapping(value = "/auth/getBorrowerStatus")
    public Result getBorrowerStatus(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JWTUtils.getUserId(token);
        Integer data = borrowerService.getBorrowerStatus(userId);
        return Result.success().data(data).message("获取当前用户借款状态");
    }

    @ApiOperation(value = "提交借款信息申请")
    @PostMapping(value = "/auth/save")
    public Result save(
            @ApiParam(value = "BorrowerVO对象", required = true)
            @RequestBody BorrowerVO borrowerVO,
            HttpServletRequest request
            ){
        String token = request.getHeader("token");
        Long userId = JWTUtils.getUserId(token);
        borrowerService.save(borrowerVO, userId);
        return Result.success().message("借款信息提交成功");
    }

}

