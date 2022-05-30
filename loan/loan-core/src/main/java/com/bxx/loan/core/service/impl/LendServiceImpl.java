package com.bxx.loan.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bxx.loan.common.exception.BusinessException;
import com.bxx.loan.core.enums.DictEnum;
import com.bxx.loan.core.enums.LendStatusEnum;
import com.bxx.loan.core.enums.ReturnMethodEnum;
import com.bxx.loan.core.enums.TransTypeEnum;
import com.bxx.loan.core.hfb.HfbConst;
import com.bxx.loan.core.hfb.RequestHelper;
import com.bxx.loan.core.mapper.UserAccountMapper;
import com.bxx.loan.core.pojo.entity.*;
import com.bxx.loan.core.mapper.LendMapper;
import com.bxx.loan.core.pojo.vo.BorrowInfoApprovalVO;


import com.bxx.loan.core.pojo.vo.BorrowerDetailVO;
import com.bxx.loan.core.pojo.vo.TransFlowVO;
import com.bxx.loan.core.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bxx.loan.core.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务实现类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Service
@Slf4j
public class LendServiceImpl extends ServiceImpl<LendMapper, Lend> implements LendService {


    @Resource
    private BorrowerService borrowerService;

    @Resource
    private DictService dictService;

    @Resource
    private UserAccountMapper userAccountMapper;

    @Resource
    private TransFlowService transFlowService;

    @Resource
    private UserBindService userBindService;

    @Resource
    private LendItemService lendItemService;

    @Resource
    private LendReturnService lendReturnService;

    @Resource
    private LendItemReturnService lendItemReturnService;

    /**
     * 获取标的列表
     * @return
     */
    @Override
    public List<Lend> getList() {
        List<Lend> lends = baseMapper.selectList(null);
        lends.forEach(lend -> {
            lend.getParam().put("returnMethod", dictService.getDictString(DictEnum.RETURN_METHOD.getDictCode(), lend.getReturnMethod()));
            lend.getParam().put("status", LendStatusEnum.getMsgByStatus(lend.getStatus()));
        });
        return lends;
    }

    /**
     * 创建新的标的
     * @param borrowInfo
     * @param borrowInfoApprovalVO
     */
    @Override
    public void createLend(BorrowInfo borrowInfo, BorrowInfoApprovalVO borrowInfoApprovalVO) {
        Lend lend = new Lend();
        lend.setUserId(borrowInfo.getUserId());
        lend.setBorrowInfoId(borrowInfo.getId());
        lend.setLendNo(LendNoUtils.getLendNo());
        lend.setTitle(borrowInfoApprovalVO.getTitle());
        lend.setAmount(borrowInfo.getAmount());
        lend.setPeriod(borrowInfo.getPeriod());
        lend.setLendYearRate(borrowInfoApprovalVO.getLendYearRate().divide(new BigDecimal(100)));
        lend.setServiceRate(borrowInfoApprovalVO.getServiceRate().divide(new BigDecimal(100)));
        lend.setReturnMethod(borrowInfo.getReturnMethod());
        lend.setLowestAmount(new BigDecimal(100)); //最低投资100
        lend.setInvestAmount(new BigDecimal(0)); //已投金额
        lend.setInvestNum(0); //已投人数
        lend.setPublishDate(LocalDateTime.now()); //发布日期

        //起息日
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate lendStartDate = LocalDate.parse(borrowInfoApprovalVO.getLendStartDate(), dateTimeFormatter);
        lend.setLendStartDate(lendStartDate);

        //结束日
        lend.setLendEndDate(lendStartDate.plusMonths(borrowInfo.getPeriod()));
        lend.setLendInfo(borrowInfoApprovalVO.getLendInfo());//标的描述

        //计算平台预期收益
        BigDecimal perMonthRate = lend.getServiceRate().divide(new BigDecimal(12),
                8, BigDecimal.ROUND_DOWN);
        BigDecimal exceptAmount = lend.getAmount().multiply(
                new BigDecimal(lend.getPeriod()).multiply(perMonthRate));
        lend.setExpectAmount(exceptAmount);//平台预期收益

        lend.setRealAmount(new BigDecimal(0));  //实际收益
        lend.setStatus(LendStatusEnum.INVEST_RUN.getStatus());
        lend.setCheckTime(LocalDateTime.now());
        lend.setCheckAdminId(1L);

        baseMapper.insert(lend);
    }

