package com.bxx.loan.core.service;

import com.bxx.loan.core.pojo.entity.BorrowInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bxx.loan.core.pojo.vo.BorrowInfoApprovalVO;
import com.bxx.loan.core.pojo.vo.BorrowInfoVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 服务类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface BorrowInfoService extends IService<BorrowInfo> {

    List<BorrowInfo> listJoin();

    Map<String, Object> getDetailById(Long id);

    void approval(BorrowInfoApprovalVO borrowInfoApprovalVO);

    Integer getBorrowInfoStatus(Long userId);

    BigDecimal getBorrowAmount(Long userId);

    void save(BorrowInfoVO borrowInfoVO, Long userId);
}
