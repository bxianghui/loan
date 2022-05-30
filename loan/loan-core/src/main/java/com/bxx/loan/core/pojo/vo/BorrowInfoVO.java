package com.bxx.loan.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author : bu
 * @date : 2022/5/25  17:01
 */
@ApiModel(description = "借款信息对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowInfoVO {

    @ApiModelProperty(value = "借款金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "期数")
    private Integer period;

    @ApiModelProperty(value = "还款方式")
    private Integer returnMethod;

    @ApiModelProperty(value = "借款用途")
    private Integer moneyUse;

    @ApiModelProperty(value = "年化利率")
    private BigDecimal borrowYearRate;

}
