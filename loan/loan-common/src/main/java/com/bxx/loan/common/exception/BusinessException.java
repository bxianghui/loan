package com.bxx.loan.common.exception;

import com.bxx.loan.common.result.ResponseEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * 为了解决每个异常繁琐的问题和准确的解决异常，此类自定义异常类就格外重要，减少代码的繁琐
 * @author : bu
 * @date : 2022/5/17  16:24
 */
@NoArgsConstructor
@Data
public class BusinessException extends RuntimeException{

    //    错误码
    private Integer code;
    //    错误消息
    private String message;

    /**
     *
     * @param message 错误消息
     */
    public BusinessException(String message) {
        this.message = message;
    }

    /**
     *
     * @param message 错误消息
     * @param code 错误码
     */
    public BusinessException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    /**
     *
     * @param message 错误消息
     * @param code 错误码
     * @param cause 原始异常对象
     */
    public BusinessException(String message, Integer code, Throwable cause) {
        super(cause);
        this.message = message;
        this.code = code;
    }

    /**
     *
     * @param resultCodeEnum 接收枚举类型
     */
    public BusinessException(ResponseEnum resultCodeEnum) {
        this.message = resultCodeEnum.getMessage();
        this.code = resultCodeEnum.getCode();
    }

    /**
     *
     * @param resultCodeEnum 接收枚举类型
     * @param cause 原始异常对象
     */
    public BusinessException(ResponseEnum resultCodeEnum, Throwable cause) {
        super(cause);
        this.message = resultCodeEnum.getMessage();
        this.code = resultCodeEnum.getCode();
    }
}
