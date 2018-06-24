package com.demo.spring.SpringBootOAuth2.service.impl;

import com.demo.spring.SpringBootOAuth2.domain.app.Machine;
import com.demo.spring.SpringBootOAuth2.domain.app.Role;
import com.demo.spring.SpringBootOAuth2.repository.RoleRepository;
import com.demo.spring.SpringBootOAuth2.service.RoleService;
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

@Service
public class RoleServiceImpl implements RoleService{

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RoleRepository roleRepository;

    JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            return json == null ? null : new Date(json.getAsLong());
        }
    };

    protected Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapter(Date.class, deser).create();

    @Override
    public Map<String,String> saveRole(String json) {
        Map<String,String> result = new HashMap<>();
        try{
            LOGGER.info("Save Role");
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);
            Role role = mapper.readValue(jsonObject.toString(),Role.class);
            roleRepository.saveAndFlush(role);
            result.put("code",role.getCode());
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> updateRole(String json) {
        Map<String,String> result = new HashMap<>();
        try{
            LOGGER.info("Update Role");
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);
            Role newRole = mapper.readValue(jsonObject.toString(),Role.class);
            Role oldRole = roleRepository.findOne(newRole.getId());
            newRole = mapper.readerForUpdating(oldRole).readValue(gson.toJson(newRole));

            roleRepository.saveAndFlush(newRole);
            result.put("code",newRole.getCode());
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String,String> deleteRole(String json) {
        Map<String,String> result = new HashMap<>();
        try{
            LOGGER.info("Delete Role");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("id");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    Long id = Long.valueOf(jsonArray.get(i).toString());
                    Role role = roleRepository.findOne(id);
                    if(role != null){
                        roleRepository.delete(role);
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
    public List<Role> findAllRole() {
        try{
            LOGGER.info("Find All Role");
            return roleRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Role findRoleById(Long id) {
        try{
            LOGGER.info("Find Role By Id");
            return roleRepository.findOne(id);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Role> findRoleByName(String name) {
        try{
            LOGGER.info("Find Role By Name");
            return roleRepository.findByNameLike(name);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Role> findRoleByCode(String code) {
        try{
            LOGGER.info("Find Role By Code");
            return roleRepository.findByCodeLike(code);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }
    }
}
