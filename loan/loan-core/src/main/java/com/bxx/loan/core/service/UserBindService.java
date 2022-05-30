package com.bxx.loan.core.service;

import com.bxx.loan.core.pojo.entity.UserBind;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bxx.loan.core.pojo.vo.UserBindVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
public interface UserBindService extends IService<UserBind> {

    String bindHfb(UserBindVO userBindVO, Long userId);

    void notifyUserBind(Map<String, Object> param);

    String getBindCodeById(Long userId);

    Long getIdByBindCode(String bindCode);
}
