package com.zmy.zmyserver.interceptor;

import com.zmy.zmyserver.exception.TokenExpirationException;
import com.zmy.zmyserver.exception.TokenNotExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("======== auth   preHandle ========");
        String token = request.getHeader("token");
        if (token == null) {
            log.error("token not exists");
            throw new TokenNotExistsException();
        }
        String key = "token." + token;
        String userInfo = redisTemplate.opsForValue().get(key);
        if (userInfo == null) {
            log.error("token expiration , token : {}", token);
            throw new TokenExpirationException();
        }

        log.info("token expiration time update");
        redisTemplate.expire(key, 60, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("======== auth   postHandle ========");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("======== auth   afterCompletion ========");
    }
}