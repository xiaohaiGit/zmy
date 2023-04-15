package com.zmy.zmyserver.exception;


public class LoginException extends RestException {
    public LoginException() {
        super(ErrorEnum.LOGIN_ERROR.code(), ErrorEnum.LOGIN_ERROR.desc());
    }
}
