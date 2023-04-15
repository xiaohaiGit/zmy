package com.zmy.zmyserver.exception;

public class AirExistsException extends RestException {
    public AirExistsException() {
        super(ErrorEnum.AIR_EXISTS.code(), ErrorEnum.AIR_EXISTS.desc());
    }
}
