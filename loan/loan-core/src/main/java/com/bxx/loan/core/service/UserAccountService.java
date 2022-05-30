package com.bxx.loan.core.service;

import com.bxx.loan.core.pojo.entity.UserAccount;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 服务类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface UserAccountService extends IService<UserAccount> {

    String commitCharge(BigDecimal chargeAmt, Long userId);

    void notifyCharge(Map<String, Object> param);

    String commitWithdraw(BigDecimal fetchAmt, Long userId);

    void notifyWithDraw(Map<String, Object> param);

    BigDecimal getAccount(Long userId);

    void updateAccount(String voteBindCode, BigDecimal fVoteAmt, BigDecimal voteAmt);
}
