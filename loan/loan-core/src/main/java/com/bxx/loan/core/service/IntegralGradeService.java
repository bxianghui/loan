package com.bxx.loan.core.service;

import com.bxx.loan.core.pojo.entity.IntegralGrade;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 积分等级表 服务类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface IntegralGradeService extends IService<IntegralGrade> {

    BigDecimal getGrade(Integer integral);
}
