package com.bxx.loan.core;

import com.bxx.loan.core.mapper.UserAccountMapper;
import com.bxx.loan.core.pojo.entity.UserAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author : bu
 * @date : 2022/5/20  22:33
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan({"com.bxx.loan"})
public class UserAccountTest {

    @Resource
    private UserAccountMapper userAccountMapper;

    @Test
    public void test(){
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(1L);
        userAccountMapper.insert(userAccount);
    }
}
