package com.bxx.loan.core.controller.web;


import com.alibaba.fastjson.JSON;
import com.bxx.loan.base.util.JWTUtils;
import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.hfb.RequestHelper;
import com.bxx.loan.core.pojo.entity.Lend;
import com.bxx.loan.core.pojo.entity.LendItem;
import com.bxx.loan.core.pojo.vo.InvestVO;
import com.bxx.loan.core.service.LendItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
@RequestMapping("/web/core/lendItem")
@Slf4j
public class WebLendItemController {

    @Resource
    private LendItemService lendItemService;

    @ApiOperation(value = "投资记录表")
    @GetMapping("/list")
    public Result getList(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JWTUtils.getUserId(token);
        List<LendItem> list = lendItemService.getListByUserId(userId);
        return Result.success().data(list);
    }

    @ApiOperation(value = "单个投资记录")
    @GetMapping("/list/{lendId}")
    public Result getDetailById(
            @ApiParam(value = "lendId", example = "1", required = true)
            @PathVariable("lendId") Long lendId
    ){
        List<LendItem> lendItems = lendItemService.getListById(lendId);
        return Result.success().data(lendItems);
    }

    @ApiOperation(value = "投资")
    @PostMapping("/auth/commitInvest")
    public Result commitInvest(
            @RequestBody InvestVO investVO,
            HttpServletRequest request
            ){
        String token = request.getHeader("token");
        Long userId = JWTUtils.getUserId(token);
        String userName = JWTUtils.getUserName(token);
        String formStr = lendItemService.commitInvest(investVO.getLendId(), investVO.getInvestAmount(), userId, userName);
        return Result.success().data(formStr);
    }

    @ApiOperation(value = "回调投资接口")
    @PostMapping("/notify")
    public String notify( HttpServletRequest request){
        Map<String, Object> param = RequestHelper.switchMap(request.getParameterMap());
        log.info("从汇付宝获取的参数信息" + JSON.toJSONString(param));

        boolean result = RequestHelper.isSignEquals(param);
        if (!result){
            log.error("签名错误:" + JSON.toJSONString(param));
            return "fail";
        }else{
            if("0001".equals(param.get("resultCode"))) {
                log.info("绑定成功");
                lendItemService.notify(param);
            }else{
                log.error("异步绑定失败:" + JSON.toJSONString(param));
                return "fail";
            }
        }
        return "success";
    }

}

