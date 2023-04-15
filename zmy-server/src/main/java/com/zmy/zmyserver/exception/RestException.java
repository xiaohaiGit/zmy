package com.zmy.zmyserver.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestException extends RuntimeException {
    private int code;
    private String desc;

}
