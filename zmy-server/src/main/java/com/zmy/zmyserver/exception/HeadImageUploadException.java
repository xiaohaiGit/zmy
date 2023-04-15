package com.zmy.zmyserver.exception;

public class HeadImageUploadException extends RestException {
    public HeadImageUploadException() {
        super(ErrorEnum.HEAD_IMAGE_UPLOAD_ERROR.code(), ErrorEnum.HEAD_IMAGE_UPLOAD_ERROR.desc());
    }
}
