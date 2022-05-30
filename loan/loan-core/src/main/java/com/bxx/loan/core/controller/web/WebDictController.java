package com.bxx.loan.core.controller.web;


import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.enums.DictEnum;
import com.bxx.loan.core.pojo.entity.Dict;
import com.bxx.loan.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
//@CrossOrigin
@Api(tags = "数据字典接口")
@RestController
@RequestMapping("/web/core/dict")
public class WebDictController {

    @Resource
    private DictService dictService;

    @ApiOperation(value = "获取数据字典中收入列表")
    @GetMapping("/findByDictCode/{dictCode}")
    public Result findByDictCode(
            @ApiParam(value = "数据字典的编码", example = "income", required = true)
            @PathVariable("dictCode") String dictCode
            ){
        List<Dict> list = dictService.getDataList(dictCode);
        return Result.success().data(list);
    }

}

