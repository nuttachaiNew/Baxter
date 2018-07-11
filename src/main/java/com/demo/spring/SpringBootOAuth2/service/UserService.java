package com.demo.spring.SpringBootOAuth2.service;

import com.demo.spring.SpringBootOAuth2.domain.app.User;

import java.util.Map;

public interface UserService {

    Map<String,String> saveUser(String json);
    Map<String,String> updateUser(String json);
    Map<String,String> deleteUser(String json);
    User findUserByUsername(String userName);
}
