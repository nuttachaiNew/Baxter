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
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/api/casemanagement")
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
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").exclude("refCase.*").exclude("caseActivitys.user.branch").exclude("caseActivitys.user.role").exclude("caseActivitys.user.password").exclude("branch.*").exclude("user.*").exclude("role.*").include("caseActivitys.user.id").include("caseActivitys.branch.id").include("refCase.id").deepSerialize(caseManagement), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }


    @RequestMapping(value ="/findCaseManagementByCaseNumber", method = RequestMethod.GET)
    ResponseEntity<String> findCaseManagementByCaseNumber(@RequestParam(value = "caseNumber",required = false)String caseNumber){


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            CaseManagement caseManagement = caseManagementService.findByCaseNumber(caseNumber);
            headers.add("errorStatus", "N");
            headers.add("errsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(caseManagement), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value ="/findCaseManagementforChangeMachineByCriteria", method = RequestMethod.GET)
    ResponseEntity<String> findCaseManagementforChangeMachineByCriteria(@RequestParam(value = "keyword",required = false)String keyword,@RequestParam(value = "customerType",required = false)String customerType ,@RequestParam(value = "hospitalName",required = false)String hospitalName){


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            List<Map<String,Object>> caseManagement = caseManagementService.findCaseManagementforChangeMachineByCriteria(keyword,customerType,hospitalName);
            headers.add("errorStatus", "N");
            headers.add("errsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(caseManagement), headers, HttpStatus.OK);
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
        try{
            String user = null;
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

    @RequestMapping(value ="/submitToASM", method = RequestMethod.POST ,produces = "text/html", headers = "Accept=application/json")
    public ResponseEntity<String> submitToASM(
        MultipartHttpServletRequest multipartHttpServletRequest
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("multipartHttpServletRequest : {}",multipartHttpServletRequest.getParameter("json"));
            caseManagementService.submitToASM(multipartHttpServletRequest.getParameter("json"),multipartHttpServletRequest);
            Map result = new HashMap<>();
            result.put("status","success");
            result.put("caseStatus","W");
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

    @RequestMapping(value ="/saveCase", method = RequestMethod.POST ,produces = "text/html", headers = "Accept=application/json")
    public ResponseEntity<String> saveCase(
        MultipartHttpServletRequest multipartHttpServletRequest
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("multipartHttpServletRequest : {}",multipartHttpServletRequest.getParameter("json"));
            Map<String,Object> result = caseManagementService.saveCase(multipartHttpServletRequest.getParameter("json"),multipartHttpServletRequest);
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

    @RequestMapping(value ="/updateCase", method = RequestMethod.POST ,produces = "text/html", headers = "Accept=application/json")
    public ResponseEntity<String> updateCase(
        MultipartHttpServletRequest multipartHttpServletRequest
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("multipartHttpServletRequest : {}",multipartHttpServletRequest.getParameter("json"));
            Map<String,Object> result = caseManagementService.updateCase(multipartHttpServletRequest.getParameter("json"),multipartHttpServletRequest);
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

    @RequestMapping(value ="/uploadTest", method = RequestMethod.POST ,produces = "text/html", headers = "Accept=application/json")
    public ResponseEntity<String> uploadTest(
        MultipartHttpServletRequest multipartHttpServletRequest
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("multipartHttpServletRequest : {}",multipartHttpServletRequest.getParameter("json"));
            Map<String,Object> result = caseManagementService.uploadTest(multipartHttpServletRequest);
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

    @RequestMapping(value ="/findHistoryDocByAreaAndDocStatusAndRoleAndCase", method = RequestMethod.GET)
    ResponseEntity<String> findHistoryDocByAreaAndDocStatusAndRoleAndCase(@RequestParam(value = "createdBy",required = false)String createdBy
                                                                        , @RequestParam(value = "areaId",required = false)Long areaId
                                                                        , @RequestParam(value = "caseStatus",required = false)String documentStatus
                                                                        , @RequestParam(value = "roleBy",required = false)String roleBy
                                                                        ){


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            List<Map<String,Object>> caseManagement = caseManagementService.findHistoryDocByAreaAndDocStatusAndRoleAndCase(createdBy,areaId,documentStatus,roleBy);
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

   @RequestMapping(value ="/findCaseByCriteria", method = RequestMethod.GET)
    ResponseEntity<String> findCaseByCriteria(                           @RequestParam(value = "date",required = false)String date
                                                                        , @RequestParam(value = "caseNumber",required = false)String caseNumber
                                                                        , @RequestParam(value = "areaId",required = false)String areaId
                                                                        , @RequestParam(value = "caseStatus",required = false)String documentStatus
                                                                        , @RequestParam(value = "firstResult",required = false)Integer firstResult
                                                                        , @RequestParam(value = "maxResult",required = false)Integer maxResult
                                                                        , @RequestParam(value = "caseType",required = false)String caseType
                                                                        
                                                                        ){


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            List<Map<String,Object>> caseManagement = caseManagementService.findCaseByCriteria(date,caseNumber,areaId,documentStatus,firstResult,maxResult,caseType);
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

    @RequestMapping(value ="/findCaseforReturnCaseByCustomer", method = RequestMethod.GET)
    ResponseEntity<String> findCaseforReturnCaseByCustomer(       
                      @RequestParam(value = "caseType",required = false)String caseType
                 ,    @RequestParam(value = "customer",required = false)String customer
                 ,    @RequestParam(value = "caseNumber",required = false)String caseNumber
             ){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            List<Map<String,Object>> caseManagement = caseManagementService.findCaseforReturnCaseByCustomer(caseType,customer,caseNumber);
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


    @RequestMapping(value ="/saveFromASM", method = RequestMethod.POST ,produces = "text/html", headers = "Accept=application/json")
    public ResponseEntity<String> saveFromASM(
        MultipartHttpServletRequest multipartHttpServletRequest
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("multipartHttpServletRequest : {}",multipartHttpServletRequest.getParameter("json"));
           caseManagementService.saveFromASM(multipartHttpServletRequest);
             Map<String,Object> result = new HashMap<>();
            result.put("json",multipartHttpServletRequest.getParameter("json"));
            result.put("status","save Success");
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

   @RequestMapping(value ="/approveFromAsm", method = RequestMethod.POST ,produces = "text/html", headers = "Accept=application/json")
    public ResponseEntity<String> approveFromAsm(
        MultipartHttpServletRequest multipartHttpServletRequest
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("multipartHttpServletRequest : {}",multipartHttpServletRequest.getParameter("json"));
            Map<String,Object> result = caseManagementService.approveFromAsm(multipartHttpServletRequest);
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

    @RequestMapping(value ="/rejectFromAsm", method = RequestMethod.POST ,produces = "text/html", headers = "Accept=application/json")
    public ResponseEntity<String> rejectFromAsm(
        MultipartHttpServletRequest multipartHttpServletRequest
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("multipartHttpServletRequest : {}",multipartHttpServletRequest.getParameter("json"));
            Map<String,Object> result = caseManagementService.rejectFromAsm(multipartHttpServletRequest);
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

    @RequestMapping(value ="/saveChangeCase", method = RequestMethod.POST ,produces = "text/html", headers = "Accept=application/json")
    public ResponseEntity<String> saveChangeCase(
        MultipartHttpServletRequest multipartHttpServletRequest
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("multipartHttpServletRequest : {}",multipartHttpServletRequest.getParameter("json"));
            Map<String,Object> result = caseManagementService.saveChangeCase(multipartHttpServletRequest.getParameter("json"),multipartHttpServletRequest);
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




    @RequestMapping(value ="/confirmByTS", method = RequestMethod.POST ,produces = "text/html", headers = "Accept=application/json")
    public ResponseEntity<String> confirmByTS(
        MultipartHttpServletRequest multipartHttpServletRequest
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("multipartHttpServletRequest : {}",multipartHttpServletRequest.getParameter("json"));
            Map<String,Object> result =  new HashMap<>();
            // caseManagementService.confirmBySale(multipartHttpServletRequest.getParameter("json"),multipartHttpServletRequest);
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



    @RequestMapping(value ="/confirmByFN", method = RequestMethod.POST ,produces = "text/html", headers = "Accept=application/json")
    public ResponseEntity<String> confirmByFN(
        MultipartHttpServletRequest multipartHttpServletRequest
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("multipartHttpServletRequest : {}",multipartHttpServletRequest.getParameter("json"));
            Map<String,Object> result =  new HashMap<>();
            // caseManagementService.confirmByFN(multipartHttpServletRequest.getParameter("json"),multipartHttpServletRequest);
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

    @RequestMapping(value ="/confirmByCS", method = RequestMethod.POST ,produces = "text/html", headers = "Accept=application/json")
    public ResponseEntity<String> confirmByCS(
        MultipartHttpServletRequest multipartHttpServletRequest
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("multipartHttpServletRequest : {}",multipartHttpServletRequest.getParameter("json"));
            Map<String,Object> result =  new HashMap<>();
            // caseManagementService.confirmByFN(multipartHttpServletRequest.getParameter("json"),multipartHttpServletRequest);
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




    @RequestMapping(value ="/mockChangeCase", method = RequestMethod.POST ,produces = "text/html", headers = "Accept=application/json")
    public ResponseEntity<String> mockChangeCase(
        MultipartHttpServletRequest multipartHttpServletRequest
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("multipartHttpServletRequest : {}",multipartHttpServletRequest.getParameter("json"));
            Map<String,Object> result = caseManagementService.mockSaveChangeCase(multipartHttpServletRequest.getParameter("json"),multipartHttpServletRequest);
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



    @RequestMapping(value ="/changeCaseToASM", method = RequestMethod.POST ,produces = "text/html", headers = "Accept=application/json")
    public ResponseEntity<String> changeCaseToASM(
        MultipartHttpServletRequest multipartHttpServletRequest
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("multipartHttpServletRequest : {}",multipartHttpServletRequest.getParameter("json"));
            caseManagementService.changeCaseToASM(multipartHttpServletRequest.getParameter("json"),multipartHttpServletRequest);
            Map result = new HashMap<>();
            result.put("status","success");
            result.put("caseStatus","W");
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


    @RequestMapping(value ="/saveReturnCase", method = RequestMethod.POST ,produces = "text/html", headers = "Accept=application/json")
    public ResponseEntity<String> saveReturnCase(
        MultipartHttpServletRequest multipartHttpServletRequest
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("multipartHttpServletRequest : {}",multipartHttpServletRequest.getParameter("json"));
            Map<String,Object> result = caseManagementService.saveReturnCase(multipartHttpServletRequest.getParameter("json"),multipartHttpServletRequest);
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

   @RequestMapping(value ="/sendReturnCaseToNextRole", method = RequestMethod.POST ,produces = "text/html", headers = "Accept=application/json")
    public ResponseEntity<String> sendReturnCaseToNextRole(
        MultipartHttpServletRequest multipartHttpServletRequest
        ){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            LOGGER.debug("multipartHttpServletRequest : {}",multipartHttpServletRequest.getParameter("json"));
            Map<String,Object> result = caseManagementService.sendReturnCaseToNextRole(multipartHttpServletRequest.getParameter("json"),multipartHttpServletRequest);
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

}