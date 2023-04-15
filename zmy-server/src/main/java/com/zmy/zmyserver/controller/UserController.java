package com.zmy.zmyserver.controller;

import com.zmy.zmyserver.modle.User;
import com.zmy.zmyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
//@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password);
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean register(@RequestParam String email, @RequestParam String username, @RequestParam String password) {
        return userService.register(email, username, password);
    }

    @PostMapping(value = "/forgot-password", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean forgotPassword(@RequestParam String email) {
        return userService.forgotPassword(email);
    }


    @PostMapping(value = "/recover-password", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean recoverPassword(@RequestParam String password, @RequestParam String forgotToken) {
        return userService.recoverPassword(password, forgotToken);
    }

    @PostMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean changePassword(@RequestParam String email, @RequestParam String password) {
        return userService.changePassword(email, password);
    }

    @GetMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean logout(HttpServletRequest request) {
        return userService.logout(request);
    }


    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User userinfo(HttpServletRequest request) {
        return userService.userinfo(request);
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<User> users() {
        return userService.users();
    }

    @PutMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean modifyUserInfo(@RequestBody User user, HttpServletRequest request) {
        return userService.modifyUserInfo(user, request);
    }


    @PostMapping("/head-image-upload")
    public String upload(@RequestParam("file") MultipartFile uploadFile, HttpServletRequest request) {
        return userService.upload(uploadFile, request);
    }


}
