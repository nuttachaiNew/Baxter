package com.demo.spring.SpringBootOAuth2.controller;


import com.demo.spring.SpringBootOAuth2.domain.app.User;
import com.demo.spring.SpringBootOAuth2.service.UserService;
import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.util.List;
import java.util.Map;
import java.util.*;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@RestController
@CrossOrigin
@RequestMapping("/api/users")
// @RequestMapping("/users")
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

    @RequestMapping(value ="/findUserByUserName", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> findUserByUserName(@RequestParam(value = "username", required = true) String userName){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            User user = userService.findUserByUsername(userName);
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(user), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }

    }

    @RequestMapping(value ="/findAllUser", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> findAllUser(){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            List<User> user = userService.findAllUser();
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(user), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }

    }




    @RequestMapping(value ="/updateProfile", method = RequestMethod.POST ,produces = "text/html", headers = "Accept=application/json")
    public ResponseEntity<String> updateProfile(
        MultipartHttpServletRequest multipartHttpServletRequest
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("updateProfile : {}",multipartHttpServletRequest.getParameter("json"));
            userService.updateProfile(multipartHttpServletRequest.getParameter("json"),multipartHttpServletRequest);
            Map<String,Object> result = new HashMap<>();
            result.put("status","success");
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(result), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
             Map<String,Object> result = new HashMap<>();
             result.put("status","error");
             result.put("errorMsg",ex.getMessage());
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(result), headers, HttpStatus.OK);
        }
    }


    @PostMapping("/updateProfileWeb")
    public ResponseEntity<String> updateProfileWeb(
//              @RequestParam("file")MultipartFile file,
                @RequestParam(value = "file",required = false)MultipartFile file,

		    @RequestParam("json")String json
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("=================== update profile web========================");
	    userService.updateProfileWeb(json,file);
            Map<String,Object> result = new HashMap<>();
            result.put("status","success");
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(result), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
             Map<String,Object> result = new HashMap<>();
             result.put("status","error");
             result.put("errorMsg",ex.getMessage());
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(result), headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getDigitalSignature",method = RequestMethod.GET,headers = "Accept=application/json")
    ResponseEntity<String> getDigitalSignature(@RequestParam(value = "username",required = false)String username
                                                         ,  HttpServletResponse response)throws ServletException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        InputStream in = null;
        try {
            in = userService.downloadDigitalSignatureFileUser(username);
            IOUtils.copy(in, response.getOutputStream());
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        }catch (Exception e) {
            LOGGER.error("ERROR : {}",e);
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.OK);
        }finally{
            IOUtils.closeQuietly(in);
        }
    }


}
