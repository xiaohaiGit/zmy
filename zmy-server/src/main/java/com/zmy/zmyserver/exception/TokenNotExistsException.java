package com.zmy.zmyserver.exception;

public class TokenNotExistsException extends RestException {
    public TokenNotExistsException() {
        super(ErrorEnum.TOKEN_NOT_EXISTS.code(), ErrorEnum.TOKEN_NOT_EXISTS.desc());
    }
}
