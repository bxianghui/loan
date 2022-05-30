package com.bxx.loan.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bxx.loan.common.exception.Assert;
import com.bxx.loan.common.result.ResponseEnum;
import com.bxx.loan.core.enums.LendStatusEnum;
import com.bxx.loan.core.enums.TransTypeEnum;
import com.bxx.loan.core.hfb.FormHelper;
import com.bxx.loan.core.hfb.HfbConst;
import com.bxx.loan.core.hfb.RequestHelper;
import com.bxx.loan.core.mapper.*;
import com.bxx.loan.core.pojo.entity.Lend;
import com.bxx.loan.core.pojo.entity.LendItem;
import com.bxx.loan.core.pojo.entity.LendItemReturn;
import com.bxx.loan.core.pojo.entity.LendReturn;
import com.bxx.loan.core.pojo.vo.TransFlowVO;
import com.bxx.loan.core.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bxx.loan.core.util.LendNoUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 还款记录表 服务实现类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Service
public class LendReturnServiceImpl extends ServiceImpl<LendReturnMapper, LendReturn> implements LendReturnService {

    @Resource
    private LendItemReturnService lendItemReturnService;

    @Resource
    private UserAccountService userAccountService;

    @Resource
    private LendMapper lendMapper;

    @Resource
    private UserBindService userBindService;

    @Resource
    private TransFlowService transFlowService;

    @Resource
    private UserAccountMapper userAccountMapper;

    @Resource
    private LendItemMapper lendItemMapper;

    /**
     * 获取还款信息
     * @param lendId
     * @return
     */
    @Override
    public List<LendReturn> getListById(Long lendId) {
        QueryWrapper<LendReturn> lendReturnQueryWrapper = new QueryWrapper<>();
        lendReturnQueryWrapper.eq("lend_id", lendId);
        List<LendReturn> lendReturnList = baseMapper.selectList(lendReturnQueryWrapper);
        lendReturnList.forEach(lendReturn -> {
            if (lendReturn.getReturnDate().isBefore(LocalDate.now())){
                lendReturn.setOverdue(true);
                lendReturn.setOverdueTotal(lendReturn.getTotal());
                baseMapper.updateById(lendReturn);
            }
        });
        return lendReturnList;
    }

    /**
     * 还款计划表
     * @param userId
     * @return
     */
    @Override
    public List<LendReturn> getListByUserId(Long userId) {
        List<LendReturn> lendReturnList = baseMapper.selectList(new QueryWrapper<LendReturn>().eq("user_id", userId));
        lendReturnList.forEach(lendReturn -> {
            lendReturn.setLendStatus(lendReturn.getStatus()==0 ? "未还款" : "已还款");
        });
        return lendReturnList;
    }

    @Override
    public String commitReturn(Long lendReturnId) {

        LendReturn lendReturn = baseMapper.selectById(lendReturnId);

        //获取用户余额
        BigDecimal amount = userAccountService.getAccount(lendReturn.getUserId());
        Assert.isTrue(amount.doubleValue() >= lendReturn.getTotal().doubleValue(),
                ResponseEnum.NOT_SUFFICIENT_FUNDS_ERROR);

        //标的记录
        Lend lend = lendMapper.selectById(lendReturn.getLendId());
        //获取还款人的绑定协议号
        String bindCode = userBindService.getBindCodeById(lendReturn.getUserId());
        //组装参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        //商户商品名称
        paramMap.put("agentGoodsName", lend.getTitle());
        //批次号
        paramMap.put("agentBatchNo", lendReturn.getReturnNo());
        //还款人绑定协议号
        paramMap.put("fromBindCode", bindCode);
        //还款总额
        paramMap.put("totalAmt", lendReturn.getTotal());
        paramMap.put("note", "");
        //还款明细
        List<Map<String, Object>> lendItemReturnDetailList = lendItemReturnService.addReturnDetail(lendReturnId);
        paramMap.put("data", JSONObject.toJSONString(lendItemReturnDetailList));

        paramMap.put("voteFeeAmt", new BigDecimal(0));
        paramMap.put("notifyUrl", HfbConst.BORROW_RETURN_NOTIFY_URL);
        paramMap.put("returnUrl", HfbConst.BORROW_RETURN_RETURN_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);

        //构建自动提交表单
        String formStr = FormHelper.buildForm(HfbConst.BORROW_RETURN_URL, paramMap);
        return formStr;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void notify(Map<String, Object> paramMap) {


        //还款编号
        String agentBatchNo = (String)paramMap.get("agentBatchNo");

        boolean result = transFlowService.isSaveTransFlow(agentBatchNo);
        if(result){
            log.warn("幂等性返回");
            return;
        }

        //获取还款数据
        QueryWrapper<LendReturn> lendReturnQueryWrapper = new QueryWrapper<>();
        lendReturnQueryWrapper.eq("return_no", agentBatchNo);
        LendReturn lendReturn = baseMapper.selectOne(lendReturnQueryWrapper);

        //更新还款状态
        String voteFeeAmt = (String)paramMap.get("voteFeeAmt");
        lendReturn.setStatus(1);
        lendReturn.setFee(new BigDecimal(voteFeeAmt));
        lendReturn.setRealReturnTime(LocalDateTime.now());
        baseMapper.updateById(lendReturn);

        //更新标的信息
        Lend lend = lendMapper.selectById(lendReturn.getLendId());
        //如果是最后一次还款，那么久更新标的状态
        if(lendReturn.getLast()){
            lend.setStatus(LendStatusEnum.PAY_OK.getStatus());
            lendMapper.updateById(lend);
        }

        //还款账号转出金额
        BigDecimal totalAmt = new BigDecimal((String)paramMap.get("totalAmt"));
        String bindCode = userBindService.getBindCodeById(lendReturn.getUserId());
        userAccountMapper.updateAccount(bindCode, totalAmt.negate(), new BigDecimal(0));

        //还款流水
        TransFlowVO transFlowVO = new TransFlowVO(
                agentBatchNo,
                bindCode,
                totalAmt,
                TransTypeEnum.RETURN_DOWN,
                "借款人还款扣减，项目编号：" + lend.getLendNo() + "，项目名称：" + lend.getTitle());
        transFlowService.saveTransFlow(transFlowVO);

        //回款明细的获取
        List<LendItemReturn> lendItemReturnList = lendItemReturnService.selectLendItemReturnList(lendReturn.getId());
        lendItemReturnList.forEach(item -> {

            //更新回款状态
            item.setStatus(1);
            item.setRealReturnTime(LocalDateTime.now());
            lendItemReturnService.updateById(item);

            //更新出借信息
            LendItem lendItem = lendItemMapper.selectById(item.getLendItemId());
            lendItem.setRealAmount(lendItem.getRealAmount().add(item.getInterest())); //动态的实际收益
            lendItemMapper.updateById(lendItem);

            // 投资账号转入金额
            String investBindCode = userBindService.getBindCodeById(item.getInvestUserId());
            userAccountMapper.updateAccount(investBindCode, item.getTotal(), new BigDecimal(0));

            //回款流水
            TransFlowVO investTransFlowVO = new TransFlowVO(
                    LendNoUtils.getReturnItemNo(),
                    investBindCode,
                    item.getTotal(),
                    TransTypeEnum.INVEST_BACK,
                    "还款到账，项目编号：" + lend.getLendNo() + "，项目名称：" + lend.getTitle());
            transFlowService.saveTransFlow(investTransFlowVO);
        });
    }

}
