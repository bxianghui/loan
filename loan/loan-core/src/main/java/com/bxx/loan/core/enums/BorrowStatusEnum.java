package com.bxx.loan.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : bu
 * @date : 2022/5/19  16:05
 */
@AllArgsConstructor
@Getter
public enum BorrowStatusEnum {

    NO_AUTH(0, "未认证"),
    CHECK_RUN(1, "认证中"),
    CHECK_OK(2, "认证通过"),
    CHECK_FAIL(-1, "认证失败"),
    ;

    private Integer status;

    private String message;

    public static String getMsg(Integer status){
        BorrowStatusEnum[] values = BorrowStatusEnum.values();
        for (BorrowStatusEnum v: values) {
            if (v.getStatus().equals(status)){
                return v.getMessage();
            }
        }
        return "";
    }
}
