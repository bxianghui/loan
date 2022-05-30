package com.bxx.loan.core.controller.admin;


import com.bxx.loan.common.exception.Assert;
import com.bxx.loan.common.result.ResponseEnum;
import com.bxx.loan.core.pojo.entity.IntegralGrade;
import com.bxx.loan.core.service.IntegralGradeService;
import com.bxx.loan.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 积分等级表 前端控制器
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Api(tags = "积分管理系统")
//@CrossOrigin
@RestController
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {

    @Resource
    private IntegralGradeService integralGradeService;

    @ApiOperation(value = "获取积分管理的列表")
    @GetMapping(value = "/list")
    public Result getIntegralGradeList(){
        List<IntegralGrade> list = integralGradeService.list();
        if (list != null){
            return Result.success().data(list);
        }else {
            return Result.error();
        }
    }

    @ApiOperation(value = "根据id删除积分管理记录", notes = "逻辑删除")
    @DeleteMapping(value = "/delete/{id}")
    public Result removeById(
            @PathVariable("id")
            @ApiParam(value = "id值", required = true, example = "1") Long id){

        if (integralGradeService.removeById(id)) {
            return Result.success().data(true).message("删除成功");
        }else{
            return Result.success().message("删除失败");
        }
    }

    @ApiOperation(value = "插入积分管理记录")
    @PostMapping(value = "/insert")
    public Result insertIntegralGrade(
            @ApiParam(value = "积分等级对象", required = true)
            @RequestBody IntegralGrade integralGrade){

        Assert.notNull(integralGrade.getBorrowAmount(), ResponseEnum.BAD_SQL_GRAMMAR_ERROR);

        if (integralGradeService.save(integralGrade)) {
            return Result.success().data(true).message("插入成功");
        }else {
            return Result.error().message("插入失败");
        }
    }

    @ApiOperation(value = "通过id查询积分管理记录")
    @GetMapping(value = "/get/{id}")
    public Result getById(
            @ApiParam(value = "需要查询的id值", example = "1", required = true)
            @PathVariable("id") Long id) {
        IntegralGrade integralGrade = integralGradeService.getById(id);
        if (integralGrade != null){
            return Result.success().data(integralGrade).message("查询成功");
        }else {
            return Result.error().message("查询失败");
        }
    }

    @ApiOperation(value = "通过id进行修改")
    @PutMapping(value = "/update")
    public Result updateById(
            @ApiParam(value = "积分等级对象",required = true)
            @RequestBody IntegralGrade integralGrade){
        if (integralGradeService.updateById(integralGrade)) {
            return Result.success().message("更新成功");
        }else {
            return Result.error().message("更新失败");
        }

    }
}

