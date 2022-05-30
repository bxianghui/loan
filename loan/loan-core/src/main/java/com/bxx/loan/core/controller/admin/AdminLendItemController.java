package com.bxx.loan.core.controller.admin;


import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.pojo.entity.LendItem;
import com.bxx.loan.core.service.LendItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 标的出借记录表 前端控制器
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
//@CrossOrigin
@Api(tags = "投资记录")
@RestController
@RequestMapping("/admin/core/lendItem")
public class AdminLendItemController {

    @Resource
    private LendItemService lendItemService;

    @ApiOperation(value = "获取投资信息列表")
    @GetMapping("/list/{lendId}")
    public Result listById(
            @ApiParam(value = "id", required = true ,example = "1")
            @PathVariable("lendId") Long lendId
    ){
        List<LendItem> itemList = lendItemService.getListById(lendId);
        return Result.success().data(itemList);
    }

}

