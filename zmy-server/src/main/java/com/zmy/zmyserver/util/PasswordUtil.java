package com.zmy.zmyserver.util;

import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class PasswordUtil {


    @Autowired
    private StringRedisTemplate redisTemplate;


    public String forgotPassword(String email) {
        String token = TokenUtil.newToken();
        String key = "forgot." + token;
        redisTemplate.opsForValue().set(key, email);
        redisTemplate.expire(key, 20, TimeUnit.MINUTES);
        return token;
    }


    public static String md5Password(String password) {
        return DigestUtil.md5Hex(password);
    }


}
