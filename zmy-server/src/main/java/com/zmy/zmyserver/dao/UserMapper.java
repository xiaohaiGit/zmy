package com.zmy.zmyserver.dao;

import com.zmy.zmyserver.modle.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> selectAll();

    User selectByEmail(String email);

    int recoverPassword(String email, String password);


    int registerUser(String email, String username, String password);


    int updateHeadImage(String email, String path);

    int modifyUser(User user);

}
