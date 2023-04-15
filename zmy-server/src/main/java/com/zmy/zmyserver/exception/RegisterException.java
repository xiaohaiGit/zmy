package com.zmy.zmyserver.exception;

public class RegisterException extends RestException {
    public RegisterException() {
        super(ErrorEnum.REGISTER_ERROR.code(), ErrorEnum.REGISTER_ERROR.desc());
    }
}
