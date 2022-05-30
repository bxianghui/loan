package com.bxx.loan.core.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bxx.loan.core.pojo.entity.Borrower;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 借款人 Mapper 接口
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface BorrowerMapper extends BaseMapper<Borrower> {

    void deleteByUserId(@Param("userId") Long userId);
}
