package com.bxx.loan.core.pojo.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author : bu
 * @date : 2022/5/19  15:05
 */
@Data
@ToString
@ApiModel(value = "借款人详细信息")
public class BorrowInfoDetailVO {

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("性别（1：男 0：女）")
    private String sex;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("学历")
    private String education;

    @ApiModelProperty("是否结婚（1：是 0：否）")
    private String marry;

    @ApiModelProperty("行业")
    private String industry;

    @ApiModelProperty("月收入")
    private String income;

    @ApiModelProperty("还款来源")
    private String returnSource;

    @ApiModelProperty("状态（0：未认证，1：认证中， 2：认证通过， -1：认证失败）")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}

