package com.awc20.usercenter.exception;

import com.awc20.usercenter.common.ErrorCodeEnum;
import lombok.Data;
import lombok.Getter;

/**
 * 自定义异常类
 * @author grey
 */
@Getter
public class BusinessException extends RuntimeException{

    private int code;
    private String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum, String description) {
        super(errorCodeEnum.getMessage());
        this.code = errorCodeEnum.getCode();
        this.description = description;
    }

}
