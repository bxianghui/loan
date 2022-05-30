package com.bxx.loan.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bxx.loan.core.mapper.LendItemMapper;
import com.bxx.loan.core.mapper.LendMapper;
import com.bxx.loan.core.mapper.LendReturnMapper;
import com.bxx.loan.core.pojo.entity.Lend;
import com.bxx.loan.core.pojo.entity.LendItem;
import com.bxx.loan.core.pojo.entity.LendItemReturn;
import com.bxx.loan.core.mapper.LendItemReturnMapper;
import com.bxx.loan.core.pojo.entity.LendReturn;
import com.bxx.loan.core.service.LendItemReturnService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bxx.loan.core.service.UserBindService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借回款记录表 服务实现类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Service
public class LendItemReturnServiceImpl extends ServiceImpl<LendItemReturnMapper, LendItemReturn> implements LendItemReturnService {

    @Resource
    private LendMapper lendMapper;

    @Resource
    private LendReturnMapper lendReturnMapper;

    @Resource
    private LendItemMapper lendItemMapper;

    @Resource
    private UserBindService userBindService;

    /**
     * 此处应该注意更新是否逾期
     * @param userId
     * @param lendId
     * @return
     */
    @Override
    public List<LendItemReturn> listById(Long userId, Long lendId) {
        List<LendItemReturn> lendItemReturns = baseMapper.selectList(new QueryWrapper<LendItemReturn>().eq("lend_id", lendId).eq("invest_user_id", userId));
        lendItemReturns.forEach(lendItemReturn -> {
            if (LocalDate.now().compareTo(lendItemReturn.getReturnDate()) >= 1){
                lendItemReturn.setOverdue(true);
                lendItemReturn.setOverdueTotal(lendItemReturn.getTotal());
                baseMapper.updateById(lendItemReturn);
            }
        });
        return lendItemReturns;
    }

    @Override
    public List<LendItemReturn> listByUserId(Long userId) {
        List<LendItemReturn> lendItemReturns = baseMapper.selectList(new QueryWrapper<LendItemReturn>().eq("invest_user_id", userId));
        lendItemReturns.forEach(lendItemReturn -> {
            lendItemReturn.setLendStatus(lendItemReturn.getStatus()==0 ? "未还款" : "已还款");
            lendItemReturn.setOverdueStr(lendItemReturn.getOverdue()? "是":"否");
        });
        return lendItemReturns;
    }

    /**
     * 通过还款计划的id，找到对应的回款计划数据，组装data参数中需要的List<Map>
     * @param lendReturnId
     * @return
     */
    @Override
    public List<Map<String, Object>> addReturnDetail(Long lendReturnId) {
        //还款记录
        LendReturn lendReturn = lendReturnMapper.selectById(lendReturnId);
        //获取标的
        Lend lend = lendMapper.selectById(lendReturn.getLendId());


        List<LendItemReturn> lendItemReturnList = selectLendItemReturnList(lendReturnId);
        List<Map<String, Object>> lendItemReturnDetailList = new ArrayList<>();
        for (LendItemReturn lendItemReturn : lendItemReturnList) {

            //获取投资记录
            Long lendItemId = lendItemReturn.getLendItemId();
            LendItem lendItem = lendItemMapper.selectById(lendItemId);

            //获取投资人id
            Long investUserId = lendItem.getInvestUserId();
            String bindCode = userBindService.getBindCodeById(investUserId);

            Map<String, Object> map = new HashMap<>();
            map.put("agentProjectCode", lend.getLendNo());//项目编号
            map.put("voteBillNo", lendItem.getLendItemNo());//投资编号
            map.put("toBindCode", bindCode); //投资人bindCode
            map.put("transitAmt", lendItemReturn.getTotal());//还款总额
            map.put("baseAmt", lendItemReturn.getPrincipal());//本金
            map.put("benifitAmt", lendItemReturn.getInterest());//利息
            map.put("feeAmt", new BigDecimal(0));

            lendItemReturnDetailList.add(map);
        }

        return lendItemReturnDetailList;
    }

    @Override
    public List<LendItemReturn> selectLendItemReturnList(Long lendReturnId) {
        QueryWrapper<LendItemReturn> lendItemReturnQueryWrapper = new QueryWrapper<>();
        lendItemReturnQueryWrapper.eq("lend_return_id", lendReturnId);
        return baseMapper.selectList(lendItemReturnQueryWrapper);
    }
}
