package com.zmy.zmyserver.advice;

import com.alibaba.fastjson.JSON;
import com.zmy.zmyserver.common.GlobalResponse;
import com.zmy.zmyserver.exception.ErrorEnum;
import com.zmy.zmyserver.exception.RestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {


    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public GlobalResponse<String> errorHandler(Throwable t) {
        RestException e;
        if (t instanceof RestException) {
            e = ((RestException) t);
        } else {
            t.printStackTrace();
            if (t.getCause() != null) {

                e = new RestException(ErrorEnum.UNKNOWN.code(), t.getCause().getMessage());
            } else {
                e = new RestException(ErrorEnum.UNKNOWN.code(), t.getMessage());
            }
        }
        return new GlobalResponse<>(e.getCode(), e.getDesc(), null);
    }


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !(returnType.getParameterType().isAssignableFrom(GlobalResponse.class) ||
                returnType.hasMethodAnnotation(NotControllerResponseAdvice.class));
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {


        if (body instanceof String) {
            return JSON.toJSONString(GlobalResponse.success(body));
        }

        return GlobalResponse.success(body);
    }
}
