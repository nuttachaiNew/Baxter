package com.demo.spring.SpringBootOAuth2.controller;

import com.demo.spring.SpringBootOAuth2.domain.app.CaseManagement;
import com.demo.spring.SpringBootOAuth2.repository.CaseManagementRepository;
import com.demo.spring.SpringBootOAuth2.service.CaseManagementService;
import com.demo.spring.SpringBootOAuth2.service.UserService;

import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.*;
import org.apache.commons.io.IOUtils;
import org.springframework.security.crypto.codec.Base64;

@RestController
@CrossOrigin
@RequestMapping("/files")
public class FileController {

    @Autowired
    private CaseManagementService caseManagementService;

    @Autowired
    UserService userService;

    @Autowired
    private CaseManagementRepository caseManagementRepository;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/downloadFileByCaseIdAndFileType",method = RequestMethod.GET,headers = "Accept=application/json")
    ResponseEntity<String> downloadFileByCaseIdAndFileType(@RequestParam(value = "caseId",required = false)String id,
                                                           @RequestParam(value = "fileType",required = false)String fileType,
                                                           HttpServletResponse response)throws ServletException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        InputStream in = null;
        try {
            in = caseManagementService.downloadFileByCaseIdAndFileType(Long.valueOf(id),fileType);
            IOUtils.copy(in, response.getOutputStream());
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        }catch (Exception e) {
            LOGGER.error("ERROR : {}",e);
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.OK);
        }finally{
            IOUtils.closeQuietly(in);
        }
    }

    @RequestMapping(value = "/downloadFile",method = RequestMethod.GET,headers = "Accept=application/json")
    ResponseEntity<String> downloadFile(@RequestParam(value = "caseId",required = false)String id,
                                                           @RequestParam(value = "fileType",required = false)String fileType,
                                                           HttpServletResponse response)throws ServletException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        // headers.add("Content-Type","image/*");

        InputStream in = null;
        try {
            response.setContentType("image/*");
            in = caseManagementService.downloadFileByCaseIdAndFileType(Long.valueOf(id),fileType);
            // IOUtils.copy(in, response.getOutputStream());

            byte[] bytes= IOUtils.toByteArray(in);
            byte[] encoded= Base64.encode(bytes);
            String encodedString = new String(encoded);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(encodedString),headers,HttpStatus.OK);
        }catch (Exception e) {
            LOGGER.error("ERROR : {}",e);
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.OK);
        }finally{
            IOUtils.closeQuietly(in);
        }
    }
   

    @RequestMapping(value = "/downloadFileUser",method = RequestMethod.GET,headers = "Accept=application/json")
    ResponseEntity<String> downloadFileUser(@RequestParam(value = "username",required = false)String username,
                                                            HttpServletResponse response)throws ServletException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        InputStream in = null;
        try {
            in = userService.downloadFileUser(username);
            IOUtils.copy(in, response.getOutputStream());
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        }catch (Exception e) {
            LOGGER.error("ERROR : {}",e);
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.OK);
        }finally{
            IOUtils.closeQuietly(in);
        }
    }

    @RequestMapping(value = "/downloadDigitalSignatureFileUser",method = RequestMethod.GET,headers = "Accept=application/json")
    ResponseEntity<String> downloadDigitalSignatureFileUser(@RequestParam(value = "username",required = false)String username,
                                                            HttpServletResponse response)throws ServletException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            String encodedString = userService.downloadDigitalSignatureFileUser(username);
            // return new ResponseEntity<String>(headers, HttpStatus.OK);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(encodedString),headers,HttpStatus.OK);
            
        }catch (Exception e) {
            LOGGER.error("ERROR : {}",e);
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getImageUser",method = RequestMethod.GET,headers = "Accept=application/json")
    ResponseEntity<String> getImageUser(@RequestParam(value = "username",required = false)String username,
                                                            HttpServletResponse response)throws ServletException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","image/*");
        try {
           String encodedString = userService.getImageUser(username);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(encodedString),headers,HttpStatus.OK);
        }catch (Exception e) {
            LOGGER.error("ERROR : {}",e);
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.OK);
        }
    }

}