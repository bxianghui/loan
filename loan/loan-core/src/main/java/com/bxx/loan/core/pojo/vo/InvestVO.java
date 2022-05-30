package com.bxx.loan.core.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author : bu
 * @date : 2022/5/26  16:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvestVO {

    private Long lendId;

    private BigDecimal investAmount;
}
