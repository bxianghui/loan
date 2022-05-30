package com.bxx.loan.core.controller.admin;


import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.pojo.entity.LendReturn;
import com.bxx.loan.core.service.LendReturnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 还款记录表 前端控制器
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
//@CrossOrigin
@Api(tags = "还款计划")
@RestController
@RequestMapping("/admin/core/lendReturn")
public class AdminLendReturnController {

    @Resource
    private LendReturnService lendReturnService;

    @ApiOperation(value = "获取还款计划")
    @GetMapping("/list/{lendId}")
    public Result listById(
            @ApiParam(value = "id", required = true, example = "1")
            @PathVariable("lendId") Long lendId
    ){
        List<LendReturn> lendReturnList = lendReturnService.getListById(lendId);
        return Result.success().data(lendReturnList);
    }

}

