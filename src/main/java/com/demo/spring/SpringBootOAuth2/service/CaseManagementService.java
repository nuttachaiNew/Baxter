package com.demo.spring.SpringBootOAuth2.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;
import java.util.List;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.demo.spring.SpringBootOAuth2.domain.app.CaseManagement;


public interface CaseManagementService {
    String uploadfileByCaseIdAndFileType(String name, MultipartFile multipartFile,Long caseId,String fileTpye,String user);
    InputStream downloadFileByCaseIdAndFileType(Long caseId,String fileTpye);
    Map<String,Object> saveCase(String json,MultipartHttpServletRequest multipartHttpServletRequest);
    Map<String,Object> uploadTest(MultipartHttpServletRequest multipartHttpServletRequest);
    
    String generateCaseNumber(String caseType);
    Long autoGenerateMachineByTypeAndStatusEqActive(String machineType,String modelRef,String serialNumber);
    void updateMachineStatus(Long machineId ,Integer status,String caseNumber,String actionBy); 
    Map<String,Object> updateCase(String json,MultipartHttpServletRequest multipartHttpServletRequest);
    // void submitToASM(Long id,String updBy);
    void submitToASM(String json,MultipartHttpServletRequest multipartHttpServletRequest);
    CaseManagement findByCaseNumber(String caseNumber);
    // void approveCaseByRole();
    List<Map<String,Object>> findHistoryDocByAreaAndDocStatusAndRoleAndCase(String createdBy,Long areaId,String documentStatus,String roleBy);
    List<Map<String,Object>> findCaseByCriteria(String date, String caseNumber , String areaId ,String documentStatus ,Integer firstResult ,Integer maxResult);
    List<Map<String,Object>> findCaseforReturnCaseByCustomer(String caseType,String customer,String caseNumber);
    

    Map<String,Object> rejectFromAsm(MultipartHttpServletRequest multipartHttpServletRequest);
    Map<String,Object> approveFromAsm(MultipartHttpServletRequest multipartHttpServletRequest);
    void saveFromASM(MultipartHttpServletRequest multipartHttpServletRequest);


    // Map<String,Object> saveReturnCase(String json,MultipartHttpServletRequest multipartHttpServletRequest);
    Map<String,Object> saveChangeCase(String json,MultipartHttpServletRequest multipartHttpServletRequest);
    List<Map<String,Object>> findCaseManagementforChangeMachineByCriteria(String keyword, String customerType,String hospitalName);


    Map<String,Object> confirmByTS(String json,MultipartHttpServletRequest multipartHttpServletRequest);
    Map<String,Object> confirmByFN(String json,MultipartHttpServletRequest multipartHttpServletRequest);
    Map<String,Object> confirmByCS(String json,MultipartHttpServletRequest multipartHttpServletRequest);
    Map<String,Object> mockSaveChangeCase(String json,MultipartHttpServletRequest multipartHttpServletRequest);

}   

