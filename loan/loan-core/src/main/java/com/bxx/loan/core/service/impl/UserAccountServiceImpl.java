package com.bxx.loan.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bxx.loan.core.enums.TransTypeEnum;
import com.bxx.loan.core.hfb.FormHelper;
import com.bxx.loan.core.hfb.HfbConst;
import com.bxx.loan.core.hfb.RequestHelper;
import com.bxx.loan.core.mapper.UserInfoMapper;
import com.bxx.loan.core.pojo.entity.TransFlow;
import com.bxx.loan.core.pojo.entity.UserAccount;
import com.bxx.loan.core.mapper.UserAccountMapper;
import com.bxx.loan.core.pojo.entity.UserBind;
import com.bxx.loan.core.pojo.entity.UserInfo;
import com.bxx.loan.core.pojo.vo.TransFlowVO;
import com.bxx.loan.core.service.TransFlowService;
import com.bxx.loan.core.service.UserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bxx.loan.core.service.UserBindService;
import com.bxx.loan.core.util.LendNoUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {


    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private TransFlowService transFlowService;

    @Resource
    private UserBindService userBindService;

    /**
     * 通过form表单发送请求到hfb  充值
     * @param chargeAmt
     * @param userId
     * @return
     */
    @Override
    public String commitCharge(BigDecimal chargeAmt, Long userId) {

        UserInfo userInfo = userInfoMapper.selectById(userId);
        String bindCode = userInfo.getBindCode();

        Map<String, Object> param = new HashMap<>();
        param.put("agentId", HfbConst.AGENT_ID);
        param.put("agentBillNo", LendNoUtils.getChargeNo());
        param.put("bindCode", bindCode);
        param.put("chargeAmt", chargeAmt);
        param.put("feeAmt", new BigDecimal("0"));//手续费
        param.put("notifyUrl", HfbConst.RECHARGE_NOTIFY_URL);
        param.put("returnUrl", HfbConst.RECHARGE_RETURN_URL);
        param.put("timestamp", RequestHelper.getTimestamp());
        param.put("sign", RequestHelper.getSign(param));

        String formStr = FormHelper.buildForm(HfbConst.RECHARGE_URL, param);
        return formStr;
    }

    /**
     * hfb发送充值结果更新请求到尚融宝
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void notifyCharge(Map<String, Object> param) {
        String agentBillNo = (String)param.get("agentBillNo");
        //判断流水号是否已经存在
        boolean isSave = transFlowService.isSaveTransFlow(agentBillNo);
        //幂等性判断
        if (isSave){
            log.warn("已经存在该流水号");
            return;
        }

        //账户处理
        String bindCode = (String)param.get("bindCode");
        String chargeAmt = (String)param.get("chargeAmt");
        baseMapper.updateAccount(bindCode, new BigDecimal(chargeAmt), new BigDecimal(0));


        //记录流水信息
        TransFlowVO transFlowVO = new TransFlowVO(
                agentBillNo,
                bindCode,
                new BigDecimal(chargeAmt),
                TransTypeEnum.RECHARGE,
                "充值" + chargeAmt + "元"
        );

        transFlowService.saveTransFlow(transFlowVO);

    }

    /**
     * 通过form表单发送请求到hfb  提现
     * @param fetchAmt
     * @param userId
     * @return
     */
    @Override
    public String commitWithdraw(BigDecimal fetchAmt, Long userId) {

        UserInfo userInfo = userInfoMapper.selectById(userId);
        String bindCode = userInfo.getBindCode();
        //判断体现数额是否大于当前金额
        boolean isBig = compareAmount(fetchAmt, userId);
        if (!isBig) {
            return "fail";
        }
        Map<String, Object> param = new HashMap<>();
        param.put("agentId", HfbConst.AGENT_ID);
        param.put("agentBillNo", LendNoUtils.getChargeNo());
        param.put("bindCode", bindCode);
        param.put("fetchAmt", fetchAmt);
        param.put("feeAmt", new BigDecimal("0"));//手续费
        param.put("notifyUrl", HfbConst.WITHDRAW_NOTIFY_URL);
        param.put("returnUrl", HfbConst.WITHDRAW_RETURN_URL);
        param.put("timestamp", RequestHelper.getTimestamp());
        param.put("sign", RequestHelper.getSign(param));

        String formStr = FormHelper.buildForm(HfbConst.WITHDRAW_URL, param);
        return formStr;
    }

    private boolean compareAmount(BigDecimal fetchAmt, Long userId) {
        QueryWrapper<UserAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        UserAccount userAccount = baseMapper.selectOne(queryWrapper);
        BigDecimal amount = userAccount.getAmount();
        if (amount.compareTo(fetchAmt) < 0){
            return false;
        }
        return true;
    }

    /**
     * hfb发送提现结果更新请求到尚融宝
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void notifyWithDraw(Map<String, Object> param) {
        String agentBillNo = (String)param.get("agentBillNo");

        //判断流水号是否已经存在
        boolean isSave = transFlowService.isSaveTransFlow(agentBillNo);
        //幂等性判断
        if (isSave){
            log.warn("已经存在该流水号");
            return;
        }

        //账户处理
        String bindCode = (String)param.get("bindCode");
        String fetchAmt = (String)param.get("fetchAmt");
        baseMapper.updateAccount(bindCode, new BigDecimal("-" + fetchAmt), new BigDecimal(0));

        //记录流水信息
        TransFlowVO transFlowVO = new TransFlowVO(
                agentBillNo,
                bindCode,
                new BigDecimal(fetchAmt),
                TransTypeEnum.WITHDRAW,
                "提现" + fetchAmt + "元"
        );

        transFlowService.saveTransFlow(transFlowVO);
    }

    /**
     * 获取账户余额
     * @param userId
     * @return
     */
    @Override
    public BigDecimal getAccount(Long userId) {
        UserAccount userAccount = baseMapper.selectOne(new QueryWrapper<UserAccount>().eq("user_id", userId));
        return userAccount.getAmount();
    }

    /**
     * 修改账户余额
     * @param voteBindCode
     * @param fVoteAmt
     * @param voteAmt
     */
    @Override
    public void updateAccount(String voteBindCode, BigDecimal fVoteAmt, BigDecimal voteAmt) {
       baseMapper.updateAccount(voteBindCode, fVoteAmt, voteAmt);
    }
}
