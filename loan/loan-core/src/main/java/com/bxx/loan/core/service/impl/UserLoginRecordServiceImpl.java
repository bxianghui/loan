package com.bxx.loan.core.service.impl;

import com.bxx.loan.core.pojo.entity.UserLoginRecord;
import com.bxx.loan.core.mapper.UserLoginRecordMapper;
import com.bxx.loan.core.service.UserLoginRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务实现类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord> implements UserLoginRecordService {


    @Override
    public List<UserLoginRecord> getListTop50(Long userId) {
        List<UserLoginRecord> records = baseMapper.getListTop50(userId);
        return records;
    }
}
