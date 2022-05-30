package com.bxx.loan.common.result;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : bu
 * @date : 2022/5/17  15:01
 */

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class Result {

    private Integer code;

    private String message;

    private Object data;

    public static Result success(){
        Result r = new Result();
        r.setCode(ResponseEnum.SUCCESS.getCode());
        r.setMessage(ResponseEnum.SUCCESS.getMessage());
        return r;
    }

    public static Result error(){
        Result r = new Result();
        r.setCode(ResponseEnum.ERROR.getCode());
        r.setMessage(ResponseEnum.ERROR.getMessage());
        return r;
    }

    public static Result setResult(ResponseEnum responseEnum){
        Result r = new Result();
        r.setCode(responseEnum.getCode());
        r.setMessage(responseEnum.getMessage());
        return r;
    }

    public Result data(Object data){
        this.setData(data);
        return this;
    }

    public Result message(String message){
        this.setMessage(message);
        return this;
    }

    public Result code(Integer code){
        this.setCode(code);
        return this;
    }
}
