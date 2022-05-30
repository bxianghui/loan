package com.bxx.loan.core.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.pojo.entity.UserInfo;
import com.bxx.loan.core.pojo.query.UserInfoQuery;
import com.bxx.loan.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户基本信息 前端控制器 管理员控制台显示
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
//@CrossOrigin
@Api(tags = "会员列表管理")
@RestController
@RequestMapping("/admin/core/userInfo")
public class AdminUserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation(value = "查询会员信息，按照分页的方式")
    @GetMapping(value = "/list/{current}/{size}")
    public Result list(
            @ApiParam(value = "页码", example = "1", required = true)
            @PathVariable("current") Long current,
            @ApiParam(value = "每页数据数量", example = "10", required = true)
            @PathVariable("size") Long size,
            @ApiParam(value = "查询条件", required = true)
            UserInfoQuery userInfoQuery
    ){
        Page<UserInfo> page = new Page<>(current,size);

        page = userInfoService.pageSelect(page, userInfoQuery);

        Map<String, Object> data = new HashMap<>();

        long total = page.getTotal();

        data.put("total", total);

        List<UserInfo> records = page.getRecords();

        data.put("list", records);

        return Result.success().data(data);
    }

    @ApiOperation(value = "改变当前用户状态  锁定或者正常")
    @PutMapping(value = "/lock/{id}/{status}")
    public Result updateStatus(
            @ApiParam(value = "当前数据id", example = "7", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "当前用户帐号状态", example = "0", required = true)
            @PathVariable("status") Integer status
    ){
        boolean result = userInfoService.lock(id, status);

        if (result){
            return Result.success().message("更新状态成功");
        }else {
            return Result.error().message("更新状态失败");
        }
    }
    @ApiOperation(value = "重置密码")
    @PutMapping(value = "/reset/{id}")
    public Result resetPassword(
            @ApiParam(value = "当前数据id", example = "7", required = true)
            @PathVariable("id") Long id
    ){
        userInfoService.resetPassword(id);

        return Result.success().message("密码重置成功");
    }
}

