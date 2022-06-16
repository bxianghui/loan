package com.bxx.loan.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bxx.loan.core.enums.BorrowStatusEnum;
import com.bxx.loan.core.enums.DictEnum;
import com.bxx.loan.core.mapper.BorrowerAttachMapper;
import com.bxx.loan.core.mapper.UserInfoMapper;
import com.bxx.loan.core.mapper.UserIntegralMapper;
import com.bxx.loan.core.pojo.entity.Borrower;
import com.bxx.loan.core.mapper.BorrowerMapper;
import com.bxx.loan.core.pojo.entity.BorrowerAttach;
import com.bxx.loan.core.pojo.entity.UserInfo;
import com.bxx.loan.core.pojo.entity.UserIntegral;
import com.bxx.loan.core.pojo.vo.BorrowerApprovalVO;
import com.bxx.loan.core.pojo.vo.BorrowerDetailVO;
import com.bxx.loan.core.pojo.vo.BorrowerAttachVO;
import com.bxx.loan.core.pojo.vo.BorrowerVO;

import com.bxx.loan.core.service.BorrowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bxx.loan.core.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款人 服务实现类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Service
@Slf4j
public class BorrowerServiceImpl extends ServiceImpl<BorrowerMapper, Borrower> implements BorrowerService {


    @Resource
    private BorrowerAttachMapper borrowerAttachMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private DictService dictService;

    @Resource
    private UserIntegralMapper userIntegralMapper;


    /**
     * 查询用户信息
     * @param page
     * @param size
     * @param keyWords
     * @return
     */
    @Override
    public Map<String, Object> pageSelect(Long page, Long size, String keyWords) {

        Page<Borrower> pageQuery = new Page<>(page, size);
        Map<String, Object> data = new HashMap<>();
        QueryWrapper<Borrower> queryWrapper = null;
        if (StringUtils.isNotBlank(keyWords)){
            queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name", keyWords)
                    .or().like("id_card", keyWords)
                    .or().like("mobile", keyWords)
                    .orderByDesc("id");
        }
        pageQuery = baseMapper.selectPage(pageQuery, queryWrapper);

        //获取总条数
        long total = pageQuery.getTotal();
        //获取当前页的list
        List<Borrower> records = pageQuery.getRecords();

        data.put("total", total);
        data.put("list", records);
        return data;
    }

    /**
     * 通过 user_id获取borrower
     * @param userId
     * @return
     */
    @Override
    public Borrower getBorrower(Long userId) {
        QueryWrapper<Borrower> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        Borrower borrower = baseMapper.selectOne(queryWrapper);
        if (borrower != null){
            return borrower;
        }
        return null;
    }

    /**
     * 通过uerId获取借款人的信息审核
     * @param userId
     * @return
     */
    @Override
    public Integer getBorrowerStatus(Long userId) {
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper.select("status").eq("user_id", userId);
        List<Object> objects = baseMapper.selectObjs(borrowerQueryWrapper);
        if (objects.size() == 0){
            return BorrowStatusEnum.NO_AUTH.getStatus();
        }
        return (Integer)objects.get(0);
    }

    /**
     * 保存借款人借款信息到数据库中
     * @param borrowerVO
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(BorrowerVO borrowerVO, Long userId) {
        //查询是否已经存在记录,若已存在直接删除
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<Borrower>().eq("user_id", userId);
        Borrower hasBorrower = baseMapper.selectOne(borrowerQueryWrapper);
        if (hasBorrower != null){
            baseMapper.deleteByUserId(userId);
            borrowerAttachMapper.deleteByBorrowerId(hasBorrower.getId());
        }
        //填充Borrower
        Borrower borrower = new Borrower();
        BeanUtils.copyProperties(borrowerVO, borrower);
        borrower.setUserId(userId);
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("id", userId);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);
        borrower.setName(userInfo.getName());
        borrower.setMobile(userInfo.getMobile());
        borrower.setIdCard(userInfo.getIdCard());
        borrower.setStatus(BorrowStatusEnum.CHECK_RUN.getStatus());
        //保存至数据库
        baseMapper.insert(borrower);
        //填充Borrower
        List<BorrowerAttach> borrowerAttachList = borrowerVO.getBorrowerAttachList();
        borrowerAttachList.forEach(borrowerAttach -> {
            borrowerAttach.setBorrowerId(borrower.getId());
            //保存至数据库
            borrowerAttachMapper.insert(borrowerAttach);
        });

        //更新userInfo中的借款人认证状态
        userInfo.setBorrowAuthStatus(BorrowStatusEnum.CHECK_RUN.getStatus());
        userInfoMapper.updateById(userInfo);
    }

    /**
     * 获取借款人的详细信息，方便审批
     * @param id
     * @return
     */
    @Override
    public BorrowerDetailVO getBorrowerDetailVOById(Long id) {
        Borrower borrower = baseMapper.selectById(id);
        BorrowerDetailVO borrowerDetailVO = new BorrowerDetailVO();
        BeanUtils.copyProperties(borrower, borrowerDetailVO);
        getBorrowerDetailVO(borrower, borrowerDetailVO);
        List<BorrowerAttach> borrowerAttaches = borrowerAttachMapper.selectList(new QueryWrapper<BorrowerAttach>().
                eq("borrower_id", id));
        List<BorrowerAttachVO> borrowerAttachList = new ArrayList<>(borrowerAttaches.size());
        borrowerAttaches.forEach(borrowerAttach ->{
            BorrowerAttachVO borrowerAttachVO = new BorrowerAttachVO();
            BeanUtils.copyProperties(borrowerAttach, borrowerAttachVO);
            borrowerAttachList.add(borrowerAttachVO);
        });
        borrowerDetailVO.setBorrowerAttachVOList(borrowerAttachList);
        return borrowerDetailVO;
    }

