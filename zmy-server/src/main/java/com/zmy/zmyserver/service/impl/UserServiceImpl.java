package com.zmy.zmyserver.service.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.zmy.zmyserver.dao.UserMapper;
import com.zmy.zmyserver.exception.*;
import com.zmy.zmyserver.modle.User;
import com.zmy.zmyserver.service.UserService;
import com.zmy.zmyserver.util.EMailUtil;
import com.zmy.zmyserver.util.PasswordUtil;
import com.zmy.zmyserver.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EMailUtil emailUtil;


    @Override
    public String login(String email, String password) {

        User userDb = userMapper.selectByEmail(email);
        if (userDb == null) {
            log.error(ErrorEnum.USER_NOT_EXISTS.desc());
            throw new LoginException();
        }

        if (!userDb.getPassword().equals(PasswordUtil.md5Password(password))) {
            log.error(ErrorEnum.USER_PASSWORD_ERROR.desc());
            throw new LoginException();
        }

        String token = TokenUtil.newToken();
        String key = "token." + token;
        redisTemplate.opsForValue().set(key, JSON.toJSONString(userDb));
        redisTemplate.expire(key, 60, TimeUnit.MINUTES);
        return token;
    }

    @Override
    public boolean register(String email, String username, String password) {

        User user = userMapper.selectByEmail(email);
        if (user != null) {
            throw new UserExistsException();
        }

        int count = userMapper.registerUser(email, username, PasswordUtil.md5Password(password));
        log.info("insert user count : {}", count);

        return true;
    }


    @Override
    public boolean recoverPassword(String password, String forgotToken) {
        String key = "forgot." + forgotToken;
        String email = redisTemplate.opsForValue().get(key);
        if (email == null) {
            throw new ForgotTokenException();
        }
        User user = userMapper.selectByEmail(email);
        if (user == null) {
            throw new UserNotExistsException();
        }

        int count = userMapper.recoverPassword(email, PasswordUtil.md5Password(password));

        log.info("recover password rows : {}", count);
        return true;
    }

    @Override
    public boolean forgotPassword(String email) {
        User userDb = userMapper.selectByEmail(email);
        if (userDb == null) {
            log.error(ErrorEnum.USER_NOT_EXISTS.desc());
            throw new UserNotExistsException();
        }
        return emailUtil.forgotPassword(userDb);
    }

    @Override
    public boolean changePassword(String email, String password) {
        User userDb = userMapper.selectByEmail(email);
        if (userDb == null) {
            log.error(ErrorEnum.USER_NOT_EXISTS.desc());
            throw new UserNotExistsException();
        }
        int count = userMapper.recoverPassword(email, PasswordUtil.md5Password(password));

        log.info("recover password rows : {}", count);
        return true;
    }

    @Override
    public boolean logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        String key = "token." + token;
        try {
            Boolean delete = redisTemplate.delete(key);
            if (Boolean.TRUE.equals(delete)) {
                return true;
            }

            log.error("delete token failed , token is : {}", token);
            throw new LogoutException();

        } catch (Exception exception) {
            log.error("delete token failed , token is : {}", token);
            log.error("delete token exception stack", exception);
            throw new LogoutException();
        }

    }

    @Override
    public User userinfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        String key = "token." + token;
        String userinfoStr = redisTemplate.opsForValue().get(key);
        if (userinfoStr == null) {
            log.error("token expiration at get userinfo, token : {}", token);
            throw new TokenExpirationException();
        }
        User user = JSON.parseObject(userinfoStr, User.class);
        return userMapper.selectByEmail(user.getEmail());
    }

    public List<User> users() {
        List<User> users = userMapper.selectAll();
        log.info("select all user , user count : {}", users);
        return users;
    }


    @Override
    public boolean modifyUserInfo(User user, HttpServletRequest request) {
        String token = request.getHeader("token");
        String key = "token." + token;
        String userinfo = redisTemplate.opsForValue().get(key);
        User userCache = JSON.parseObject(userinfo, User.class);
        user.setEmail(userCache.getEmail());
        user.setUpdateTime(new Date());
        int count = userMapper.modifyUser(user);
        log.info("modify userinfo count : {}", count);
        return true;
    }


    @Override
    public String upload(MultipartFile uploadFile, HttpServletRequest request) {
        String token = request.getHeader("token");
        String key = "token." + token;
        String userinfo = redisTemplate.opsForValue().get(key);
        User user = JSON.parseObject(userinfo, User.class);

        String originalFilename = uploadFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = user.getEmail() + suffix;

        String dir = System.getProperty("user.dir");
        String path = dir + File.separator + "static" + File.separator + fileName;
        String requestPath = "/zmy/static/" + fileName;
        try {
            FileUtil.writeFromStream(uploadFile.getInputStream(), path);
            userMapper.updateHeadImage(user.getEmail(), requestPath);
        } catch (IOException e) {
            log.info("user head image upload failed", e);
            throw new HeadImageUploadException();
        }
        return requestPath;
    }
}
