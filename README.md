


![68747470733a2f2f696d616765732e67697465652e636f6d2f75706c6f6164732f696d616765732f323032312f303333312f3039323030305f32393538313532395f373735383339322e706e67](https://user-images.githubusercontent.com/90383015/170948449-1b80d8eb-59a0-483f-96a7-f684b73a0286.jpg)
# loan
尚融宝后端源码

## 尚融宝项目在loan文件中， 第三方支付平台汇付宝在hfb中 

## 项目地址  （数据全虚拟，可以随便填写, 还存在一些bug后续慢慢改进） 
	前台：http://www.facexk.top（110.40.137.41）  前台用户注册功能手机短信没有实现  验证码是手机号的前四位  hfb绑定用户信息时 手机验证码随便填写
	后台：http://www.facexk.top/admin（110.40.137.41/admin）


## 项目介绍
尚融宝是一个金融借贷项目，主要功能实现，个人账户绑定、投资、借款、还款，一系列操作。后台管理主要审核账户的借款申请和借款项目的放款操作。

## 技术栈

SpringBoot 2.3.4.release

SpringCloud Hoston.SR9: 微服务注册，调用，服务熔断，网关，配置中心

SpringCloud Alibaba 2.2.2.release  nacos gateway sentinel openfeign

Mybatis Plus

Alibaba-easyexcel : 上传和下载excel文件

Redis

HttpClient  远程调用第三方支付平台 汇付宝（SpringBoot项目）

## 第三方接口  （如果您要下载本项目并部署，请修改配置文件中阿里云Accsess Key）

Alibaba oss

Alibaba sms  （申请阿里云短信服务要求很严格，本项目直接使用的是手机号的前四位作为验证码）

汇付宝

## 数据库

mysql 8.×  

navicat



