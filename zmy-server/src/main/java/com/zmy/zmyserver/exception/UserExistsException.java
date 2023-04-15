package com.zmy.zmyserver.exception;

public class UserExistsException extends RestException {
    public UserExistsException() {
        super(ErrorEnum.USER_EXISTS_ERROR.code(), ErrorEnum.USER_EXISTS_ERROR.desc());
    }
}
