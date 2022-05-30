package com.bxx.loan.core.service;

import com.bxx.loan.core.pojo.entity.BorrowInfo;
import com.bxx.loan.core.pojo.entity.Lend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bxx.loan.core.pojo.vo.BorrowInfoApprovalVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface LendService extends IService<Lend> {

    List<Lend> getList();

    void createLend(BorrowInfo borrowInfo, BorrowInfoApprovalVO borrowInfoApprovalVO);

    Map<String, Object> show(Long id);

    BigDecimal getInterestCount(BigDecimal investAmount, BigDecimal lendYearRate, Integer period, Integer returnMethod);

    void makeLoan(Long id);

    List<Lend> getListByUserId(Long userId);

}
