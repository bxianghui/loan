package com.bxx.loan.core.service;

import com.bxx.loan.core.pojo.entity.LendItem;
import com.bxx.loan.core.pojo.entity.LendReturn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 还款记录表 服务类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface LendReturnService extends IService<LendReturn> {

    List<LendReturn> getListById(Long lendId);

    List<LendReturn> getListByUserId(Long userId);

    String commitReturn(Long lendReturnId);

    void notify(Map<String, Object> paramMap);
}
