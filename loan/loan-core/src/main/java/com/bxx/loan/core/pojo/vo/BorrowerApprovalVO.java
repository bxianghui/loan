package com.bxx.loan.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : bu
 * @date : 2022/5/25  9:22
 */
@ApiModel(description = "借款人审批提交信息")
@Data
@NoArgsConstructor
public class BorrowerApprovalVO {

    @ApiModelProperty(value = "id")
    private Long borrowerId;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty("个人信息积分")
    private Integer infoIntegral;

    @ApiModelProperty(value = "身份证信息是否正确")
    private Boolean isIdCardOk;

    @ApiModelProperty(value = "房产证信息是否正确")
    private Boolean isHouseOk;

    @ApiModelProperty(value = "车辆信息证明是否正确")
    private Boolean isCarOk;

}