    public void getBorrowerDetailVO(Borrower borrower, BorrowerDetailVO borrowerDetailVO){
        borrowerDetailVO.setSex(borrower.getSex()==1 ? "男" : "女");
        borrowerDetailVO.setMarry(borrower.getMarry()? "是" : "否");
        borrowerDetailVO.setEducation(dictService.getDictString(DictEnum.EDUCATION.getDictCode(),
                borrower.getEducation()));
        borrowerDetailVO.setIndustry(dictService.getDictString(DictEnum.INDUSTRY.getDictCode(),
                borrower.getIndustry()));
        borrowerDetailVO.setIncome(dictService.getDictString(DictEnum.INCOME.getDictCode(),
                borrower.getIncome()));
        borrowerDetailVO.setReturnSource(dictService.getDictString(DictEnum.RETURN_SOURCE.getDictCode(),
                borrower.getReturnSource()));
        borrowerDetailVO.setContactsRelation(dictService.getDictString(DictEnum.RELATION.getDictCode(),
                borrower.getContactsRelation()));
        borrowerDetailVO.setStatus(BorrowStatusEnum.getMsg(borrower.getStatus()));
    }

    /**
     * 审批借款人身份认定
     * @param borrowerApprovalVO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approval(BorrowerApprovalVO borrowerApprovalVO) {
        System.out.println(borrowerApprovalVO.toString());
        Borrower borrower = baseMapper.selectById(borrowerApprovalVO.getBorrowerId());
        Long userId = borrower.getUserId();
        UserInfo userInfo = userInfoMapper.selectById(userId);

        //不通过
        if (borrowerApprovalVO.getStatus() == -1){
            borrower.setStatus(BorrowStatusEnum.CHECK_FAIL.getStatus());
            userInfo.setBorrowAuthStatus(BorrowStatusEnum.CHECK_FAIL.getStatus());
            userInfoMapper.updateById(userInfo);
            baseMapper.updateById(borrower);
            return;
        }

        //审批过程中要将用户的积分信息进行保存
        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setUserId(userId);
        int integral = borrowerApprovalVO.getInfoIntegral();
        StringBuilder context = new StringBuilder();
        context.append("身份信息获取").append(integral).append("积分;");
        if (borrowerApprovalVO.getIsCarOk()){
            integral += 30;
            context.append("身份证信息获取30积分;");
        }
        if (borrowerApprovalVO.getIsCarOk()){
            integral += 60;
            context.append("车辆信息获取60积分;");
        }
        if (borrowerApprovalVO.getIsHouseOk()){
            integral += 100;
            context.append("房产证信息获取100积分;");
        }
        userIntegral.setIntegral(integral);
        userIntegral.setContent(context.toString());
        userIntegralMapper.insert(userIntegral);

        //更改当前借款人的状态
        borrower.setStatus(BorrowStatusEnum.CHECK_OK.getStatus());
        baseMapper.updateById(borrower);

        //填充userInfo的积分信息
        userInfo.setIntegral(integral);
        userInfo.setBorrowAuthStatus(BorrowStatusEnum.CHECK_OK.getStatus());
        userInfoMapper.updateById(userInfo);
    }


    /**
     * 标的详细的展示
     * @param userId
     * @return
     */
    @Override
    public BorrowerDetailVO getBorrowDetail(Long userId) {
        BorrowerDetailVO borrowerDetailVO = new BorrowerDetailVO();
        Borrower borrower = baseMapper.selectOne(new QueryWrapper<Borrower>().eq("user_id", userId));
        BeanUtils.copyProperties(borrower, borrowerDetailVO);
        getBorrowerDetailVO(borrower, borrowerDetailVO);
        return borrowerDetailVO;
    }
}
