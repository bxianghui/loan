package com.bxx.loan.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bxx.loan.common.exception.Assert;
import com.bxx.loan.common.result.ResponseEnum;
import com.bxx.loan.core.enums.LendStatusEnum;
import com.bxx.loan.core.enums.TransTypeEnum;
import com.bxx.loan.core.hfb.FormHelper;
import com.bxx.loan.core.hfb.HfbConst;
import com.bxx.loan.core.hfb.RequestHelper;
import com.bxx.loan.core.mapper.LendMapper;
import com.bxx.loan.core.pojo.entity.Lend;
import com.bxx.loan.core.pojo.entity.LendItem;
import com.bxx.loan.core.mapper.LendItemMapper;
import com.bxx.loan.core.pojo.vo.TransFlowVO;
import com.bxx.loan.core.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bxx.loan.core.util.LendNoUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 服务实现类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Service
public class LendItemServiceImpl extends ServiceImpl<LendItemMapper, LendItem> implements LendItemService {


    @Resource
    private LendMapper lendMapper;

    @Resource
    private UserAccountService userAccountService;

    @Resource
    private LendService lendService;

    @Resource
    private UserBindService userBindService;

    @Resource
    private TransFlowService transFlowService;
    /**
     * 根据lendId获取投资记录
     * @param lendId
     * @return
     */
    @Override
    public List<LendItem> getListById(Long lendId) {
        QueryWrapper<LendItem> lendItemQueryWrapper = new QueryWrapper<>();
        lendItemQueryWrapper.eq("lend_id", lendId);
        return baseMapper.selectList(lendItemQueryWrapper);
    }

    /**
     * 获取所有的投资列表
     * @param userId
     * @return
     */
    @Override
    public List<LendItem> getListByUserId(Long userId) {
        List<LendItem> itemList = baseMapper.selectList(new QueryWrapper<LendItem>().eq("invest_user_id", userId));
        return itemList;
    }

    /**
     * 发送 投资请求到hfb
     * @param lendId
     * @param investAmount
     * @param userId
     * @param userName
     * @return
     */
    @Override
    public String commitInvest(Long lendId, BigDecimal investAmount, Long userId, String userName) {
        Lend lend = lendMapper.selectById(lendId);
        Assert.isTrue(
                lend.getStatus().intValue() == LendStatusEnum.INVEST_RUN.getStatus().intValue(),
                ResponseEnum.LEND_INVEST_ERROR
        );

        //超卖： 已投 + 当前投资金额 <= 标的金额（正常）
        BigDecimal sum = lend.getInvestAmount().add(investAmount);
        Assert.isTrue(
                sum.doubleValue() <= lend.getAmount().doubleValue(),
                ResponseEnum.LEND_FULL_SCALE_ERROR
        );

        //用户余额：当前用户的余额 >= 当前投资金额
        BigDecimal amount = userAccountService.getAccount(userId);
        Assert.isTrue(
                amount.doubleValue() >= investAmount.doubleValue(),
                ResponseEnum.NOT_SUFFICIENT_FUNDS_ERROR
        );

        LendItem lendItem = new LendItem();
        lendItem.setInvestUserId(userId);
        lendItem.setInvestName(userName);
        String lendItemNo = LendNoUtils.getLendItemNo();
        lendItem.setLendItemNo(lendItemNo);
        lendItem.setInvestAmount(investAmount);
        lendItem.setLendYearRate(lend.getLendYearRate());
        lendItem.setLendId(lend.getId());
        lendItem.setInvestTime(LocalDateTime.now());
        lendItem.setLendStartDate(lend.getLendStartDate());
        lendItem.setLendEndDate(lend.getLendEndDate());
        //预期收益
        BigDecimal exceptAmount = lendService.getInterestCount(lendItem.getInvestAmount(),
                lendItem.getLendYearRate(), lend.getPeriod(), lend.getReturnMethod());
        lendItem.setExpectAmount(exceptAmount);
        //真实收益 当前为0
        lendItem.setRealAmount(new BigDecimal(0));

        //投资记录状态
        lendItem.setStatus(0); //首次创建，账户信息尚未发生改变
        baseMapper.insert(lendItem);

        //获取投资人的bindCode
        String bindCode = userBindService.getBindCodeById(userId);
        //获取借款人的bindCode
        String benefitBindCode = userBindService.getBindCodeById(lend.getUserId());
        //封装提交至汇付宝的参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("voteBindCode", bindCode);
        paramMap.put("benefitBindCode",benefitBindCode);
        paramMap.put("agentProjectCode", lend.getLendNo());//项目标号
        paramMap.put("agentProjectName", lend.getTitle());
        //在资金托管平台上的投资订单的唯一编号，可以独立生成，不一定非要和lendItemNo保持一致，但是可以一致。
        //投资流水

        paramMap.put("agentBillNo", lendItemNo);
        paramMap.put("voteAmt", investAmount);
        paramMap.put("votePrizeAmt", "0");
        paramMap.put("voteFeeAmt", "0");
        paramMap.put("projectAmt", lend.getAmount()); //标的总金额
        paramMap.put("note", "");
        paramMap.put("notifyUrl", HfbConst.INVEST_NOTIFY_URL); //检查常量是否正确
        paramMap.put("returnUrl", HfbConst.INVEST_RETURN_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);

        //获取投资自动提交表单
        String form = FormHelper.buildForm(HfbConst.INVEST_URL, paramMap);
        return form;
    }

    /**
     * hfb回调接口实现
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void notify(Map<String, Object> param) {
        String agentBillNo = (String)param.get("agentBillNo");
        boolean result = transFlowService.isSaveTransFlow(agentBillNo);
        if(result){
            log.warn("幂等性返回");
            return;
        }

        //修改账户金额：从余额中减去投资金额，在冻结金额中增加投资进入
        String voteBindCode = (String)param.get("voteBindCode");
        String voteAmt = (String)param.get("voteAmt");

        userAccountService.updateAccount(
                voteBindCode,
                new BigDecimal("-" + voteAmt),
                new BigDecimal(voteAmt)
        );

        //修改投资记录的状态标记
        LendItem lendItem = getByLendItemNO(agentBillNo);
        lendItem.setStatus(1);
        baseMapper.updateById(lendItem);

        //修改标的的投资人数 已投金额 和当前状态
        Long lendId = lendItem.getLendId();
        Lend lend = lendService.getById(lendId);
        lend.setInvestAmount(lend.getInvestAmount().add(lendItem.getInvestAmount()));
//        if (lend.getInvestAmount().doubleValue() == lend.getAmount().doubleValue()){
//            lend.setStatus(LendStatusEnum.PAY_RUN.getStatus());
//        }
        lend.setInvestNum(lend.getInvestNum() + 1);
        lendMapper.updateById(lend);

        //记录账户流水
        TransFlowVO transFlowBO = new TransFlowVO(
                agentBillNo,
                voteBindCode,
                new BigDecimal(voteAmt),
                TransTypeEnum.INVEST_LOCK,
                "投资  项目编号：" + lend.getLendNo() + "，项目名称" + lend.getTitle());
        transFlowService.saveTransFlow(transFlowBO);
    }

    private LendItem getByLendItemNO(String lendItemNO) {
        LendItem lendItem = baseMapper.selectOne(new QueryWrapper<LendItem>().eq("lend_item_no", lendItemNO));
        return lendItem;
    }


}
