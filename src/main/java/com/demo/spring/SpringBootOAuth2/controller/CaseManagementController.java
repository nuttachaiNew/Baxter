package com.demo.spring.SpringBootOAuth2.controller;

import com.demo.spring.SpringBootOAuth2.domain.app.CaseManagement;
import com.demo.spring.SpringBootOAuth2.repository.CaseManagementRepository;
import com.demo.spring.SpringBootOAuth2.service.CaseManagementService;
import flexjson.JSONSerializer;
import org.apache.tomcat.util.http.fileupload.IOUtils;
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

@RestController
@RequestMapping("/casemanagement")
public class CaseManagementController {

    @Autowired
    private CaseManagementService caseManagementService;

    @Autowired
    private CaseManagementRepository caseManagementRepository;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value ="/findCaseManagementById", method = RequestMethod.GET)
    ResponseEntity<String> findCaseManagementById(@RequestParam(value = "id",required = false)String id){


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            CaseManagement caseManagement = caseManagementRepository.findOne(Long.valueOf(id));
            headers.add("errorStatus", "N");
            headers.add("errsg", null);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(caseManagement), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/uploadfileByCaseIdAndFileType", method = RequestMethod.POST, headers = "content-type=multipart/*")
    public ResponseEntity<String> uploadfileByCaseIdAndFileType(@RequestParam(value = "caseId",required = false)String id,
                                                                @RequestParam(value = "fileType",required = false)String fileType,
                                                                MultipartHttpServletRequest multipartHttpServletRequest, HttpServletRequest request) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        LOGGER.info("[uploadfileByCaseIdAndFileType][Controller] user:{} ",request.getHeader("USER"));
        try{
            String user = request.getHeader("USER");
            MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
            String name = multipartHttpServletRequest.getParameter("name");
            String status = caseManagementService.uploadfileByCaseIdAndFileType(name,multipartFile,Long.valueOf(id),fileType,user);
            if("OK".equals(status)){
                return new ResponseEntity<String>(headers, HttpStatus.OK);
            }else{
                return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("CONTROLLER CATCH");
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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

}