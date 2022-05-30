package com.bxx.loan.core.controller.web;


import com.bxx.loan.base.util.JWTUtils;
import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.pojo.entity.Lend;
import com.bxx.loan.core.pojo.vo.LendVO;
import com.bxx.loan.core.service.LendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 前端控制器
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
//@CrossOrigin
@Api(tags = "投资信息管理")
@RestController
@RequestMapping("/web/core/lend")
public class WebLendController {

    @Resource
    private LendService lendService;

    @ApiOperation(value = "借贷列表")
    @GetMapping("/list")
    public Result getList(){
        List<Lend> list = lendService.getList();
        return Result.success().data(list);
    }

    @ApiOperation(value = "借贷列表")
    @GetMapping("/lendList")
    public Result getListByUserId(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JWTUtils.getUserId(token);
        List<Lend> list = lendService.getListByUserId(userId);
        return Result.success().data(list);
    }

    @ApiOperation(value = "计算收益")
    @GetMapping("/getInterestCount/{investAmount}/{lendYearRate}/{period}/{returnMethod}")
    public Result getInterestCount(
            @ApiParam(value = "投资金额", example = "2000", required = true)
            @PathVariable("investAmount")BigDecimal investAmount,
            @ApiParam(value = "年化利率", example = "0.12", required = true)
            @PathVariable("lendYearRate")BigDecimal lendYearRate,
            @ApiParam(value = "还款周期", example = "1", required = true)
            @PathVariable("period")Integer period,
            @ApiParam(value = "还款方式", example = "1", required = true)
            @PathVariable("returnMethod")Integer returnMethod
            ){
        BigDecimal interest = lendService.getInterestCount(investAmount, lendYearRate, period, returnMethod);
        return Result.success().data(interest);
    }

    @ApiOperation(value = "获取投资详细信息")
    @GetMapping("/show/{lendId}")
    public Result getDetailById(
            @ApiParam(value = "lendId", required = true, example = "1")
            @PathVariable("lendId") Long lendId
            ){
        Map<String, Object> data = lendService.show(lendId);
        return Result.success().data(data);
    }


}

