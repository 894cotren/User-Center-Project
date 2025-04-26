package com.awc20.usercenter.common;

import lombok.Data;

/**
 * 统一请求返回
 * @author grey
 */
@Data
public class Result<T>{
    /**
     * 返回给前端的状态码  0 成功  1 失败
     */
    private Integer code;
    /**
     * 数据体
     */
    private T data;
    /**
     * 消息
     */
    private String message;

    /**
     * 具体描述
     */
    private String description;

    /**
     * 私有化构造函数
     */
    private Result(){}

    /**
     * 成功统一返回对象
     * @param data  数据体
     */
    public static <T> Result<T> success(T data){
        Result<T> result=new Result<>();
        result.code=0;
        result.data=data;
        result.message="成功";
        result.description="";
        return result;
    }

    /**
     * 统一失败返回
     * @param message  错误消息信息
     */
    public static <T> Result<T> fail(String message){
        Result<T> result=new Result<>();
        result.code=1;
        result.data=null;
        result.message=message;
        result.description="";
        return result;
    }

    public static <T> Result<T> fail(int code,String message,String description){
        Result<T> result=new Result<>();
        result.code=code;
        result.data=null;
        result.message=message;
        result.description=description;
        return result;
    }

    public static <T> Result<T> fail(ErrorCodeEnum errorCodeEnum){
        Result<T> result=new Result<>();
        result.code=errorCodeEnum.getCode();
        result.data=null;
        result.message=errorCodeEnum.getMessage();
        result.description=errorCodeEnum.getDescription();
        return result;
    }

    public static <T> Result<T> fail(ErrorCodeEnum errorCodeEnum,String description){
        Result<T> result=new Result<>();
        result.code=errorCodeEnum.getCode();
        result.data=null;
        result.message=errorCodeEnum.getMessage();
        result.description=description;
        return result;
    }

    public static <T> Result<T> fail(ErrorCodeEnum errorCodeEnum,String message,String description){
        Result<T> result=new Result<>();
        result.code=errorCodeEnum.getCode();
        result.data=null;
        result.message=message;
        result.description=description;
        return result;
    }

    /**
     * 自定义返回 参数自定
     * @param code 状态码
     * @param data 数据体
     * @param message 发给前端的消息
     */
    public static <T> Result<T> build(Integer code,T data,String message,String description){
        Result<T> result=new Result<>();
        result.code=code;
        result.data=data;
        result.message=message;
        result.description=description;
        return result;
    }
}
