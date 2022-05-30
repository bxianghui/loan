package com.bxx.loan.common.handler;

import com.bxx.loan.common.exception.BusinessException;
import com.bxx.loan.common.result.ResponseEnum;
import com.bxx.loan.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author : bu
 * @date : 2022/5/17  15:57
 *  全局的异常处理器
 *  处理controller中出现的异常问题
 */

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    /**
     * 处理所有异常的handler
     * @param e 异常信息
     * @return 统一返回信息
     */
    @ExceptionHandler(value = Exception.class)
    public Result handlerException(Exception e){
        log.error(e.getMessage(), e);
        return Result.error();
    }

    /**
     * 处理Sql异常的handler
     * @param e 异常信息
     * @return 统一返回信息
     */
    @ExceptionHandler(value = BadSqlGrammarException.class)
    public Result handlerBadSqlGrammarException(BadSqlGrammarException e){
        log.error(e.getMessage(), e);
        return Result.setResult(ResponseEnum.BAD_SQL_GRAMMAR_ERROR);
    }

    /**
     * 为了解决不同的异常，使用枚举类和自定义异常类进行处理
     * @param e 异常信息
     * @return 统一返回信息
     */
    @ExceptionHandler(value = BusinessException.class)
    public Result handlerBusinessException(BusinessException e){
        log.error(e.getMessage(), e);
        return Result.error().message(e.getMessage()).code(e.getCode());
    }


    /**
     * controller层 之前出现的错误
     * @param e 异常信息
     * @return
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MethodArgumentNotValidException.class,
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    public Result handleServletException(Exception e) {
        log.error(e.getMessage(), e);
        //SERVLET_ERROR(-102, "servlet请求异常"),
        return Result.error().message(ResponseEnum.SERVLET_ERROR.getMessage()).code(ResponseEnum.SERVLET_ERROR.getCode());
    }

}
