package com.zmy.zmyserver.exception;

public class ForgotTokenException extends RestException {
    public ForgotTokenException() {
        super(ErrorEnum.FORGOT_TOKEN_EXPIRATION.code(), ErrorEnum.FORGOT_TOKEN_EXPIRATION.desc());
    }
}
