package com.demo.spring.SpringBootOAuth2.controller;

import com.demo.spring.SpringBootOAuth2.domain.RandomCity;
import com.demo.spring.SpringBootOAuth2.domain.User;
import com.demo.spring.SpringBootOAuth2.service.GenericService;
import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/springjwt")
public class ResourceController {
    @Autowired
    private GenericService userService;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value ="/cities")
    @PreAuthorize("hasAuthority('ADMIN_USER') or hasAuthority('STANDARD_USER')")
    public List<RandomCity> getUser(){
        return userService.findAllRandomCities();
    }

    @RequestMapping(value ="/users", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    public List<User> getUsers(){
        return userService.findAllUsers();
    }

    @RequestMapping(value ="/yourData", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ADMIN_USER') or hasAuthority('STANDARD_USER')")
    public ResponseEntity<String> getYourData(Authentication authentication){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json; charset=utf-8;");
        try{
            User user = userService.findByUsername(authentication.getName());

            LOGGER.info("user info");
            LOGGER.info("\n{}",new JSONSerializer().prettyPrint(true).exclude("class").exclude("username").exclude("password").serialize(user));

            return new ResponseEntity<>(new JSONSerializer()
                    .prettyPrint(true)
                    .exclude("class")
                    .exclude("username")
                    .exclude("password")
                    .serialize(user),headers,HttpStatus.OK);
        }catch (Exception e) {
            LOGGER.info("error");
            return new ResponseEntity<>(new JSONSerializer().prettyPrint(true).deepSerialize("{ status : error }"), headers, HttpStatus.OK);
        }
    }
}

