package com.demo.spring.SpringBootOAuth2.controller;

import com.demo.spring.SpringBootOAuth2.domain.app.Role;
import com.demo.spring.SpringBootOAuth2.service.RoleService;
import com.demo.spring.SpringBootOAuth2.service.RoleMenuService;
import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
// @RequestMapping("/roles")
public class RoleController {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RoleService roleService;

    @Autowired
    RoleMenuService roleMenuService;

    @RequestMapping(value ="/save", method = RequestMethod.POST)
    public ResponseEntity<String> saveRole(@RequestBody String json){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            Map<String,String> result = roleService.saveRole(json);
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(result), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value ="/update", method = RequestMethod.POST)
    public ResponseEntity<String> updateRole(@RequestBody String json){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            Map<String,String> result = roleService.updateRole(json);
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(result), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value ="/delete", method = RequestMethod.POST)
    public ResponseEntity<String> deleteRole(@RequestBody String json){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            Map<String,String> result = roleService.deleteRole(json);
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(result), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value ="/findAllRole", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> findAllRole(){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            List<Role> roleList = roleService.findAllRole();
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(roleList), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }

    }

    @RequestMapping(value ="/findRoleById", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> findRoleById(@RequestParam(value = "id", required = true) String id){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            Role role = roleService.findRoleById(Long.valueOf(id));
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(role), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }

    }

    @RequestMapping(value ="/findRoleByName", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> findRoleByName(@RequestParam(value = "name", required = true) String name){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            List<Role> roleList = roleService.findRoleByName(name);
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(roleList), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }

    }

    @RequestMapping(value ="/findRoleByCode", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> findRoleByCode(@RequestParam(value = "code", required = true) String code){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            List<Role> roleList = roleService.findRoleByCode(code);
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(roleList), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }

    }


    @RequestMapping(value ="/findMenuByRoleId", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> findMenuByRoleId(@RequestParam(value = "id", required = true) Long id){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            List<Map<String,Object>>results = roleMenuService.findByRoleId(id);
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(results), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }

   @RequestMapping(value ="/getAllRoleMenu", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> getAllRoleMenu(){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            List<Map<String,Object>>results = roleMenuService.findAll();
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(results), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }


}
