package com.bxx.loan.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : bu
 * @date : 2022/5/21  17:06
 */
@ApiModel(description = "用户绑定账户对象")
@Data
public class UserBindVO {

    @ApiModelProperty(value = "真实姓名")
    private String name;

    @ApiModelProperty(value = "身份证")
    private String idCard;

    @ApiModelProperty(value = "绑定银行")
    private String bankType;

    @ApiModelProperty(value = "绑定卡号")
    private String bankNo;

    @ApiModelProperty(value = "预留手机")
    private String mobile;
}
