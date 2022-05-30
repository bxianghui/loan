package com.bxx.loan.core.service;

import com.bxx.loan.core.pojo.entity.TransFlow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bxx.loan.core.pojo.vo.TransFlowVO;

import java.util.List;

/**
 * <p>
 * 交易流水表 服务类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface TransFlowService extends IService<TransFlow> {

    boolean isSaveTransFlow(String agentBillNo);

    void saveTransFlow(TransFlowVO transFlowVO);

    List<TransFlow> list(Long userId);
}
