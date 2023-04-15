package com.zmy.zmyserver.common;

import com.zmy.zmyserver.exception.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GlobalResponse<T> {
    private int code;
    private String desc;
    private T data;

    public static <T> GlobalResponse success(T data) {
        return GlobalResponse.builder()
                .code(ErrorEnum.SUCCESS.code())
                .desc(ErrorEnum.SUCCESS.desc())
                .data(data)
                .build();
    }
}
