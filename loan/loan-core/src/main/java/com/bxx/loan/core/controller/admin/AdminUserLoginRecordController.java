package com.bxx.loan.core.controller.admin;


import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.pojo.entity.UserLoginRecord;
import com.bxx.loan.core.service.UserLoginRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户登录记录表 前端控制器
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Api(tags = "用户登录记录")
//@CrossOrigin
@RestController
@RequestMapping("/admin/core/userLoginRecord")
public class AdminUserLoginRecordController {

    @Resource
    private UserLoginRecordService userLoginRecordService;

    @ApiOperation(value = "查询用户的最近50次的登录记录")
    @GetMapping("/listTop50/{id}")
    public Result getListTop50(
            @ApiParam(value = "用户id", required = true)
            @PathVariable("id") Long userId
    ){
        List<UserLoginRecord> list = userLoginRecordService.getListTop50(userId);
        return Result.success().data(list);
    }
}

