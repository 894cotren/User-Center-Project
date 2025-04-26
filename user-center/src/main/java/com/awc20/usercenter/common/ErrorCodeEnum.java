package com.awc20.usercenter.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误状态码&信息 枚举类
 * @author grey
 */

@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {

    PARAMS_ERROR(40000,"参数错误",""),
    NULL_ERROR(40001,"请求数据为空",""),
    NOT_LOGIN(40100,"未登录",""),
    NOT_AUTH(40101,"无权限",""),
    SYSTEM_ERROR(50000,"业务系统内部异常","");

    private final int code;
    private final String message;
    private final String description;
}
