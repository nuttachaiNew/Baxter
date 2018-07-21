package com.demo.spring.SpringBootOAuth2.service.impl;

import com.demo.spring.SpringBootOAuth2.domain.app.RoleMenu;
import com.demo.spring.SpringBootOAuth2.repository.RoleMenuRepository;
import com.demo.spring.SpringBootOAuth2.repository.RoleMenuRepositoryCustom;

import com.demo.spring.SpringBootOAuth2.service.RoleMenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

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
import java.util.Arrays;
import com.demo.spring.SpringBootOAuth2.domain.app.Role;
import com.demo.spring.SpringBootOAuth2.repository.RoleRepository;

import com.demo.spring.SpringBootOAuth2.domain.app.Menu;
import com.demo.spring.SpringBootOAuth2.repository.MenuRepository;
import com.demo.spring.SpringBootOAuth2.util.StandardUtil;

@Service
public class RoleMenuServiceImpl implements RoleMenuService{

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RoleMenuRepository roleMenuRepository;

    @Autowired
    RoleMenuRepositoryCustom roleMenuRepositoryCustom;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MenuRepository menuRepository;

    @Override
    public void manageRoleMenu(String json){
        LOGGER.info("manageRoleMenu :{}",json);
        try{
            JSONObject jsonObject = new JSONObject(json);
            List<Map<String,Object>> dataList = new JSONDeserializer<List<Map<String,Object>>>().deserialize(jsonObject.toString());
            for(Map<String,Object> data : dataList){
                String roleId = data.get("roleId").toString();
                Role role  = roleRepository.findOne(  Long.valueOf(roleId)  ); 

                List<String> menuList = Arrays.asList(data.get("menus").toString().split(",")) ;
                List<Map<String,Object>> listByRoleId = findByRoleId(Long.valueOf(roleId));
               //  clear Role Menu
                for(Map<String,Object>  menu : listByRoleId){
                    RoleMenu roleMenu = roleMenuRepository.findOne(  Long.valueOf( menu.get("id").toString())  );
                    roleMenuRepository.delete(roleMenu);
                } 

                // save Menu
                for(String  menuData : menuList){
                    Menu menus = menuRepository.findOne( Long.valueOf( menuData  )  );
                    RoleMenu newRoleMenu = new RoleMenu();
                    newRoleMenu.setCreatedDate(StandardUtil.getCurrentDate());
                    newRoleMenu.setCreatedBy("ADM");
                    newRoleMenu.setRole( role );
                    newRoleMenu.setMenu( menus );
                    roleMenuRepository.save(newRoleMenu);
                }

            }
        LOGGER.debug("success ");

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
   public List<Map<String,Object>> findByRoleId(Long roleId){
        LOGGER.info("findByRoleId :{}",roleId);
        try{
            return roleMenuRepositoryCustom.findByRoleId(roleId);
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
     public List<Map<String,Object>> findAll(){
        LOGGER.info("findAll ");
        try{
            return roleMenuRepositoryCustom.findAllRoleMenu();
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
  
}
