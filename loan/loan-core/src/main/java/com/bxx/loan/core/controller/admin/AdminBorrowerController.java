package com.bxx.loan.core.controller.admin;


import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.pojo.vo.BorrowerApprovalVO;
import com.bxx.loan.core.pojo.vo.BorrowerDetailVO;
import com.bxx.loan.core.service.BorrowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@Api(tags = "借款管理")
@RestController
@RequestMapping("/admin/core/borrower")
public class AdminBorrowerController {

    @Resource
    private BorrowerService borrowerService;

    @GetMapping(value = "/list/{page}/{size}")
    @ApiOperation(value = "根据关键字(姓名， 手机， 身份证)获取借款人列表")
    public Result list(
            @ApiParam(value = "页码", example = "1", required = true)
            @PathVariable("page") Long page,
            @ApiParam(value = "每页的条数", example = "3", required = true)
            @PathVariable("size") Long size,
            @ApiParam(value = "搜索条件", example = "张三", required = true)
            @RequestParam("keyword") String keyWords
    ){
        Map<String, Object> data = borrowerService.pageSelect(page, size, keyWords);
        return Result.success().data(data).message("成功获取");
    }

    @ApiOperation(value = "展示当前借款人的具体信息")
    @GetMapping(value = "/show/{id}")
    public Result show(
            @ApiParam(value = "当前记录的id", example = "1", required = true)
            @PathVariable("id") Long id
    ){
        BorrowerDetailVO data = borrowerService.getBorrowerDetailVOById(id);
        return Result.success().data(data);
    }

    @ApiOperation(value = "审批提交")
    @PostMapping("/approval")
    public Result approval(@RequestBody BorrowerApprovalVO borrowerApprovalVO){
        borrowerService.approval(borrowerApprovalVO);
        return Result.success().message("审批成功");
    }

}

