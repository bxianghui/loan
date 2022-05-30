package com.bxx.loan.core.pojo.query;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : bu
 * @date : 2022/5/18  10:16
 */
@Data
@ApiModel(value="会员查询条件")
public class UserInfoQuery {

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "用户类型 1.投资人 2.贷款人")
    private Integer userType;

}
