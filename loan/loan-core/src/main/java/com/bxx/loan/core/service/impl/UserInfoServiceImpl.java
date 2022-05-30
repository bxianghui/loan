package com.bxx.loan.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bxx.loan.base.util.JWTUtils;
import com.bxx.loan.common.exception.Assert;
import com.bxx.loan.common.result.ResponseEnum;
import com.bxx.loan.common.result.Result;
import com.bxx.loan.core.enums.UserBindEnums;
import com.bxx.loan.core.mapper.UserAccountMapper;
import com.bxx.loan.core.mapper.UserLoginRecordMapper;
import com.bxx.loan.core.pojo.entity.UserAccount;
import com.bxx.loan.core.pojo.entity.UserInfo;
import com.bxx.loan.core.mapper.UserInfoMapper;
import com.bxx.loan.core.pojo.entity.UserLoginRecord;
import com.bxx.loan.core.pojo.query.UserInfoQuery;
import com.bxx.loan.core.pojo.vo.RegisterVO;
import com.bxx.loan.core.pojo.vo.UserIndexVO;
import com.bxx.loan.core.pojo.vo.UserInfoVo;
import com.bxx.loan.core.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bxx.loan.core.util.MD5Util;
import com.bxx.loan.core.util.SHAUtil;
import com.bxx.loan.core.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserAccountMapper userAccountMapper;

    @Resource
    private UserLoginRecordMapper loginRecordMapper;

    /**
     * 会员列表信息， 通过查询条件分页查询
     * @param page
     * @param userInfoQuery
     * @return
     */
    @Override
    public Page<UserInfo> pageSelect(Page<UserInfo> page, UserInfoQuery userInfoQuery) {

        QueryWrapper<UserInfo> queryWrapper = null;

        if (userInfoQuery == null){
            return baseMapper.selectPage(page, null);
        }

        queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(!StringUtils.isBlank(userInfoQuery.getMobile()), "mobile", userInfoQuery.getMobile())
                    .eq(userInfoQuery.getUserType()!=null, "user_type", userInfoQuery.getUserType())
                    .eq(userInfoQuery.getStatus()!=null,"status", userInfoQuery.getStatus());

        return baseMapper.selectPage(page, queryWrapper);
    }

    /**
     * 锁定当前账号
     * @param id
     * @param status
     * @return
     */
    @Override
    public boolean lock(Long id, Integer status) {
        UserInfo userInfo = new UserInfo();

        userInfo.setId(id);

        userInfo.setStatus(status);

        return this.updateById(userInfo);
    }

    /**
     * 负责远程调用的方法  注册时查看是否该账号已经注册
     * @param mobile
     * @return
     */
    @Override
    public boolean getByMobile(String mobile) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("mobile", mobile);

        Integer count = baseMapper.selectCount(queryWrapper);

        if (count <= 0){
            return true;
        }

        return false;
    }

    /**
     * 注册账号
     * @param registerVO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result register(RegisterVO registerVO) {

        String code = registerVO.getCode();

        Assert.notNull(code, ResponseEnum.CODE_NULL_ERROR);

        validCode(registerVO);

        UserInfo userInfo = new UserInfo();

        userInfo.setUserType(registerVO.getUserType());

        userInfo.setNickName(registerVO.getMobile());

        userInfo.setName(registerVO.getMobile());

        userInfo.setMobile(registerVO.getMobile());

        userInfo.setPassword(MD5Util.encryptMD5(SHAUtil.encrypt(registerVO.getPassword())));

        userInfo.setStatus(Utils.STATUS_NORMAL);

        userInfo.setHeadImg(Utils.USER_AVATAR);

        baseMapper.insert(userInfo);

        //插入用户账户记录：user_account
        UserAccount userAccount = new UserAccount();

        userAccount.setUserId(userInfo.getId());

        userAccountMapper.insert(userAccount);


        return Result.success();
    }

    /**
     * 验证码诊断
     * @param registerVO
     * @return
     */
    public void validCode(RegisterVO registerVO) {
        String str = (String) redisTemplate.opsForValue().get("valid:code:" + registerVO.getMobile());

        Assert.notNull(str, ResponseEnum.CODE_TIMEOUT_ERROR);

        Assert.equals(registerVO.getCode(), str, ResponseEnum.CODE_ERROR);
    }

    /**
     * 登录
     * @param userInfo
     * @param ip
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result login(UserInfo userInfo, String ip) {
        QueryWrapper<UserInfo> queryWrapper  = new QueryWrapper<>();

        queryWrapper
                .eq("mobile", userInfo.getMobile())
                .eq("user_type", userInfo.getUserType());

        UserInfo user = baseMapper.selectOne(queryWrapper);
        //帐号不存在
        Assert.notNull(user, ResponseEnum.LOGIN_MOBILE_ERROR);

        String password = userInfo.getPassword();

        password = MD5Util.encryptMD5(SHAUtil.encrypt(password));

        //密码错误
        Assert.equals(password, user.getPassword(),ResponseEnum.LOGIN_PASSWORD_ERROR);

        //账户已被锁定
        Assert.notEquals(user.getStatus(),0,ResponseEnum.LOGIN_LOKED_ERROR);

        //登陆成功
        //设置登录日志
        UserLoginRecord userLoginRecord = new UserLoginRecord();

        userLoginRecord.setIp(ip);

        userLoginRecord.setUserId(user.getId());

        loginRecordMapper.insert(userLoginRecord);
        //生成cookie对象
        UserInfoVo userInfoVo = new UserInfoVo();

        BeanUtils.copyProperties(user, userInfoVo);

        userInfoVo.setToken(JWTUtils.createToken(user.getId(), user.getName()));

        return Result.success().data(userInfoVo);
    }

    /**
     * 密码重置
     * @param id
     */
    @Override
    public void resetPassword(Long id) {
        UserInfo userInfo = new UserInfo();

        userInfo.setPassword(MD5Util.encryptMD5(SHAUtil.encrypt("123456")));

        baseMapper.update(userInfo, new QueryWrapper<UserInfo>().eq("id", id));
    }

    /**
     * 通过token的id获取用户信息
     * @param request
     * @return
     */
    @Override
    public UserIndexVO getIndexUserInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        //getUserId 已经判断是否存在
        Long userId = JWTUtils.getUserId(token);
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        if (userInfo == null){
            return null;
        }
        UserIndexVO userIndexVO = new UserIndexVO();
        UserLoginRecord userLoginRecord = loginRecordMapper.selectByIdOrder(userId);
        BeanUtils.copyProperties(userInfo, userIndexVO);
        userIndexVO.setUserId(userId);
        userIndexVO.setLastLoginTime(userLoginRecord.getCreateTime());
        if (userIndexVO.getBindStatus().equals(UserBindEnums.BIND_OK.getStatus())){
            QueryWrapper<UserAccount> userAccountQueryWrapper = new QueryWrapper<>();
            userAccountQueryWrapper.eq("user_id", userId);
            UserAccount userAccount = userAccountMapper.selectOne(userAccountQueryWrapper);
            userIndexVO.setAmount(userAccount.getAmount());
            userIndexVO.setFreezeAmount(userAccount.getFreezeAmount());
        }
        return userIndexVO;
    }

    /**
     * 判断用户是否已登陆
     * @param request
     * @return
     */
    @Override
    public void checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        boolean checkToken = JWTUtils.checkToken(token);
        System.out.println(checkToken);
        Assert.isTrue(checkToken, ResponseEnum.LOGIN_AUTH_ERROR);
    }

}
