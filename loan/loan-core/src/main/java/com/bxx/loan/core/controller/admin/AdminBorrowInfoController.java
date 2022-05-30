package com.bxx.loan.core.controller.admin;


import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.pojo.entity.BorrowInfo;
import com.bxx.loan.core.pojo.vo.BorrowInfoApprovalVO;
import com.bxx.loan.core.service.BorrowInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 前端控制器
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
//@CrossOrigin
@Api(tags = "借款列表管理")
@RestController
@RequestMapping("/admin/core/borrowInfo")
public class AdminBorrowInfoController {

    @Resource
    private BorrowInfoService borrowInfoService;

    @ApiOperation(value = "获取借款的全列表")
    @GetMapping(value = "/list")
    public Result list(){
        List<BorrowInfo> list = borrowInfoService.listJoin();
        return Result.success().data(list);
    }


    @ApiOperation(value = "查看借款记录的详情")
    @GetMapping(value = "/show/{id}")
    public Result show(
            @ApiParam(value = "当前记录的id值", example = "1", required = true)
            @PathVariable Long id
    ){
        Map<String, Object> data = borrowInfoService.getDetailById(id);
        return Result.success().data(data);
    }

    @ApiOperation(value = "审批通过操作")
    @PostMapping(value = "/approval")
    public Result approval(@RequestBody BorrowInfoApprovalVO borrowInfoApprovalVO){
        borrowInfoService.approval(borrowInfoApprovalVO);
        return Result.success().message("审批成功");
    }
}

