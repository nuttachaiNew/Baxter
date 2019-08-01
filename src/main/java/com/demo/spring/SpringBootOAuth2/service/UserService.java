package com.demo.spring.SpringBootOAuth2.service;

import com.demo.spring.SpringBootOAuth2.domain.app.User;

import java.util.Map;
import java.util.List;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


public interface UserService {

    Map<String,String> saveUser(String json);
    Map<String,String> updateUser(String json);
    Map<String,String> deleteUser(String json);
    User findUserByUsername(String userName);
    void updateProfile(String json,MultipartHttpServletRequest multipartHttpServletRequest);
    void updateProfileWeb(String json,MultipartFile multipartHttpServletRequest);
    String downloadDigitalSignatureFileUser(String username);
    
    InputStream downloadFileUser(String username);
    String getImageUser(String username);
    List<User> findAllUser();
}
