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
import org.apache.commons.codec.binary.Base64;
import java.security.MessageDigest;

@Service
public class UserServiceImpl implements UserService{

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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
            LOGGER.info("Password   :   {}",user.getPassword());

            String password = encodeSha256(user.getPassword());

            user.setRole(role);
            user.setBranch(branch);
            user.setPassword(password);

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

            String oldPassword = oldUser.getPassword();
            newUser = mapper.readerForUpdating(oldUser).readValue(gson.toJson(newUser));

            String newPassword = newUser.getPassword();
            String newPasswordMD5 = encodeSha256(newPassword);

            newUser.setRole(role);
            newUser.setBranch(branch);

            if(!newPassword.equals(oldPassword)){
                newUser.setPassword(newPasswordMD5);
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
}