    /**
     * 标的的详细信息
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> show(Long id) {
        Lend lend = baseMapper.selectById(id);
        lend.getParam().put("returnMethod", dictService.getDictString(DictEnum.RETURN_METHOD.getDictCode(), lend.getReturnMethod()));
        lend.getParam().put("status", LendStatusEnum.getMsgByStatus(lend.getStatus()));

        BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowDetail(lend.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("lend", lend);
        map.put("borrower", borrowerDetailVO);
        return map;
    }

    @Override
    public BigDecimal getInterestCount(BigDecimal investAmount, BigDecimal lendYearRate, Integer period, Integer returnMethod) {
        BigDecimal interestCount;
        if (returnMethod.intValue() == ReturnMethodEnum.ONE.getMethod()){
            interestCount = Amount1Helper.getInterestCount(investAmount, lendYearRate, period);
        }else if (returnMethod.intValue() == ReturnMethodEnum.TWO.getMethod()){
            interestCount = Amount2Helper.getInterestCount(investAmount, lendYearRate, period);
        }else if(returnMethod.intValue() == ReturnMethodEnum.THREE.getMethod()){
            interestCount = Amount3Helper.getInterestCount(investAmount, lendYearRate, period);
        }else{
            interestCount = Amount4Helper.getInterestCount(investAmount, lendYearRate, period);
        }
        return interestCount;
    }

    /**
     * 放款
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void makeLoan(Long id) {
        //获取标的
        Lend lend = baseMapper.selectById(id);

        //发送hfb请求
        Map<String, Object> map = new HashMap<>();
        map.put("agentId", HfbConst.AGENT_ID);
        map.put("agentProjectCode", lend.getLendNo());
        map.put("agentBillNo", LendNoUtils.getLoanNo());
        //月利率
        BigDecimal monthRate = lend.getServiceRate().divide(new BigDecimal(12), 8, BigDecimal.ROUND_DOWN);
        //平台服务费 = 已投金额 * 月年化 * 投资时长
        BigDecimal realAmount = lend.getInvestAmount().multiply(monthRate).multiply(new BigDecimal(lend.getPeriod()));
        map.put("mchFee", realAmount);
        map.put("timestamp", RequestHelper.getTimestamp());
        map.put("sign", RequestHelper.getSign(map));

        //提交远程请求
        JSONObject result = RequestHelper.sendRequest(map, HfbConst.MAKE_LOAD_URL);
        log.info("放款结果：" + result.toJSONString());

        //放款失败的处理
        if(!"0000".equals(result.getString("resultCode"))){
            throw new BusinessException(result.getString("resultMsg"));
        }


        //放款成功
//     （1）标的状态和标的平台收益：更新标的相关信息
        lend.setRealAmount(realAmount);//平台收益
        lend.setStatus(LendStatusEnum.PAY_RUN.getStatus());
        lend.setPaymentTime(LocalDateTime.now());
        baseMapper.updateById(lend);

//     （2）给借款账号转入金额
        Long userId = lend.getUserId();
        String bindCode = userBindService.getBindCodeById(userId);
        BigDecimal voteAmt = new BigDecimal(result.getString("voteAmt"));
        userAccountMapper.updateAccount(
                bindCode,
                voteAmt,
                new BigDecimal(0)
        );
//      (3) 增加流水信息
        //借款人流水信息
        TransFlowVO transFlowVO = new TransFlowVO(
                result.getString("agentBillNo"),
                bindCode,
                voteAmt,
                TransTypeEnum.BORROW_BACK,
                "项目放款，项目编号：" + lend.getLendNo() + "，项目名称：" + lend.getTitle()
        );
        transFlowService.saveTransFlow(transFlowVO);

//      (4) 投资人资金解冻
        List<LendItem> lendItems = lendItemService.getListById(id);
        lendItems.forEach(lendItem -> {
            Long investUserId = lendItem.getInvestUserId();
            String investBindCode = userBindService.getBindCodeById(investUserId);
            BigDecimal investAmount = lendItem.getInvestAmount();
            userAccountMapper.updateAccount(investBindCode, new BigDecimal(0), investAmount.negate());
//          (5) 投资人流水信息
            transFlowService.saveTransFlow(new TransFlowVO(
                    LendNoUtils.getTransNo(),
                    investBindCode,
                    investAmount,
                    TransTypeEnum.INVEST_UNLOCK,
                    "项目放款投标解锁，项目编号：" + lend.getLendNo() + "，项目名称：" + lend.getTitle()
            ));
        });
//      (6)生成借款人还款计划和投资人回款计划
        repaymentPlan(lend);
    }

    @Override
    public List<Lend> getListByUserId(Long userId) {
        List<Lend> lends = baseMapper.selectList(new QueryWrapper<Lend>().eq("user_id", userId));
        lends.forEach(lend -> {
            lend.getParam().put("returnMethod", dictService.getDictString(DictEnum.RETURN_METHOD.getDictCode(), lend.getReturnMethod()));
            lend.getParam().put("status", LendStatusEnum.getMsgByStatus(lend.getStatus()));
        });
        return lends;
    }

    /**
     * 还款计划
     * @param lend
     */
    private void repaymentPlan(Lend lend) {
       //创建还款计划
        List<LendReturn> lendReturnList = new ArrayList<>();
        //按照还款时间生成还款计划
        int len = lend.getPeriod();
        for (int i = 1; i <= len; i++) {
            LendReturn lendReturn = new LendReturn();

            lendReturn.setLendId(lend.getId());
            lendReturn.setBorrowInfoId(lend.getBorrowInfoId());
            lendReturn.setReturnNo(LendNoUtils.getReturnNo());
            lendReturn.setUserId(lend.getUserId());
            lendReturn.setAmount(lend.getAmount());
            lendReturn.setBaseAmount(lend.getInvestAmount());
            lendReturn.setCurrentPeriod(i);
            lendReturn.setLendYearRate(lend.getLendYearRate());
            lendReturn.setReturnMethod(lend.getReturnMethod());
            lendReturn.setFee(new BigDecimal(0));
            lendReturn.setReturnDate(lend.getLendStartDate().plusMonths(i));
            lendReturn.setOverdue(false);
            //是否为最后一期
            if (i == len){
                lendReturn.setLast(true);
            }else{
                lendReturn.setLast(false);
            }
            lendReturn.setStatus(0);//0 未归还 1已归还

            lendReturnList.add(lendReturn);
        }
        lendReturnService.saveBatch(lendReturnList);
        //所有投资人的所有期数回款记录
        List<LendItemReturn> allLendItemReturn = new ArrayList<>();
        //所有的投资人
        List<LendItem> lendItems = lendItemService.getListById(lend.getId());
        //获取一个投资人的所有期回款
        lendItems.forEach(lendItem -> {
            List<LendItemReturn> lendItemReturns = returnPlan(lendReturnList, lend, lendItem);
            allLendItemReturn.addAll(lendItemReturns);
        });

        lendReturnList.forEach(lendReturn -> {
            //通过filter、map、reduce将相关期数的回款数据过滤出来
            //将当前期数的所有投资人的数据相加，就是当前期数的所有投资人的回款数据（本金、利息、总金额）
            BigDecimal sumPrincipal = allLendItemReturn.stream()
                    .filter(item -> item.getLendReturnId().longValue() == lendReturn.getId().longValue())
                    .map(LendItemReturn::getPrincipal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal sumInterest = allLendItemReturn.stream()
                    .filter(item -> item.getLendReturnId().longValue() == lendReturn.getId().longValue())
                    .map(LendItemReturn::getInterest)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal sumTotal = allLendItemReturn.stream()
                    .filter(item -> item.getLendReturnId().longValue() == lendReturn.getId().longValue())
                    .map(LendItemReturn::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            //将计算出的数据填充入还款计划记录：设置本金、利息、总金额
            lendReturn.setPrincipal(sumPrincipal);
            lendReturn.setInterest(sumInterest);
            lendReturn.setTotal(sumTotal);
        });
        //批量更新还款计划列表
        lendReturnService.updateBatchById(lendReturnList);
    }

    /**
     * 回款计划
     * @return
     * @param lendReturnList
     * @param lend
     * @param lendItem
     */
    public List<LendItemReturn> returnPlan(List<LendReturn> lendReturnList, Lend lend, LendItem lendItem){
        BigDecimal amount = lendItem.getInvestAmount();
        BigDecimal lendYearRate = lendItem.getLendYearRate();
        Integer totalMouths = lend.getPeriod();
        Integer returnMethod = lend.getReturnMethod();

        Map<String, Map<Integer, BigDecimal>> stringMapMap = computeAmount(amount, lendYearRate, totalMouths, returnMethod);
        //所有期数的本息
        Map<Integer, BigDecimal> invest = stringMapMap.get("invest");
        //所有期数的本金
        Map<Integer, BigDecimal> principal = stringMapMap.get("principal");

        List<LendItemReturn> lendItemReturns = new ArrayList<>();
        lendReturnList.forEach(lendReturn -> {
            LendItemReturn lendItemReturn = new LendItemReturn();
            lendItemReturn.setLendReturnId(lendReturn.getId());
            lendItemReturn.setLendItemId(lendItem.getId());
            lendItemReturn.setLendId(lend.getId());
            lendItemReturn.setInvestUserId(lendItem.getInvestUserId());
            lendItemReturn.setInvestAmount(lendItem.getInvestAmount());
            lendItemReturn.setCurrentPeriod(lendReturn.getCurrentPeriod());
            lendItemReturn.setLendYearRate(lend.getLendYearRate());
            lendItemReturn.setReturnMethod(lend.getReturnMethod());

            //最后一期
            if (lendReturn.getLast()){
                //一次还本还息
                if (lendReturn.getReturnMethod().intValue() != ReturnMethodEnum.FOUR.getMethod()){
                    //一次还本还息
                    lendItemReturn.setPrincipal(principal.get(1));
                    //本息
                    lendItemReturn.setInterest(invest.get(1));
                }else{
                    //非一次还本还息
                    //本金
                    BigDecimal sumPrincipal = lendItemReturns.stream()
                            .map(LendItemReturn::getPrincipal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal lastPrincipal = lendItem.getInvestAmount().subtract(sumPrincipal);
                    lendItemReturn.setPrincipal(lastPrincipal);
                    //本息
                    BigDecimal sumInterest = lendItemReturns.stream()
                            .map(LendItemReturn::getInterest)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal lastInterest = lendItem.getExpectAmount().subtract(sumInterest);
                    lendItemReturn.setInterest(lastInterest);
                }
            }else{
                //非一次还本还息
                if (lendReturn.getReturnMethod().intValue() != ReturnMethodEnum.FOUR.getMethod()){
                    //本金
                    lendItemReturn.setPrincipal(principal.get(lendReturn.getCurrentPeriod()));
                    //本息
                    lendItemReturn.setInterest(invest.get(lendReturn.getCurrentPeriod()));
                }
            }

            //总和
            lendItemReturn.setTotal(lendItemReturn.getPrincipal().add(lendItemReturn.getInterest()));

            lendItemReturn.setFee(new BigDecimal(0));
            lendItemReturn.setReturnDate(lendReturn.getReturnDate());
            lendItemReturn.setOverdue(false);
            lendItemReturn.setStatus(0);//0 未归还 1已归还

            lendItemReturns.add(lendItemReturn);
        });
        lendItemReturnService.saveBatch(lendItemReturns);
        return lendItemReturns;
    }

    /**
     * 计算回款本息和本金
     * @param amount
     * @param lendYearRate
     * @param totalMouths
     * @param returnMethod
     * @return
     */
    public Map<String, Map<Integer, BigDecimal>> computeAmount(BigDecimal amount, BigDecimal lendYearRate, Integer totalMouths, Integer returnMethod){
        Map<String, Map<Integer, BigDecimal>> stringMapHashMap = new HashMap<>();
        if (returnMethod.intValue() == ReturnMethodEnum.ONE.getMethod()){
            stringMapHashMap.put("invest", Amount1Helper.getPerMonthInterest(amount,lendYearRate,totalMouths));
            stringMapHashMap.put("principal", Amount1Helper.getPerMonthPrincipal(amount, lendYearRate, totalMouths));
        }else if (returnMethod.intValue() == ReturnMethodEnum.TWO.getMethod()){
            stringMapHashMap.put("invest", Amount2Helper.getPerMonthInterest(amount,lendYearRate,totalMouths));
            stringMapHashMap.put("principal", Amount2Helper.getPerMonthPrincipal(amount, lendYearRate, totalMouths));
        }else if (returnMethod.intValue() == ReturnMethodEnum.THREE.getMethod()){
            stringMapHashMap.put("invest", Amount3Helper.getPerMonthInterest(amount,lendYearRate,totalMouths));
            stringMapHashMap.put("principal", Amount3Helper.getPerMonthPrincipal(amount, lendYearRate, totalMouths));
        }else {
            stringMapHashMap.put("invest", Amount4Helper.getPerMonthInterest(amount,lendYearRate,totalMouths));
            stringMapHashMap.put("principal", Amount4Helper.getPerMonthPrincipal(amount, lendYearRate, totalMouths));
        }
        return stringMapHashMap;
    }

}
