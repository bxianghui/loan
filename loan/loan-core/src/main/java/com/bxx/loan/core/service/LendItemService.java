package com.bxx.loan.core.service;

import com.bxx.loan.core.pojo.entity.LendItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 服务类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface LendItemService extends IService<LendItem> {

    List<LendItem> getListById(Long lendId);

    List<LendItem> getListByUserId(Long userId);

    String commitInvest(Long lendId, BigDecimal investAmount, Long userId, String userName);

    void notify(Map<String, Object> param);
}
