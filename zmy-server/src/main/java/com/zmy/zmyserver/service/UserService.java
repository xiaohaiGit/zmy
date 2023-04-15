package com.zmy.zmyserver.service;

import com.zmy.zmyserver.modle.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    String login(String email, String password);

    boolean register(String email, String username, String password);

    boolean recoverPassword(String password, String forgotToken);

    boolean forgotPassword(String email);

    boolean changePassword(String email, String password);

    boolean logout(HttpServletRequest request);

    User userinfo(HttpServletRequest request);

    List<User> users();

    boolean modifyUserInfo(User user, HttpServletRequest request);

    String upload(MultipartFile uploadFile, HttpServletRequest req);


}