package com.heepay.config;

import com.alibaba.fastjson.JSON;
import com.heepay.mapper.UserBindMapper;
import com.heepay.util.SignUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
public class NotifyThread implements Runnable {

    private int count = 1;
    private String notifyUrl;
    private Map<String, Object> paramMap;
    @Resource
    private UserBindMapper userBindMapper;

    public NotifyThread(){}

    public NotifyThread(String notifyUrl, Map<String, Object> paramMap) {
        this.notifyUrl = notifyUrl;
        this.paramMap = paramMap;
    }

    @Override
    public void run() {
        task();
    }

    private void task() {
        String result = SignUtil.sendRequest(paramMap,notifyUrl);
        log.info(notifyUrl + "：" + result + " count：" + count);
        if(!"success".equals(result)) {
            try {

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //ScheduledTask.queue.offer(new NotifyVo(notifyUrl, paramMap));
            count++;
            if(count <= 5) {
                task();
                log.info("失败重试：" + JSON.toJSONString(this));
            }
        }
//        String agentUserId = (String)paramMap.get("agentUserId");
//        System.out.println(agentUserId);
//        userBindMapper.updateStatus(Integer.valueOf(agentUserId));
    }
}
