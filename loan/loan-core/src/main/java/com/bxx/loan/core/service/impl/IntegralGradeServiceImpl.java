package com.bxx.loan.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bxx.loan.core.pojo.entity.IntegralGrade;
import com.bxx.loan.core.mapper.IntegralGradeMapper;
import com.bxx.loan.core.service.IntegralGradeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 * 积分等级表 服务实现类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Service
public class IntegralGradeServiceImpl extends ServiceImpl<IntegralGradeMapper, IntegralGrade> implements IntegralGradeService {

    @Override
    public BigDecimal getGrade(Integer integral) {
        QueryWrapper<IntegralGrade> integralGradeQueryWrapper = new QueryWrapper<>();
        integralGradeQueryWrapper = integralGradeQueryWrapper.select("borrow_amount").le("integral_start", integral).ge("integral_end", integral);
        List<Object> objects = baseMapper.selectObjs(integralGradeQueryWrapper);
        BigDecimal amount = (BigDecimal) objects.get(0);
        return amount;
    }
}
