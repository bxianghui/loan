package com.bxx.loan.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bxx.loan.core.enums.BorrowInfoStatusEnum;
import com.bxx.loan.core.enums.BorrowStatusEnum;
import com.bxx.loan.core.enums.DictEnum;
import com.bxx.loan.core.mapper.BorrowInfoMapper;
import com.bxx.loan.core.mapper.UserIntegralMapper;
import com.bxx.loan.core.pojo.entity.BorrowInfo;
import com.bxx.loan.core.pojo.entity.Borrower;
import com.bxx.loan.core.pojo.entity.UserIntegral;
import com.bxx.loan.core.pojo.vo.BorrowInfoApprovalVO;
import com.bxx.loan.core.pojo.vo.BorrowInfoDetailVO;
import com.bxx.loan.core.pojo.vo.BorrowInfoVO;
import com.bxx.loan.core.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 服务实现类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Service
public class BorrowInfoServiceImpl extends ServiceImpl<BorrowInfoMapper, BorrowInfo> implements BorrowInfoService {

    @Resource
    private DictService dictService;

    @Resource
    private BorrowerService borrowerService;

    @Resource
    private UserIntegralMapper userIntegralMapper;

    @Resource
    private IntegralGradeService integralGradeService;

    @Resource
    private LendService lendService;

    /**
     * 连接查询
     * @return
     */
    @Override
    public List<BorrowInfo> listJoin() {
        List<BorrowInfo> list = baseMapper.listJoin();
        if (list == null || list.size() == 0) {
            return null;
        }
        list.forEach(this::getDetailString);

        return list;
    }

    /**
     * 获取借款记录的具体信息
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> getDetailById(Long id) {
        Map<String, Object> data = new HashMap<>();

        //借贷信息
        BorrowInfo borrowInfo = baseMapper.selectById(id);
        getDetailString(borrowInfo);

        //接待人信息
        Borrower borrower = borrowerService.getBorrower(borrowInfo.getUserId());
        BorrowInfoDetailVO borrowInfoDetailVO = borrowerToBorrowInfoVO(borrower);

        data.put("borrowInfo", borrowInfo);
        data.put("borrower", borrowInfoDetailVO);
        return data;
    }


    private void getDetailString(BorrowInfo borrowInfo) {
        String returnMethod = dictService.getDictString(DictEnum.RETURN_METHOD.getDictCode(), borrowInfo.getReturnMethod()) != null ?
                dictService.getDictString(DictEnum.RETURN_METHOD.getDictCode(), borrowInfo.getReturnMethod()) : "";
        String MoneyUse = dictService.getDictString(DictEnum.MONEY_USER.getDictCode(), borrowInfo.getMoneyUse()) != null ?
                dictService.getDictString(DictEnum.MONEY_USER.getDictCode(), borrowInfo.getMoneyUse()) : "";
        String status = BorrowInfoStatusEnum.getMsgByStatus(borrowInfo.getStatus());
        borrowInfo.getParam().put("returnMethod", returnMethod);
        borrowInfo.getParam().put("moneyUse", MoneyUse);
        borrowInfo.getParam().put("status", status);
    }

    /**
     * 审批借款信息
     * @param borrowInfoApprovalVO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approval(BorrowInfoApprovalVO borrowInfoApprovalVO) {
        BorrowInfo borrowInfo = baseMapper.selectById(borrowInfoApprovalVO.getId());
        if (borrowInfoApprovalVO.getStatus() == -1){
            borrowInfo.setStatus(BorrowInfoStatusEnum.CHECK_FAIL.getStatus());
            borrowInfo.setBorrowYearRate(borrowInfoApprovalVO.getLendYearRate());
            baseMapper.updateById(borrowInfo);
            return;
        }
        borrowInfo.setStatus(BorrowInfoStatusEnum.CHECK_OK.getStatus());
        baseMapper.updateById(borrowInfo);
        //创建新标的
        lendService.createLend(borrowInfo, borrowInfoApprovalVO);
    }

    /**
     * 获取借款条款信息的审批状态
     * @param userId
     * @return
     */
    @Override
    public Integer getBorrowInfoStatus(Long userId) {
        BorrowInfo borrowInfo = baseMapper.selectOne(new QueryWrapper<BorrowInfo>().eq("user_id", userId));
        if (borrowInfo == null){
            return BorrowInfoStatusEnum.NO_AUTH.getStatus();
        }
        return borrowInfo.getStatus();
    }

    /**
     * 最大借款金额
     * @param userId
     * @return
     */
    @Override
    public BigDecimal getBorrowAmount(Long userId) {
        UserIntegral userIntegral = userIntegralMapper.selectOne(new QueryWrapper<UserIntegral>().eq("user_id", userId));
        return integralGradeService.getGrade(userIntegral.getIntegral());
    }

    /**
     * 提交借款信息
     * @param borrowInfoVO
     * @param userId
     */
    @Override
    public void save(BorrowInfoVO borrowInfoVO, Long userId) {
        Integer count = baseMapper.selectCount(new QueryWrapper<BorrowInfo>().eq("user_id", userId));
        if (count > 0){
            baseMapper.deleteByUserId(userId);
        }
        BorrowInfo borrowInfo = new BorrowInfo();
        BeanUtils.copyProperties(borrowInfoVO, borrowInfo);
        borrowInfo.setUserId(userId);
        borrowInfo.setBorrowYearRate(borrowInfo.getBorrowYearRate().divide(new BigDecimal(100)));
        borrowInfo.setStatus(BorrowInfoStatusEnum.CHECK_RUN.getStatus());
        baseMapper.insert(borrowInfo);
    }

    /**
     * Borrower转换为BorrowInfoVO
     * @param borrower
     * @return
     */
    private BorrowInfoDetailVO borrowerToBorrowInfoVO(Borrower borrower) {
        if (borrower == null) return null;
        BorrowInfoDetailVO borrowInfoDetailVO = new BorrowInfoDetailVO();
        BeanUtils.copyProperties(borrower, borrowInfoDetailVO);

        borrowInfoDetailVO.setMarry(borrower.getSex() == 1 ? "男" : "女");
        borrowInfoDetailVO.setSex(borrower.getMarry() ? "已婚" : "未婚");
        borrowInfoDetailVO.setEducation(dictService.getDictString(DictEnum.EDUCATION.getDictCode(), borrower.getEducation()));
        borrowInfoDetailVO.setIndustry(dictService.getDictString(DictEnum.INDUSTRY.getDictCode(), borrower.getIndustry()));
        borrowInfoDetailVO.setIncome(dictService.getDictString(DictEnum.INCOME.getDictCode(), borrower.getIncome()));
        borrowInfoDetailVO.setReturnSource(dictService.getDictString(DictEnum.RETURN_METHOD.getDictCode(), borrower.getReturnSource()));
        borrowInfoDetailVO.setStatus(BorrowStatusEnum.getMsg(borrower.getStatus()));

        return borrowInfoDetailVO;
    }




}
