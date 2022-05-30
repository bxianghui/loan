package com.bxx.loan.core.controller.admin;


import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.pojo.entity.Lend;
import com.bxx.loan.core.pojo.vo.LendVO;
import com.bxx.loan.core.service.LendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@Api(tags = "标的列表管理")
@RestController
@RequestMapping("/admin/core/lend")
public class AdminLendController {

    @Resource
    private LendService lendService;

    @ApiOperation(value = "获取标的的列表")
    @GetMapping("/list")
    public Result list(){
        List<Lend> list = lendService.getList();
        return Result.success().data(list);
    }

    @ApiOperation(value = "标的信息的详细信息")
    @GetMapping("/show/{id}")
    public Result show(
            @ApiParam(value = "id", required = true, example = "1")
            @PathVariable("id") Long id
    ){
        Map<String, Object> data = lendService.show(id);
        return Result.success().data(data);
    }

    @ApiOperation("放款")
    @GetMapping("/makeLoan/{id}")
    public Result makeLoan(
            @ApiParam(value = "标的id", required = true)
            @PathVariable("id") Long id
    ){
        lendService.makeLoan(id);
        return Result.success().message("放款成功");
    }
}

