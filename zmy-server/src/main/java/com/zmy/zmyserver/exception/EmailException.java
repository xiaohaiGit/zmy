package com.zmy.zmyserver.exception;

public class EmailException extends RestException {
    public EmailException() {
        super(ErrorEnum.EMAIL_SEND_ERROR.code(), ErrorEnum.EMAIL_SEND_ERROR.desc());
    }

}
