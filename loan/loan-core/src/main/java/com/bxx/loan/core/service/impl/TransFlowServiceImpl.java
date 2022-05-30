package com.bxx.loan.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bxx.loan.core.mapper.UserInfoMapper;
import com.bxx.loan.core.pojo.entity.TransFlow;
import com.bxx.loan.core.mapper.TransFlowMapper;
import com.bxx.loan.core.pojo.entity.UserInfo;
import com.bxx.loan.core.pojo.vo.TransFlowVO;
import com.bxx.loan.core.service.TransFlowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 交易流水表 服务实现类
 * </p>
 *
 * @author Bxx
 * @since 2022-05-17
 */
@Service
public class TransFlowServiceImpl extends ServiceImpl<TransFlowMapper, TransFlow> implements TransFlowService {

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 验证是否已经存在相同流水记录
     * @param agentBillNo
     * @return
     */
    @Override
    public boolean isSaveTransFlow(String agentBillNo) {
        QueryWrapper<TransFlow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trans_no", agentBillNo);
        TransFlow transFlow = baseMapper.selectOne(queryWrapper);
        return transFlow != null;
    }

    /**
     * 保存流水信息
     * @param transFlowVO
     */
    @Override
    public void saveTransFlow(TransFlowVO transFlowVO) {
        String bindCode = transFlowVO.getBindCode();

        QueryWrapper<UserInfo> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.eq("bind_code", bindCode);
        UserInfo userInfo = userInfoMapper.selectOne(infoQueryWrapper);

        TransFlow transFlow = new TransFlow();
        transFlow.setUserId(userInfo.getId());
        transFlow.setUserName(userInfo.getName());
        transFlow.setTransNo(transFlowVO.getAgentBillNo());
        transFlow.setTransType(transFlowVO.getTransTypeEnum().getTransType());
        transFlow.setTransTypeName(transFlowVO.getTransTypeEnum().getTransTypeName());
        transFlow.setTransAmount(transFlowVO.getAmount());
        transFlow.setMemo(transFlowVO.getMemo());

        baseMapper.insert(transFlow);
    }

    /**
     * 根据id获取transFlowList
     * @param userId
     * @return
     */
    @Override
    public List<TransFlow> list(Long userId) {
        List<TransFlow> transFlows = baseMapper.selectList(new QueryWrapper<TransFlow>().eq("user_id", userId));
        return transFlows;
    }
}
