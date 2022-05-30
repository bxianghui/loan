package com.bxx.loan.core;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;


/**
 * @author : bu
 * @date : 2022/5/17  11:01
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.bxx.loan")
public class LoanCoreMain {
    public static void main(String[] args) {
        SpringApplication.run(LoanCoreMain.class, args);
    }
}
