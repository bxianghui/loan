package com.bxx.loan.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author : bu
 * @date : 2022/5/19  20:37
 */
@ApiModel("标的信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LendVO {

    @ApiModelProperty(value = "标的编号")
    private String lendNo;

    @ApiModelProperty(value = "标的金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "投资期数")
    private Integer period;

    @ApiModelProperty(value = "年化利率")
    private BigDecimal lendYearRate;

    @ApiModelProperty(value = "已投金额")
    private BigDecimal investAmount;

    @ApiModelProperty(value = "投资人数")
    private Integer investNum;

    @ApiModelProperty(value = "开始日期")
    private LocalDate lendStartDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDate lendEndDate;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "还款方式")
    private String returnMethod;
}
