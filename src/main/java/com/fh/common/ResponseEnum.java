package com.fh.common;

public enum ResponseEnum {
    REG_MEMBER_IS_PASSWORD(1008,"密码错误！！！"),
    REG_MEMBER_IS_NULL_NAME_PHONE(1007,"用户名或手机号不存在！！！"),
    REDISCODE_ERROR(1006,"验证码错误！"),
    REDISCODE_NULL_NAME(1005,"验证码已失效！"),
    REG_MEMBER_IS_NULL_NAME(1004,"账号不能为空！！！"),
    REG_MEMBER_IS_NULL_MAIL(1003,"邮箱号已经存在！！！"),
    REG_MEMBER_IS_MAIL(1003,"邮箱号已经存在！！！"),
    REG_MEMBER_IS_PHONE(1002,"手机号已经存在！！！"),
    REG_MEMBER_IS_MEMBERNAME(1001,"账号已存在！！！"),
    REG_MEMBER_IS_NULL(1000,"信息不能为空！！！")
   ;

    private int code;
    private String msg;

    private ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
