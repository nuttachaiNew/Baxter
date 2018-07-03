package com.demo.spring.SpringBootOAuth2.service;

import java.util.Map;

public interface UserService {

    Map<String,String> saveUser(String json);
    Map<String,String> updateUser(String json);
    Map<String,String> deleteUser(String json);
}
