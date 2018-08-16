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
    // Map<String,Object> updateCase(String json);
    void submitToASM(Long id,String updBy);
    CaseManagement findByCaseNumber(String caseNumber);
    // void approveCaseByRole();
    List<Map<String,Object>> findHistoryDocByAreaAndDocStatusAndRoleAndCase(String createdBy,Long areaId,String documentStatus,String roleBy);
    List<Map<String,Object>> findCaseByCriteria(String date, String caseNumber , String areaId ,String documentStatus ,Integer firstResult ,Integer maxResult);
    List<Map<String,Object>> findCaseforReturnCaseByCustomer(String caseType,String customer,String caseNumber);

}

