package com.bxx.loan.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author : bu
 * @date : 2022/5/21  22:59
 */
@Data
@ApiModel(description = "展示用户信息的对象")
public class UserIndexVO {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "头像")
    private String headImg;

    @ApiModelProperty(value = "1：出借人 2：借款人")
    private Integer userType;

    @ApiModelProperty(value = "绑定状态")
    private Integer bindStatus;

    @ApiModelProperty(value = "上次登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "帐户可用余额")
    private BigDecimal amount;

    @ApiModelProperty(value = "冻结金额")
    private BigDecimal freezeAmount;

}
