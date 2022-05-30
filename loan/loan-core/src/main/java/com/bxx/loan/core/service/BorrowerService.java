package com.bxx.loan.core.service;

import com.bxx.loan.core.pojo.entity.Borrower;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bxx.loan.core.pojo.vo.BorrowerApprovalVO;
import com.bxx.loan.core.pojo.vo.BorrowerDetailVO;
import com.bxx.loan.core.pojo.vo.BorrowerVO;

import java.util.Map;

/**
 * <p>
 * 借款人 服务类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface BorrowerService extends IService<Borrower> {


    Map<String, Object> pageSelect(Long page, Long size, String keyWords);

    Borrower getBorrower(Long userId);

    Integer getBorrowerStatus(Long userId);

    void save(BorrowerVO borrowerVO, Long userId);

    BorrowerDetailVO getBorrowerDetailVOById(Long id);

    void approval(BorrowerApprovalVO borrowerApprovalVO);

    BorrowerDetailVO getBorrowDetail(Long userId);

}
