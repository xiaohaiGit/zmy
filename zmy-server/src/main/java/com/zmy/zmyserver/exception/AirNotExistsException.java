package com.zmy.zmyserver.exception;

public class AirNotExistsException extends RestException {
    public AirNotExistsException() {
        super(ErrorEnum.AIR_NOT_EXISTS.code(), ErrorEnum.AIR_NOT_EXISTS.desc());
    }
}
