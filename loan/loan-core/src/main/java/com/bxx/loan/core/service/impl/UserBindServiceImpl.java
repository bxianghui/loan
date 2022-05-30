package com.bxx.loan.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bxx.loan.base.util.JWTUtils;
import com.bxx.loan.common.exception.Assert;
import com.bxx.loan.common.result.ResponseEnum;
import com.bxx.loan.core.enums.UserBindEnums;
import com.bxx.loan.core.hfb.FormHelper;
import com.bxx.loan.core.hfb.HfbConst;
import com.bxx.loan.core.hfb.RequestHelper;
import com.bxx.loan.core.mapper.UserInfoMapper;
import com.bxx.loan.core.pojo.entity.UserBind;
import com.bxx.loan.core.mapper.UserBindMapper;
import com.bxx.loan.core.pojo.entity.UserInfo;
import com.bxx.loan.core.pojo.vo.UserBindVO;
import com.bxx.loan.core.service.UserBindService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bxx.loan.core.service.UserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务实现类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Service
public class UserBindServiceImpl extends ServiceImpl<UserBindMapper, UserBind> implements UserBindService {

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 通过form表单发送请求到hfb
     * @param userBindVO
     * @param userId
     * @return
     */
    @Override
    public String bindHfb(UserBindVO userBindVO, Long userId) {
        //不同的user_id, 相同的身份证，如果存在，则不允许
        QueryWrapper<UserBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id_card", userBindVO.getIdCard())
                .ne("user_id", userId);
        UserBind userBind = baseMapper.selectOne(queryWrapper);
        Assert.isNull(userBind, ResponseEnum.USER_BIND_IDCARD_EXIST_ERROR);

        queryWrapper.clear();;
        queryWrapper.eq("user_id", userId);
        userBind = baseMapper.selectOne(queryWrapper);
        if (userBind == null){
            //创建用户记录
            userBind = new UserBind();
            BeanUtils.copyProperties(userBindVO, userBind);
            userBind.setUserId(userId);
            userBind.setStatus(UserBindEnums.NO_BIND.getStatus());
            baseMapper.insert(userBind);
        }else{
            //更新用户记录
            BeanUtils.copyProperties(userBindVO, userBind);
            baseMapper.updateById(userBind);
        }

        //组装自动提交表单的参数
        Map<String, Object> param = new HashMap<>();
        param.put("agentId", HfbConst.AGENT_ID);
        param.put("agentUserId", userId);
        param.put("personalName", userBindVO.getName());
        param.put("mobile", userBindVO.getMobile());
        param.put("idCard", userBindVO.getIdCard());
        param.put("bankNo", userBindVO.getBankNo());
        param.put("bankType", userBindVO.getBankType());
        param.put("returnUrl", HfbConst.USERBIND_RETURN_URL);
        param.put("notifyUrl", HfbConst.USERBIND_NOTIFY_URL);
        param.put("timestamp", RequestHelper.getTimestamp());
        param.put("sign", RequestHelper.getSign(param));

        return FormHelper.buildForm(HfbConst.USERBIND_URL, param);
    }


    /**
     * 用于异步接收hfb的更新状态操作
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void notifyUserBind(Map<String, Object> param) {
        String bindCode = (String)param.get("bindCode");
        String agentUserId = (String)param.get("agentUserId");

        //bindCode幂等性判断
        QueryWrapper<UserBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bind_code", bindCode);
        UserBind userBind = baseMapper.selectOne(queryWrapper);
        if (userBind != null){
            log.warn("已经完成绑定");
            return;
        }

        //不存在幂等性操作
        queryWrapper.clear();
        queryWrapper.eq("user_id", agentUserId);
        userBind = baseMapper.selectOne(queryWrapper);
        userBind.setBindCode(bindCode);
        userBind.setStatus(UserBindEnums.BIND_OK.getStatus());
        baseMapper.updateById(userBind);

        UserInfo userInfo = userInfoMapper.selectById(agentUserId);
        userInfo.setName(userBind.getName());
        userInfo.setIdCard(userBind.getIdCard());
        userInfo.setBindCode(bindCode);
        userInfo.setBindStatus(UserBindEnums.BIND_OK.getStatus());
        userInfoMapper.updateById(userInfo);
    }

    /**
     * 获取账户的绑定号
     * @param userId
     * @return
     */
    @Override
    public String getBindCodeById(Long userId) {
        UserBind userBind = baseMapper.selectOne(new QueryWrapper<UserBind>().eq("user_id", userId));
        return userBind.getBindCode();
    }

    public Long getIdByBindCode(String bindCode){
        UserBind userBind = baseMapper.selectOne(new QueryWrapper<UserBind>().eq("bind_code", bindCode));
        return userBind.getUserId();
    }
}
