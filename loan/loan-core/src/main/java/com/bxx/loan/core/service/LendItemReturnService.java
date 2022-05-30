package com.bxx.loan.core.service;

import com.bxx.loan.core.pojo.entity.LendItemReturn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借回款记录表 服务类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface LendItemReturnService extends IService<LendItemReturn> {

    List<LendItemReturn> listById(Long userId, Long lendId);

    List<LendItemReturn> listByUserId(Long userId);

    List<Map<String, Object>> addReturnDetail(Long lendReturnId);

    List<LendItemReturn> selectLendItemReturnList(Long id);
}
