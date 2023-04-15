package com.zmy.zmyserver.exception;

public class LogoutException extends RestException {
    public LogoutException() {
        super(ErrorEnum.LOGOUT_ERROR.code(), ErrorEnum.LOGOUT_ERROR.desc());
    }
}
