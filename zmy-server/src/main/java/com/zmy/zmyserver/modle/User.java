package com.zmy.zmyserver.modle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private int age;
    private String address;
    private String phone;
    private String weixin;
    private String qq;
    private String headImagePath;
    private Date createTime;
    private Date updateTime;
}
