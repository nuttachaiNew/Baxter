package com.demo.spring.SpringBootOAuth2.controller;


import com.demo.spring.SpringBootOAuth2.service.UserService;
import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @RequestMapping(value ="/save", method = RequestMethod.POST)
    public ResponseEntity<String> saveRole(@RequestBody String json){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.info("JSON   :   {}",json);
            Map<String,String> result = userService.saveUser(json);
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
            Map<String,String> result = userService.updateUser(json);
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
            Map<String,String> result = userService.deleteUser(json);
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
}
