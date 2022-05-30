package com.bxx.loan.core.mapper;

import com.bxx.loan.core.pojo.entity.UserLoginRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 用户登录记录表 Mapper 接口
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface UserLoginRecordMapper extends BaseMapper<UserLoginRecord> {

    UserLoginRecord selectByIdOrder(@Param("userId") Long userId);

    List<UserLoginRecord> getListTop50(@Param("userId") Long userId);
}
