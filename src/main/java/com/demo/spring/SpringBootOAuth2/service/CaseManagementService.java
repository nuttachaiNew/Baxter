package com.demo.spring.SpringBootOAuth2.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;
import java.util.List;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.demo.spring.SpringBootOAuth2.domain.app.CaseManagement;
import org.apache.poi.xssf.usermodel.*;


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
    Long submitToASM(String json,MultipartHttpServletRequest multipartHttpServletRequest);
    CaseManagement findByCaseNumber(String caseNumber);
    // void approveCaseByRole();
    List<Map<String,Object>> findHistoryDocByAreaAndDocStatusAndRoleAndCase(String createdBy,Long areaId,String documentStatus,String roleBy,String actionUser , String actionDate);
    List<Map<String,Object>> findCaseByCriteria(String date, String caseNumber , String areaId ,String documentStatus ,Integer firstResult ,Integer maxResult,String caseType,String name);
    List<Map<String,Object>> findCaseforReturnCaseByCustomer(String caseType,String customer,String caseNumber);
    

    Map<String,Object> rejectFromAsm(MultipartHttpServletRequest multipartHttpServletRequest);
    Map<String,Object> approveFromAsm(MultipartHttpServletRequest multipartHttpServletRequest);
    void saveFromASM(MultipartHttpServletRequest multipartHttpServletRequest);


    Map<String,Object> saveReturnCase(String json,MultipartHttpServletRequest multipartHttpServletRequest);
    Map<String,Object> sendReturnCaseToNextRole(String json,MultipartHttpServletRequest multipartHttpServletRequest);
   
    Map<String,Object> saveChangeCase(String json,MultipartHttpServletRequest multipartHttpServletRequest);
    List<Map<String,Object>> findCaseManagementforChangeMachineByCriteria(String keyword, String customerType,String hospitalName);

    Map<String,Object> confirmByBU(String json);
    Map<String,Object> confirmByTS(String json);
    Map<String,Object> confirmByFN(String json);
    Map<String,Object> confirmByCS(String json);
    // Map<String,Object> mockSaveChangeCase(String json,MultipartHttpServletRequest multipartHttpServletRequest);
    void changeCaseToASM(String json,MultipartHttpServletRequest multipartHttpServletRequest);
    


    
    List<Map<String,Object>> findCaseByCriteriaforTS(String date, String caseNumber , String areaId ,String documentStatus ,Integer firstResult ,Integer maxResult,String username);
    List<Map<String,Object>> findCaseByCriteriaforFN(String date, String caseNumber , String areaId ,String documentStatus ,Integer firstResult ,Integer maxResult,String username);
    List<Map<String,Object>> findCaseByCriteriaforCS(String date, String caseNumber , String areaId ,String documentStatus ,Integer firstResult ,Integer maxResult,String username);
    List<Map<String,Object>> findCaseByCriteriaforBU(String date, String caseNumber , String areaId ,String documentStatus ,Integer firstResult ,Integer maxResult,String username);
   

    Map<String,Object>  countCaseShowInDashboard(String startDate, String endDate,String createdBy,String areaId);
   
    List<Map<String,Object>>  countCaseOverAll(String caseStatus,String startDate, String endDate,String areaId);

    // List<Map<String,Object>>  getCaseDetailShowInDashboard(String caseStatus,String startDate, String endDate,String createdBy,String areaId,String caseType);
    List<Map<String,Object>>  getCaseDetailShowInDashboard(String caseStatus,String startDate, String endDate,String areaId,String caseType);
    void uploadDigitalSignature(String json,MultipartFile file);

    XSSFWorkbook downloadFormInstallation(Long id);
    XSSFWorkbook downloadFormPrescription(Long id);
    XSSFWorkbook downloadFormReceipt(Long id);

    void sendDeposit(String json,MultipartFile file);
    List<Map<String,Object>> listDepositFn(String createdBy);
    List<Map<String,Object>> listDepositTS(String createdBy);
    void uploadFile(String json,MultipartFile file);
    List<Map<String,Object>> listSwapMachine(String createdBy);
    void returnMachine(String json);
    String getPayslipById(Long id);
}   

