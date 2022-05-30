package com.bxx.loan.core.service;

import com.bxx.loan.core.pojo.entity.UserLoginRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface UserLoginRecordService extends IService<UserLoginRecord> {

    List<UserLoginRecord> getListTop50(Long userId);
}
