package com.demo.spring.SpringBootOAuth2.service.impl;

import com.demo.spring.SpringBootOAuth2.domain.app.Branch;
import com.demo.spring.SpringBootOAuth2.domain.app.Role;
import com.demo.spring.SpringBootOAuth2.domain.app.User;
import com.demo.spring.SpringBootOAuth2.repository.BranchRepository;
import com.demo.spring.SpringBootOAuth2.repository.RoleRepository;
import com.demo.spring.SpringBootOAuth2.repository.UserRepository;
import com.demo.spring.SpringBootOAuth2.service.UserService;
import com.demo.spring.SpringBootOAuth2.util.StandardUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.security.MessageDigest;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.lang.reflect.Type;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.lang.StringBuilder;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import org.apache.commons.io.IOUtils;
import org.springframework.security.crypto.codec.Base64;

@Service
public class UserServiceImpl implements UserService{

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static final String PATH_FILE = "/home/me/devNew/img/user/";
    private static final String IPSERVER = "http://58.181.168.159:8082/files/downloadFileUser?username=";


    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BranchRepository branchRepository;

    JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            return json == null ? null : new Date(json.getAsLong());
        }
    };

    public static String encodeSha256(String message){
        try{
          MessageDigest md = MessageDigest.getInstance("SHA-256");
          md.update(message.getBytes());
          String hash = bytesToHex(md.digest());
          return hash;
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    } 

    @Override
    public InputStream downloadFileUser(String username){
        InputStream inputStream =null;
        try {
            String fileName = "IMG_"+username;
            String pathFile =PATH_FILE;
            inputStream = new FileInputStream(pathFile+fileName);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
        return inputStream;
    }

    @Override
    public String getImageUser(String username){
        InputStream inputStream=null;
        try{

            String encodeImage = "";
            String fileName = "IMG_"+username;
            String pathFile =PATH_FILE;
            inputStream = new FileInputStream(pathFile+fileName);
            byte[] bytes= IOUtils.toByteArray(inputStream);
            byte[] encoded= Base64.encode(bytes);
            encodeImage = new String(encoded);
            return encodeImage;
        }catch(Exception e){
             e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            return null;
        }
    }


    public static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    protected Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapter(Date.class, deser).create();

    @Override
    public Map<String, String> saveUser(String json) {
        Map<String,String> result = new HashMap<>();
        try{
            LOGGER.info("Save User");
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);

            User user = mapper.readValue(jsonObject.toString(),User.class);
            LOGGER.info("USER   :   {}",user);
            Role role = roleRepository.findOne(user.getRole().getId());
            Branch branch = branchRepository.findOne(user.getBranch().getId());
            LOGGER.info("Password   :   {}",user.getAccessToken());

            String password = encodeSha256(user.getAccessToken());

            user.setRole(role);
            user.setBranch(branch);
            user.setAccessToken(password);

            userRepository.saveAndFlush(user);
            result.put("username",user.getUsername());
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> updateUser(String json) {
        Map<String,String> result = new HashMap<>();
        try{
            LOGGER.info("Update User");
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);
            User newUser = mapper.readValue(jsonObject.toString(),User.class);
            User oldUser = userRepository.findOne(newUser.getId());

            Role role = roleRepository.findOne(newUser.getRole().getId());
            Branch branch = branchRepository.findOne(newUser.getBranch().getId());

            oldUser.setRole(null);
            oldUser.setBranch(null);

            String oldPassword = oldUser.getAccessToken();
            newUser = mapper.readerForUpdating(oldUser).readValue(gson.toJson(newUser));

            String newPassword = newUser.getAccessToken();
            String newPasswordMD5 = encodeSha256(newPassword);

            newUser.setRole(role);
            newUser.setBranch(branch);

            if(!newPassword.equals(oldPassword)){
                newUser.setAccessToken(newPasswordMD5);
            }

            userRepository.saveAndFlush(newUser);
            result.put("username",newUser.getUsername());
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> deleteUser(String json) {
        Map<String,String> result = new HashMap<>();
        try{
            LOGGER.info("Delete User");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("id");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    Long id = Long.valueOf(jsonArray.get(i).toString());
                    User user = userRepository.findOne(id);
                    if(user != null){
                        userRepository.delete(user);
                    }
                }
            }
            result.put("msg","delete "+String.valueOf(jsonArray.length())+" success");
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findUserByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public  void updateProfile(String json,MultipartHttpServletRequest multipartHttpServletRequest){
        try{
            LOGGER.info("updateProfile :{}",json);
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);
            User updateUser = new JSONDeserializer<User>().use(null, User.class).deserialize(jsonObject.toString());
            User user = findUserByUsername(updateUser.getUsername());
            user.setUpdatedBy(updateUser.getUpdatedBy());
            user.setEmail(updateUser.getEmail());
            user.setTelephoneNumber(updateUser.getTelephoneNumber());
            user.setFirstName(updateUser.getFirstName());
            user.setLastName(updateUser.getLastName());
           if(jsonObject.get("newPassword")!=null  && !"".equalsIgnoreCase(jsonObject.get("newPassword").toString())  ){
              String oldPassword = user.getAccessToken();
              if( !oldPassword.equalsIgnoreCase(updateUser.getAccessToken()) ){
                throw new RuntimeException("Password incorrect");
              }
              String newPassword = jsonObject.get("newPassword").toString();
              String newPasswordMD5 = encodeSha256(newPassword);
              user.setAccessToken(newPasswordMD5);
            }
            MultipartFile images = multipartHttpServletRequest.getFile("images");
            if(images!=null){
                byte[] bytes = images.getBytes();
                String FileName = "IMG_"+user.getUsername();
                FileCopyUtils.copy(bytes, new FileOutputStream(PATH_FILE+FileName));
                user.setImage(IPSERVER+user.getUsername());
            }

            userRepository.saveAndFlush(user);

        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }
    }

     @Override
    public  void updateProfileWeb(String json,MultipartFile images){
        try{
            LOGGER.info("updateProfileWeb :{}",json);
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);
            User updateUser = new JSONDeserializer<User>().use(null, User.class).deserialize(jsonObject.toString());
            User user = findUserByUsername(updateUser.getUsername());
            user.setUpdatedBy(updateUser.getUpdatedBy());
            user.setEmail(updateUser.getEmail());
            user.setTelephoneNumber(updateUser.getTelephoneNumber());
            user.setFirstName(updateUser.getFirstName());
            user.setLastName(updateUser.getLastName());
            if(jsonObject.get("newPassword")!=null  && !"".equalsIgnoreCase(jsonObject.get("newPassword").toString())  ){
              String oldPassword = user.getAccessToken();
              if( !oldPassword.equalsIgnoreCase(updateUser.getAccessToken()) ){
                throw new RuntimeException("Password incorrect");
              }
              String newPassword = jsonObject.get("newPassword").toString();
              String newPasswordMD5 = encodeSha256(newPassword);
              user.setAccessToken(newPasswordMD5);
            }
            // MultipartFile images = multipartHttpServletRequest.getFile("images");
            if(images!=null){
                byte[] bytes = images.getBytes();
                String FileName = "IMG_"+user.getUsername();
                FileCopyUtils.copy(bytes, new FileOutputStream(PATH_FILE+FileName));
                user.setImage(IPSERVER+user.getUsername());
            }

            userRepository.saveAndFlush(user);

        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }
    }
}
