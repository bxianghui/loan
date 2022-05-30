# loan
尚融宝的后端源码


项目介绍
尚融宝是一个金融借贷项目，主要功能实现，个人账户绑定、投资、借款、还款，一系列操作。后台管理主要审核账户的借款申请和借款项目的放款操作。

技术栈

SpringBoot 2.3.4.release

SpringCloud Hoston.SR9: 微服务注册，调用，服务熔断，网关，配置中心

SpringCloud Alibaba 2.2.2.release

Mybatis Plus

Alibaba-easyexcel : 上传和下载excel文件

Redis

HttpClient  远程调用第三方支付平台 汇付宝（SpringBoot项目）

第三方接口

Alibaba oss

Alibaba sms  （申请阿里云短信服务要求很严格，本项目直接使用的是手机号的前四位作为验证码）

汇付宝

数据库

mysql 8.×  

navicat

项目框架

![Uploading image.png…]()

