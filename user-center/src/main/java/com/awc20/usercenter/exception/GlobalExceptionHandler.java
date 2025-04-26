package com.awc20.usercenter.exception;

import com.awc20.usercenter.common.ErrorCodeEnum;
import com.awc20.usercenter.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 * @author grey
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务内部异常处理handler
     * @param e 异常
     * @return 统一返回结果
     */
    @ExceptionHandler(BusinessException.class)
    public Result businessExceptionHandler(BusinessException e){
        log.error("businessException:"+e.getMessage(),e);
        return Result.fail(e.getCode(),e.getMessage(),e.getDescription());
    }


    /**
     * 业务系统内运行时异常处理handler
     * @param e 异常
     * @return 统一返回结果
     */
    @ExceptionHandler(RuntimeException.class)
    public Result runtimeExceptionHandler(RuntimeException e){
        log.error("runtimeException"+e.getMessage(),e);
        return Result.fail(ErrorCodeEnum.SYSTEM_ERROR,e.getMessage(),"");
    }
}
