package com.zmy.zmyserver.exception;

public class TokenExpirationException extends RestException {
    public TokenExpirationException() {
        super(ErrorEnum.TOKEN_EXPIRATION.code(), ErrorEnum.TOKEN_EXPIRATION.desc());
    }
}
