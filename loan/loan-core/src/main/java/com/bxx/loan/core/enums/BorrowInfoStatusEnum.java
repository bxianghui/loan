package com.bxx.loan.core.enums;

import ch.qos.logback.core.status.InfoStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author : bu
 * @date : 2022/5/19  10:24
 */
@AllArgsConstructor
@Getter
public enum BorrowInfoStatusEnum {

    NO_AUTH(0, "未认证"),
    CHECK_RUN(1, "审核中"),
    CHECK_OK(2, "审核通过"),
    CHECK_FAIL(-1, "审核不通过"),
    ;

    private Integer status;
    private String msg;

    public static String getMsgByStatus(int status){
        BorrowInfoStatusEnum[] values = BorrowInfoStatusEnum.values();
        for (BorrowInfoStatusEnum v : values) {
            if (v.getStatus() == status){
                return v.getMsg();
            }
        }
        return "";
    }
}
