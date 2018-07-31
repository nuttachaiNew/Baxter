package com.demo.spring.SpringBootOAuth2.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

public interface CaseManagementService {
    String uploadfileByCaseIdAndFileType(String name, MultipartFile multipartFile,Long caseId,String fileTpye,String user);
    InputStream downloadFileByCaseIdAndFileType(Long caseId,String fileTpye);
    Map<String,Object> saveCase(String json);
    String generateCaseNumber(String caseType);
    Long autoGenerateMachineByTypeAndStatusEqActive(String machineType,String modelRef);
    void updateMachineStatus(Long machineId ,Integer status,String caseNumber,String actionBy); 
}

