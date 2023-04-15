package com.zmy.zmyserver.advice;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotControllerResponseAdvice {
}
