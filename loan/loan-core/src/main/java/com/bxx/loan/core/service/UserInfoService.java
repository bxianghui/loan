package com.bxx.loan.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.pojo.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bxx.loan.core.pojo.query.UserInfoQuery;
import com.bxx.loan.core.pojo.vo.RegisterVO;
import com.bxx.loan.core.pojo.vo.UserIndexVO;
import com.bxx.loan.core.pojo.vo.UserInfoVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface UserInfoService extends IService<UserInfo> {

    Page<UserInfo> pageSelect(Page<UserInfo> page, UserInfoQuery userInfoQuery);

    boolean lock(Long id, Integer status);

    boolean getByMobile(String mobile);

    Result register(RegisterVO registerVO);

    Result login(UserInfo userInfo, String ip);

    void resetPassword(Long id);

    UserIndexVO getIndexUserInfo(HttpServletRequest request);

    void checkToken(HttpServletRequest request);
}
