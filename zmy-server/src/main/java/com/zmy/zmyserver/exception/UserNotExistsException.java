package com.zmy.zmyserver.exception;

public class UserNotExistsException extends RestException {
    public UserNotExistsException() {
        super(ErrorEnum.USER_NOT_EXISTS.code(), ErrorEnum.USER_NOT_EXISTS.desc());
    }
}
