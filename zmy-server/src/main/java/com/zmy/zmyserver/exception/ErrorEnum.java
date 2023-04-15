package com.zmy.zmyserver.exception;

public enum ErrorEnum {

    SUCCESS(200, "success"),
    UNKNOWN(-1, "UNKNOWN"),
    TOKEN_NOT_EXISTS(302, "token not exists"),
    TOKEN_EXPIRATION(302, "token expiration"),
    LOGIN_ERROR(10000, "username or password error"),
    REGISTER_ERROR(10001, "user is exists"),

    USER_NOT_EXISTS(10002, "user not exists"),
    USER_PASSWORD_ERROR(10003, "user password error"),
    FORGOT_TOKEN_EXPIRATION(10004, "forgot token expiration"),

    USER_EXISTS_ERROR(10005, "user already exists"),
    LOGOUT_ERROR(10006, "user logout error"),
    HEAD_IMAGE_UPLOAD_ERROR(10007, "user head image upload error"),
    EMAIL_SEND_ERROR(302, "mail send error"),
    AIR_EXISTS(20000, "air already exists"),
    AIR_NOT_EXISTS(20001, "air not exists");


    private int code;
    private String desc;

    private ErrorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int code() {
        return this.code;
    }

    public String desc() {
        return this.desc;
    }


}
