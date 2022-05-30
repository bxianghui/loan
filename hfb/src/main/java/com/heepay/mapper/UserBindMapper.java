package com.heepay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heepay.model.UserBind;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBindMapper extends BaseMapper<UserBind> {

    void updateStatus(@Param("id") Integer id);

}
