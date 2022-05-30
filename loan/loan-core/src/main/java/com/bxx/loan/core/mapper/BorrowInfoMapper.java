package com.bxx.loan.core.mapper;

import com.bxx.loan.core.pojo.entity.BorrowInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 借款信息表 Mapper 接口
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface BorrowInfoMapper extends BaseMapper<BorrowInfo> {

    List<BorrowInfo> listJoin();

    void deleteByUserId(@Param("userId") Long userId);
}
