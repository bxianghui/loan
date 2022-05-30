package com.bxx.loan.core.pojo.vo;

import com.bxx.loan.core.enums.TransTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author : bu
 * @date : 2022/5/22  14:20
 */
@Data
@ApiModel("充值体现流水对象")
@AllArgsConstructor
@NoArgsConstructor
public class TransFlowVO {

    @ApiModelProperty("流水号")
    private String agentBillNo;

    @ApiModelProperty("账户绑定Code")
    private String bindCode;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("流水类型")
    private TransTypeEnum transTypeEnum;

    @ApiModelProperty("备注")
    private String memo;
}
