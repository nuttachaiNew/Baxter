package com.demo.spring.SpringBootOAuth2.service.impl;

import com.demo.spring.SpringBootOAuth2.domain.app.CaseManagement;
import com.demo.spring.SpringBootOAuth2.domain.app.FileUpload;
import com.demo.spring.SpringBootOAuth2.domain.app.Parameter;
import com.demo.spring.SpringBootOAuth2.domain.app.Customer;
import com.demo.spring.SpringBootOAuth2.domain.app.ParameterDetail;
import com.demo.spring.SpringBootOAuth2.domain.app.Machine;
import com.demo.spring.SpringBootOAuth2.domain.app.MachineHistory;
import com.demo.spring.SpringBootOAuth2.domain.app.CaseActivity;
import com.demo.spring.SpringBootOAuth2.domain.app.User;
import com.demo.spring.SpringBootOAuth2.domain.app.*;

import com.demo.spring.SpringBootOAuth2.repository.CaseManagementRepository;
import com.demo.spring.SpringBootOAuth2.repository.CustomerRepository;
import com.demo.spring.SpringBootOAuth2.repository.InstallationRepository;

import com.demo.spring.SpringBootOAuth2.repository.FileUploadRepository;
import com.demo.spring.SpringBootOAuth2.service.CaseManagementService;
import com.demo.spring.SpringBootOAuth2.service.FileUploadService;
import com.demo.spring.SpringBootOAuth2.service.ParameterDetailService;
import com.demo.spring.SpringBootOAuth2.service.ParameterService;
import com.demo.spring.SpringBootOAuth2.util.ConstantVariableUtil;
import com.demo.spring.SpringBootOAuth2.util.StandardUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import java.lang.reflect.Type;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.*;
import java.util.ArrayList;
import java.util.Date;
import java.lang.StringBuilder;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.springframework.transaction.annotation.Transactional;
import com.demo.spring.SpringBootOAuth2.util.*;
import com.demo.spring.SpringBootOAuth2.repository.CaseManagementRepositoryCustom;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.demo.spring.SpringBootOAuth2.repository.MachineRepository;
import com.demo.spring.SpringBootOAuth2.repository.MachineHistoryRepository;
import com.demo.spring.SpringBootOAuth2.repository.CaseActivityRepository;
import com.demo.spring.SpringBootOAuth2.repository.UserRepository;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.math.BigDecimal;

import java.io.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.springframework.security.crypto.codec.Base64;
@Service
public class CaseManagementServiceImpl implements CaseManagementService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CaseManagementRepository caseManagementRepository;

    @Autowired
    private CaseManagementRepositoryCustom caseManagementRepositoryCustom;

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private ParameterDetailService parameterDetailService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private MachineHistoryRepository machineHistoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CaseActivityRepository caseActivityRepository;

    @Autowired
    private InstallationRepository installationRepository;

  private static DecimalFormat FORMAT_RUNNING = new DecimalFormat("00");
  private static DecimalFormat FORMAT_YEAR = new DecimalFormat("0000");
  private static DecimalFormat FORMAT_MONTH = new DecimalFormat("00");
  private static final SimpleDateFormat GEN_CASE_DATEFORMAT = new SimpleDateFormat("MM-yyyy", Locale.US);
  private static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
  private static final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

  private static final String IPSERVER = "http://58.181.168.159:8082/files/downloadFileByCaseIdAndFileType";
  private static final String PATH_FILE = "/home/me/devNew/img/";

   private static final String  INSTALLATION_FILE= "/home/me/devNew/doc/installation.xlsx";
   private static final String  INSTALLATION_CLIARIA_FILE= "/home/me/devNew/doc/installation_cliria.xlsx";

   // private static final String  INSTALLATION_FILE= "/home/docker/Baxter_dev/INSTALLATION_RETURN.xlsx";
   private static final String  INSTALLATION_FILE_SWAP= "/home/me/devNew/doc/INSTALLATION_SWAP.xlsx";
   private static final String  INSTALLATION_CLIARIA_FILE_SWAP= "/home/me/devNew/doc/INSTALLATION_SWAP_cliria.xlsx";
   private static final String  INSTALLATION_FILE_RETURN= "/home/me/devNew/doc/INSTALLATION_RETURN.xlsx";
   private static final String  INSTALLATION_CLIARIA_FILE_RETURN= "/home/me/devNew/doc/INSTALLATION_RETURN_cliria.xlsx";
   private static final String  PRESCRIPTION_FILE= "/home/me/devNew/doc/Prescription.xlsx";
   private static final String  RECEIPT_FILE= "/home/me/devNew/doc/RECEIPT.xlsx";
   // private static final String  RECEIPT_FILE= "/home/docker/Baxter_dev/RECEIPT.xlsx";
   
   private static final String signaturePath ="/home/me/devNew/doc/";
  
    JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            return json == null ? null : new Date(json.getAsLong());
        }
    };

    protected Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapter(Date.class, deser).create();


    @Override
    public String uploadfileByCaseIdAndFileType(String name, MultipartFile multipartFile,Long caseId,String fileTpye,String user) {
        Parameter parameter = parameterService.findAppParameterByAppParameterCode(ConstantVariableUtil.PARAMETER_PATH_FILE_UPLOAD);
        ParameterDetail parameterDetail = parameterDetailService.findParameterDetailByCodeAndAppParameter(ConstantVariableUtil.PARAMETER_DETAIL_PATH_FILE_UPLOAD,parameter.getId());
        String status = "OK";
        try{

            CaseManagement caseManagement = caseManagementRepository.findOne(Long.valueOf(caseId));

            if (multipartFile != null && caseManagement != null) {

                String originalFilename = multipartFile.getOriginalFilename();
                String typeFile = originalFilename.split("\\.")[1];
                byte[] bytes = multipartFile.getBytes();
                String FileName = caseManagement.getId() + "_" + fileTpye;
                String pathFile = parameterDetail.getParameterValue();
                FileUpload fileUpload = fileUploadService.findFileUploadByCaseIdAndFileType(caseManagement.getId(),fileTpye);

                if(fileUpload == null) {
                    //---Create FileUpload and Save
                    fileUpload = new FileUpload();
                    fileUpload.setFilePath(pathFile);
                    fileUpload.setFileType(fileTpye);
                    fileUpload.setFileName(originalFilename);
                    fileUpload.setCaseManagement(caseManagement);
                    fileUploadRepository.saveAndFlush(fileUpload);

                    //---Set FileUpload in set case and save
                    Set<FileUpload> setFileUpload = caseManagement.getFileUploads();
                    setFileUpload.add(fileUpload);
                    caseManagement.setFileUploads(setFileUpload);
                    caseManagementRepository.saveAndFlush(caseManagement);
                }else{
                    fileUpload.setFileName(originalFilename);
                    fileUpload.setUpdatdDate(StandardUtil.getCurrentDate());
                }

                File path = new File(pathFile + FileName + "." +typeFile);
                FileCopyUtils.copy(bytes, new FileOutputStream(path));
            }

            return status;
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            return "ERROR";
        }
    }

    @Override
    public InputStream downloadFileByCaseIdAndFileType(Long caseId,String fileTpye){
        InputStream inputStream =null;
        try {
            LOGGER.debug("downloadFileByCaseIdAndFileType :{} : {}",caseId,fileTpye);
            // Parameter parameter = parameterService.findAppParameterByAppParameterCode(ConstantVariableUtil.PARAMETER_PATH_FILE_UPLOAD);
            // ParameterDetail parameterDetail = parameterDetailService.findParameterDetailByCodeAndAppParameter(ConstantVariableUtil.PARAMETER_DETAIL_PATH_FILE_UPLOAD,parameter.getId());
            // FileUpload fileUpload = fileUploadService.findFileUploadByCaseIdAndFileType(caseId,fileTpye);
            // if(fileUpload != null){
                String fileName = caseId + "_" + fileTpye;
                // String pathFile = parameterDetail.getParameterValue();
                String pathFile ="/home/me/devNew/img/";
                // String originalFilename = fileName;
                inputStream = new FileInputStream(pathFile+fileName);
            // }else{
                // throw new RuntimeException("File not found.");
            // }

        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
        }
        return inputStream;
    }

    @Override
    @Transactional
    public Map<String,Object> updateCase(String json,MultipartHttpServletRequest multipartHttpServletRequest){
        try{
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);          
            CaseManagement updateCase = new JSONDeserializer<CaseManagement>().use(null, CaseManagement.class).deserialize(jsonObject.toString());
            Long id = updateCase.getId();
            //Old case
            LOGGER.debug("updateCase  :{} ",id);
            CaseManagement caseManagement = caseManagementRepository.findOne(id);

            Customer customer = caseManagement.getCustomer();
            Customer updateCustomer = updateCase.getCustomer();
            updateCustomer.setId(customer.getId());
            customerRepository.saveAndFlush(updateCustomer);
            
            caseManagement.setCustomer(updateCustomer);
            LOGGER.debug("updateCustomer Type :{} ",updateCase.getCustomer().getCustomerType());
            
            
            User actionUser = caseManagement.getActionUser();
            Installation updateInstallation = updateCase.getInstallation();

            Installation installation = caseManagement.getInstallation();
            updateInstallation.setId(installation.getId());
            installation  = updateInstallation;
            caseManagement.setInstallation(installation);
            // old
            Prescription prescription = caseManagement.getPrescription();
            NurseMenu nurseMenu = prescription.getNurseMenu();
            MakeAdjustment makeAdjustment = prescription.getMakeAdjustment();
            ChangePrograme changePrograme = prescription.getChangePrograme();
            //new  
            Prescription updatePrescription = updateCase.getPrescription();
            updatePrescription.setId(prescription.getId());
            prescription = updatePrescription;
            NurseMenu nurseMenuUpdate = prescription.getNurseMenu();
            MakeAdjustment makeAdjustmentUpdate = prescription.getMakeAdjustment();
            ChangePrograme changeProgrameUpdate = prescription.getChangePrograme();
            nurseMenuUpdate.setId(nurseMenu.getId());            
            makeAdjustmentUpdate.setId(makeAdjustment.getId());
            changeProgrameUpdate.setId(changePrograme.getId());

            nurseMenu = nurseMenuUpdate;
            makeAdjustment = makeAdjustmentUpdate;
            changePrograme = changeProgrameUpdate;
            prescription.setNurseMenu(nurseMenu);
            prescription.setMakeAdjustment(makeAdjustment);
            prescription.setChangePrograme(changePrograme);
            
            caseManagement.setPrescription(prescription);

            // NurseMenu updateNurseMenu = updatePrescription.getNurseMenu();
            // updateNurseMenu.setId(nurseMenu.getId());
            // nurseMenu = updateNurseMenu;

            // prescription.setNurseMenu(nurseMenu);
            // caseManagement.setPrescription(prescription);
        
            // MakeAdjustment updateMakeAdjustment = updatePrescription.getMakeAdjustment();
            // ChangePrograme updateChangePrograme = updatePrescription.getChangePrograme();
            // updatePrescription.setId( prescription.getId() );
            // updateNurseMenu.setId(nurseMenu.getId());
            // updateMakeAdjustment.setId(makeAdjustment.getId());
            // updateChangePrograme.setId( changePrograme.getId());
            // updatePrescription.setNurseMenu(updateNurseMenu);
            // updatePrescription.setMakeAdjustment(updateMakeAdjustment);
            // updatePrescription.setChangePrograme(updateChangePrograme);
            
            caseManagement.setUpdatedDate(StandardUtil.getCurrentDate());
            caseManagement.setShareSource(updateCase.getShareSource());
            caseManagement.setElectronicConsentFlag(updateCase.getElectronicConsentFlag());
            caseManagement.setElectronicConsent(updateCase.getElectronicConsent());

            caseManagement.setChangeCause(updateCase.getChangeCause());
            caseManagement.setIssueCase(updateCase.getIssueCase());
            caseManagement.setContactPersonName(updateCase.getContactPersonName());
            caseManagement.setContactPersonLastName(updateCase.getContactPersonLastName());
            caseManagement.setContactPersonTel(updateCase.getContactPersonTel());
            caseManagement.setNote(updateCase.getNote());

            caseManagement.setCauseReport(updateCase.getCauseReport());
            caseManagement.setDocterReportName(updateCase.getDocterReportName());
            caseManagement.setDocterReportLastName(updateCase.getDocterReportLastName());
            caseManagementRepository.save(caseManagement);
            CaseManagement checkCaseManagement = caseManagementRepository.findOne(id);
            updateMachine(jsonObject.get("machines").toString(),id);

            LOGGER.debug("Customer Type :{} ",checkCaseManagement.getCustomer().getCustomerType());

            LOGGER.debug("upload file");
            MultipartFile idCardFile = multipartHttpServletRequest.getFile("copyIdCard");
            MultipartFile payslipFile = multipartHttpServletRequest.getFile("copyPayslip");
            MultipartFile contractFile = multipartHttpServletRequest.getFile("copyContract");
            String path=PATH_FILE;
            Set<FileUpload> setFile =  checkCaseManagement.getFileUploads();
            if(setFile.size()>0){
                for(FileUpload fileData : setFile){
                if(idCardFile != null && "ID".equalsIgnoreCase( fileData.getFileType()) ){
                    // fileUploadRepository.delete(fileData);
                    FileUpload file = fileData;
                    byte[] bytes = idCardFile.getBytes();
                    String FileName = checkCaseManagement.getId() + "_" + file.getFileType();
                    FileCopyUtils.copy(bytes, new FileOutputStream(path+FileName));
                    file.setFileName(idCardFile.getOriginalFilename());
                    file.setFileType( fileData.getFileType());
                    file.setUpdatdDate(StandardUtil.getCurrentDate());
                    file.setCaseManagement(checkCaseManagement);
                    file.setFileUrl(IPSERVER+"?caseId="+caseManagement.getId()+"&fileType="+file.getFileType());
                    fileUploadRepository.save(file);


                }else if(payslipFile != null &&  "PS".equalsIgnoreCase(fileData.getFileType()) ){
                    // fileUploadRepository.delete(fileData);
                    FileUpload file = fileData;
                    byte[] bytes = payslipFile.getBytes();
                    String FileName = checkCaseManagement.getId() + "_" + file.getFileType();
                    FileCopyUtils.copy(bytes, new FileOutputStream(path+FileName));
                    file.setFileName(payslipFile.getOriginalFilename());
                    file.setFileType( fileData.getFileType());
                    file.setUpdatdDate(StandardUtil.getCurrentDate());
                    file.setCaseManagement(checkCaseManagement);
                    file.setFileUrl(IPSERVER+"?caseId="+caseManagement.getId()+"&fileType="+file.getFileType());
                    // file.setFileUrl(IPSERVER+"casemanagement/downloadFileByCaseIdAndFileType?&caseId="+caseManagement.getId()+"&fileType="+file.getFileType());
                    fileUploadRepository.save(file);

                }else if(contractFile != null && "CT".equalsIgnoreCase(fileData.getFileType())){
                    // fileUploadRepository.delete(fileData);
                    FileUpload file = fileData;
                    byte[] bytes = contractFile.getBytes();
                    String FileName = checkCaseManagement.getId() + "_" + file.getFileType();
                    FileCopyUtils.copy(bytes, new FileOutputStream(path+FileName));
                    file.setFileName(contractFile.getOriginalFilename());
                    file.setFileType( fileData.getFileType());
                    file.setUpdatdDate(StandardUtil.getCurrentDate());
                    file.setCaseManagement(checkCaseManagement);
                    // file.setFileUrl(IPSERVER+"casemanagement/downloadFileByCaseIdAndFileType?&caseId="+caseManagement.getId()+"&fileType="+file.getFileType());
                    file.setFileUrl(IPSERVER+"?caseId="+caseManagement.getId()+"&fileType="+file.getFileType());

                    fileUploadRepository.save(file);
                }
            }

            }else{
              if(idCardFile!=null){
                FileUpload file = new FileUpload();
                byte[] bytes = idCardFile.getBytes();
                String FileName = caseManagement.getId() + "_" + "ID";
                FileCopyUtils.copy(bytes, new FileOutputStream(path+FileName));
                file.setFileName(idCardFile.getOriginalFilename());
                file.setFileType("ID");
                file.setUpdatdDate(StandardUtil.getCurrentDate());
                file.setCaseManagement(caseManagement);
                fileUploadRepository.save(file);

                // file.setFileUrl(IPSERVER+"casemanagement/downloadFileByCaseIdAndFileType?&caseId="+caseManagement.getId()+"&fileType="+file.getFileType());
                file.setFileUrl(IPSERVER+"?caseId="+caseManagement.getId()+"&fileType="+file.getFileType());
               
                fileUploadRepository.save(file);

            }
            if(payslipFile!=null){
                FileUpload file = new FileUpload();
                byte[] bytes = payslipFile.getBytes();
                String FileName = caseManagement.getId() + "_" +"PS";
                FileCopyUtils.copy(bytes, new FileOutputStream(path+FileName));

                file.setFileName(payslipFile.getOriginalFilename());
                file.setFileType("PS");
                file.setUpdatdDate(StandardUtil.getCurrentDate());
                file.setCaseManagement(caseManagement);
                fileUploadRepository.save(file);
                // file.setFileUrl(IPSERVER+"casemanagement/downloadFileByCaseIdAndFileType?&caseId="+caseManagement.getId()+"&fileType="+file.getFileType());
                file.setFileUrl(IPSERVER+"?caseId="+caseManagement.getId()+"&fileType="+file.getFileType());
           
                fileUploadRepository.save(file);
            }
            if(contractFile!=null){
                FileUpload file = new FileUpload();
                byte[] bytes = payslipFile.getBytes();
                String FileName = caseManagement.getId() + "_" + "CT";
                FileCopyUtils.copy(bytes, new FileOutputStream(path+FileName));

                file.setFileName(contractFile.getOriginalFilename());
                file.setFileType("CT");
                file.setUpdatdDate(StandardUtil.getCurrentDate());
                file.setCaseManagement(caseManagement);
                fileUploadRepository.save(file);
                // file.setFileUrl(IPSERVER+"casemanagement/downloadFileByCaseIdAndFileType?&caseId="+caseManagement.getId()+"&fileType="+file.getFileType());
                file.setFileUrl(IPSERVER+"?caseId="+caseManagement.getId()+"&fileType="+file.getFileType());
       
                fileUploadRepository.save(file);
            }
                
            }

        
           

            Map returnResult = new HashMap<>();
            returnResult.put("status","success");
            returnResult.put("caseNumber",caseManagement.getCaseNumber());
            returnResult.put("id",caseManagement.getId());
            return returnResult;
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e.getMessage());
        }
    }



    @Transactional 
    public void updateMachine(String json , Long caseId){
        try{
            LOGGER.info("updateMachine : {}",caseId);
            List<Map<String,Object>> machineData = new JSONDeserializer<List<Map<String,Object>>>().deserialize(json);
            LOGGER.debug("machine size :{}",machineData.size());
            if(machineData.size()>10){
                throw new RuntimeException("Oversize of Machine");
            }
            CaseManagement caseManagement = caseManagementRepository.findOne(caseId);
            LOGGER.debug("clear machine");
            Machine machine1 =caseManagement.getMachine1();
            Machine machine2 =caseManagement.getMachine2();
            Machine machine3 =caseManagement.getMachine3();
            Machine machine4 =caseManagement.getMachine4();
            Machine machine5 =caseManagement.getMachine5();
            Machine machine6 =caseManagement.getMachine6();
            Machine machine7 =caseManagement.getMachine7();
            Machine machine8 =caseManagement.getMachine8();
            Machine machine9 =caseManagement.getMachine9();
            Machine machine10 =caseManagement.getMachine10();

            caseManagement.setMachine1(null);
            caseManagement.setMachine2(null);
            caseManagement.setMachine3(null);
            caseManagement.setMachine4(null);
            caseManagement.setMachine5(null);
            caseManagement.setMachine6(null);
            caseManagement.setMachine7(null);
            caseManagement.setMachine8(null);
            caseManagement.setMachine9(null);
            caseManagement.setMachine10(null);


            // 
            LOGGER.debug("clear set flag 0 ");

            
            List<Machine> listMachine = new ArrayList<>();
            listMachine.add(machine1);
            listMachine.add(machine2);
            listMachine.add(machine3);
            listMachine.add(machine4);
            listMachine.add(machine5);
            listMachine.add(machine6);
            listMachine.add(machine7);
            listMachine.add(machine8);
            listMachine.add(machine9);
            listMachine.add(machine10);
            LOGGER.debug("list size :{}",listMachine.size());
            for(Machine data: listMachine){
                LOGGER.debug("data != null :{}",data != null);
                if(data != null){
                    data.setStatus(1);
                    LOGGER.debug("machine : {} :{} ",data.getCode() ,data.getStatus());
                    machineRepository.saveAndFlush(data);
                }
            }

            String caseNumber = caseManagement.getCaseNumber();
            Integer machineRunning = 1 ;
            for(Map<String,Object> machineInfo: machineData){   
                String machineType =  machineInfo.get("machineType") ==null?"" :machineInfo.get("machineType").toString();
                String modelRef = machineInfo.get("modelRef") ==null?"" :machineInfo.get("modelRef").toString();
                String serialNo = machineInfo.get("serialNo") ==null?"" :machineInfo.get("serialNo").toString();
                // String flagEdit = machineInfo.get("flagEdit") ==null?"0" :machineInfo.get("flagEdit").toString();
                serialNo =  !"AUTO".equalsIgnoreCase(serialNo) ? serialNo  : "";
                // if( "1".equalsIgnoreCase(flagEdit) ){
                    // generate Machine by Condition
                        LOGGER.debug("running : {}   machineInfo :{} :{} :{}",machineRunning,machineType,modelRef,serialNo);
                        // Long machineId = autoGenerateMachineByTypeAndStatusEqActive(machineType,modelRef,serialNo);  
                        Long machineId =  caseManagementRepositoryCustom.autoGenerateMachineByTypeAndStatusEqActive(machineType,modelRef,serialNo);
                        // update Status Machine 
                        LOGGER.debug("before update Machine");
                        updateMachineStatus(machineId , 0 , caseNumber ,"SYSTEM");
                        LOGGER.debug("afer update Machine");
                        
                        Machine machineUsed = machineRepository.findOne(machineId);
                        
                        if(machineRunning == 1){
                            
                            caseManagement.setMachine1(machineUsed);
                        }else if(machineRunning == 2 ){
                            Machine oldMachine = caseManagement.getMachine2();
                            caseManagement.setMachine2(machineUsed);
                        }else if(machineRunning == 3 ){
                            caseManagement.setMachine3(machineUsed);
                        }else if(machineRunning == 4 ){
                            caseManagement.setMachine4(machineUsed);
                        }else if(machineRunning == 5 ){
                            Machine oldMachine = caseManagement.getMachine5();
                            caseManagement.setMachine5(machineUsed);
                        }else if(machineRunning == 6 ){
                            caseManagement.setMachine6(machineUsed);
                        }else if(machineRunning == 7 ){
                            caseManagement.setMachine7(machineUsed);
                        }else if(machineRunning == 8 ){
                            caseManagement.setMachine8(machineUsed);
                        }else if(machineRunning == 9 ){
                            caseManagement.setMachine9(machineUsed);
                        }else if(machineRunning == 10 ){
                            caseManagement.setMachine10(machineUsed);
                        }else{
                            throw new RuntimeException("Oversize of machine");
                        }
                // }
                machineRunning++;
}       
            LOGGER.debug("end of update machine");
        caseManagementRepository.save(caseManagement);
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e.getMessage());
        }
    }



    @Override
    @Transactional
    public Map<String,Object> saveCase(String json,MultipartHttpServletRequest multipartHttpServletRequest){
        try{
            LOGGER.debug("saveCase :{} ",json);
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);
            
            CaseManagement caseManagement = new JSONDeserializer<CaseManagement>().use(null, CaseManagement.class).deserialize(jsonObject.toString());

            
            Map<String,Object> customerDtl = new JSONDeserializer<Map<String,Object>>().deserialize(jsonObject.get("customer").toString());
            List<Map<String,Object>> machineData = new JSONDeserializer<List<Map<String,Object>>>().deserialize(jsonObject.get("machines").toString());
           
            // // create Customer
            Customer customer = new Customer();
            customer.setCustomerType( customerDtl.get("customerType")==null?null:customerDtl.get("customerType").toString() );
            customer.setHospitalName( customerDtl.get("hospitalName")==null?null:customerDtl.get("hospitalName").toString() );
            customer.setPatientName( customerDtl.get("patientName")==null?null:customerDtl.get("patientName").toString() );
            customer.setPatientLastName( customerDtl.get("patientLastName")==null?null:customerDtl.get("patientLastName").toString() );
            
            customer.setNationId( customerDtl.get("nationId")==null?null:customerDtl.get("nationId").toString() );
            customer.setTelNo( customerDtl.get("telNo")==null?null:customerDtl.get("telNo").toString() );
            customer.setCurrentAddress1( customerDtl.get("currentAddress1")==null?null:customerDtl.get("currentAddress1").toString() );
            customer.setCurrentAddress2( customerDtl.get("currentAddress2")==null?null:customerDtl.get("currentAddress2").toString() );
            customer.setCurrentSubDistrict( customerDtl.get("currentSubDistrict")==null?null:customerDtl.get("currentSubDistrict").toString() );
            customer.setCurrentDistrict( customerDtl.get("currentDistrict")==null?null:customerDtl.get("currentDistrict").toString() );
            customer.setCurrentProvince( customerDtl.get("currentProvince")==null?null:customerDtl.get("currentProvince").toString() );
            customer.setCurrentZipCode( customerDtl.get("currentZipCode")==null?null:customerDtl.get("currentZipCode").toString() );
           
            customer.setContactName( customerDtl.get("contactName")==null?null:customerDtl.get("contactName").toString() );
            customer.setContactLastName( customerDtl.get("contactLastName")==null?null:customerDtl.get("contactLastName").toString() );
           

            
            customer.setShippingAddress1( customerDtl.get("shippingAddress1")==null?null:customerDtl.get("shippingAddress1").toString() );
            customer.setShippingAddress2( customerDtl.get("shippingAddress2")==null?null:customerDtl.get("shippingAddress2").toString() );
            customer.setShippingSubDistrict( customerDtl.get("shippingSubDistrict")==null?null:customerDtl.get("shippingSubDistrict").toString() );
            customer.setShippingDistrict( customerDtl.get("shippingDistrict")==null?null:customerDtl.get("shippingDistrict").toString() );
            customer.setShippingProvince( customerDtl.get("shippingProvince")==null?null:customerDtl.get("shippingProvince").toString() );
            customer.setShippingZipCode( customerDtl.get("shippingZipCode")==null?null:customerDtl.get("shippingZipCode").toString() );
            customer.setShippingSameAddress( customerDtl.get("shippingSameAddress")==null?null:customerDtl.get("shippingSameAddress").toString() );
            

            customer.setTelephoneNo1( customerDtl.get("telephoneNo1")==null?null:customerDtl.get("telephoneNo1").toString() );
            customer.setTelephoneNo2( customerDtl.get("telephoneNo2")==null?null:customerDtl.get("telephoneNo2").toString() );
            customer.setTelephoneNo3( customerDtl.get("telephoneNo3")==null?null:customerDtl.get("telephoneNo3").toString() );

            customerRepository.saveAndFlush(customer);
            caseManagement.setCustomer(customer);
            caseManagement.setCreatedDate(StandardUtil.getCurrentDate());
            String caseTypeData =  caseManagement.getCaseType()== null?"CR" :caseManagement.getCaseType()   ;
            caseManagement.setCaseType(caseTypeData);
            String caseNumber = generateCaseNumber(caseManagement.getCaseType());
            caseManagement.setCaseNumber(caseNumber);
            LOGGER.debug("machine size :{}",machineData.size());
            if(machineData.size()>10){
                throw new RuntimeException("Oversize of Machine");
            }
            Integer machineRunning = 1;
            for(Map<String,Object> machineInfo: machineData){   
                String machineType =  machineInfo.get("machineType") ==null?"" :machineInfo.get("machineType").toString();
                String modelRef = machineInfo.get("modelRef") ==null?"" :machineInfo.get("modelRef").toString();
                String serialNo = machineInfo.get("serialNo") ==null?"" :machineInfo.get("serialNo").toString();
                serialNo =  !"AUTO".equalsIgnoreCase(serialNo) ? serialNo  : "";
                // generate Machine by Condition
                Long machineId = autoGenerateMachineByTypeAndStatusEqActive(machineType,modelRef,serialNo);  
                // update Status Machine 
                updateMachineStatus(machineId , 0 , caseNumber ,"SYSTEM");
                Machine machineUsed = machineRepository.findOne(machineId);
                if(machineRunning == 1){
                    caseManagement.setMachine1(machineUsed);
                }else if(machineRunning == 2 ){
                    caseManagement.setMachine2(machineUsed);
                }else if(machineRunning == 3 ){
                    caseManagement.setMachine3(machineUsed);
                }else if(machineRunning == 4 ){
                    caseManagement.setMachine4(machineUsed);
                }else if(machineRunning == 5 ){
                    caseManagement.setMachine5(machineUsed);
                }else if(machineRunning == 6 ){
                    caseManagement.setMachine6(machineUsed);
                }else if(machineRunning == 7 ){
                    caseManagement.setMachine7(machineUsed);
                }else if(machineRunning == 8 ){
                    caseManagement.setMachine8(machineUsed);
                }else if(machineRunning == 9 ){
                    caseManagement.setMachine9(machineUsed);
                }else if(machineRunning == 10 ){
                    caseManagement.setMachine10(machineUsed);
                }else{
                    throw new RuntimeException("Oversize of machine");
                }
                machineRunning++;
            }
            LOGGER.debug("end with machine");
            caseManagement.setCaseStatus("I");
            // init caseActivity
            
            User user = userRepository.findByUsername(caseManagement.getCreatedBy());
            LOGGER.debug("user :{}",user.getId());

          
            caseManagement.setAreaId(user.getBranch().getId());
            // caseManagementRepository.save(caseManagement);
            Map<String,Object> returnResult = new HashMap<>();
            CaseActivity caseActivity = new CaseActivity();
            Set<CaseActivity> activitys = new HashSet<>();
            caseActivity.setUser(user);
            caseActivity.setActionStatus("create case");
            caseActivity.setActionDate(StandardUtil.getCurrentDate());
            caseActivity.setCaseManagement(caseManagement);
            activitys.add(caseActivity);
            caseManagement.setCaseActivitys(activitys);
            caseManagementRepository.save(caseManagement);
            LOGGER.debug("upload file ");
            MultipartFile idCardFile = multipartHttpServletRequest.getFile("copyIdCard");
            MultipartFile payslipFile = multipartHttpServletRequest.getFile("copyPayslip");
            MultipartFile contractFile = multipartHttpServletRequest.getFile("copyContract");
            String path=PATH_FILE;
            if(idCardFile!=null){
                FileUpload file = new FileUpload();
                byte[] bytes = idCardFile.getBytes();
                String FileName = caseManagement.getId() + "_" + "ID";
                FileCopyUtils.copy(bytes, new FileOutputStream(path+FileName));
                file.setFileName(idCardFile.getOriginalFilename());
                file.setFileType("ID");
                file.setUpdatdDate(StandardUtil.getCurrentDate());
                file.setCaseManagement(caseManagement);
                fileUploadRepository.save(file);
                file.setFileUrl(IPSERVER+"?caseId="+caseManagement.getId()+"&fileType="+file.getFileType());

                // file.setFileUrl(IPSERVER+"casemanagement/downloadFileByCaseIdAndFileType?&caseId="+caseManagement.getId()+"&fileType="+file.getFileType());
                fileUploadRepository.save(file);
            }
            if(payslipFile!=null){
                FileUpload file = new FileUpload();
                byte[] bytes = payslipFile.getBytes();
                String FileName = caseManagement.getId() + "_" +"PS";
                FileCopyUtils.copy(bytes, new FileOutputStream(path+FileName));

                file.setFileName(payslipFile.getOriginalFilename());
                file.setFileType("PS");
                file.setUpdatdDate(StandardUtil.getCurrentDate());
                file.setCaseManagement(caseManagement);
                fileUploadRepository.save(file);
                    file.setFileUrl(IPSERVER+"?caseId="+caseManagement.getId()+"&fileType="+file.getFileType());

                // file.setFileUrl(IPSERVER+"casemanagement/downloadFileByCaseIdAndFileType?&caseId="+caseManagement.getId()+"&fileType="+file.getFileType());
                fileUploadRepository.save(file);
            }
            if(contractFile!=null){
                FileUpload file = new FileUpload();
                byte[] bytes = contractFile.getBytes();
                String FileName = caseManagement.getId() + "_" + "CT";
                FileCopyUtils.copy(bytes, new FileOutputStream(path+FileName));

                file.setFileName(contractFile.getOriginalFilename());
                file.setFileType("CT");
                file.setUpdatdDate(StandardUtil.getCurrentDate());
                file.setCaseManagement(caseManagement);
                fileUploadRepository.save(file);
                    file.setFileUrl(IPSERVER+"?caseId="+caseManagement.getId()+"&fileType="+file.getFileType());

                // file.setFileUrl(IPSERVER+"casemanagement/downloadFileByCaseIdAndFileType?&caseId="+caseManagement.getId()+"&fileType="+file.getFileType());
                fileUploadRepository.save(file);
            }
            

            returnResult.put("status","success");
            returnResult.put("caseNumber",caseManagement.getCaseNumber());
            returnResult.put("id",caseManagement.getId());

            return returnResult;

            // return null;
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized String generateCaseNumber(String caseType){
        // cretedDate format DD-YYYY
        try{
            // return null;
            Calendar today = new GregorianCalendar();
            String createDate =  GEN_CASE_DATEFORMAT.format(today.getTime()); 
             LOGGER.info("generateCaseNumber :{} :{} ",caseType,createDate);

            List<String> caseNumberMax = caseManagementRepositoryCustom.getLastedCaseNumberByCriteria(caseType,createDate);
            String maxNumber = "";
            String caseNumber = "";
            if( caseNumberMax.size()>0 ){
                maxNumber = caseNumberMax.get(0) == null ?"":  caseNumberMax.get(0);
            }
            if( "CR".equalsIgnoreCase(caseType) || "AR".equalsIgnoreCase(caseType)   ){
                 caseNumber = caseType+FORMAT_MONTH.format(today.get(Calendar.MONTH) + 1)  + FORMAT_YEAR.format(today.get(Calendar.YEAR)).substring(2) 
                +"/01/" +FORMAT_RUNNING.format( maxNumber.equalsIgnoreCase("")? 1 : Integer.valueOf( maxNumber.substring(maxNumber.length() -2  , maxNumber.length()) ) +1 ) ;
            }
            return caseNumber;
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);   
        }
    }



    @Override
    public synchronized Long autoGenerateMachineByTypeAndStatusEqActive(String machineType,String modelRef,String serialNo){
        LOGGER.info("autoGenerateMachineByTypeAndStatusEqActive : {} :{} :{} ",machineType,modelRef,serialNo);
        try{
            return caseManagementRepositoryCustom.autoGenerateMachineByTypeAndStatusEqActive(machineType,modelRef,serialNo);
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);   
        }
    }


    @Override
    @Transactional
    public synchronized void updateMachineStatus(Long machineId ,Integer status,String caseNumber,String actionBy){
        try{
            LOGGER.debug("updateMachineStatus :{} :{}",machineId,status);
            Machine machine  = machineRepository.findOne(machineId);
            machine.setStatus(status);
            machine.setUpdatedBy(actionBy);
            machine.setUpdatedDate(StandardUtil.getCurrentDate());
            // add MachineHistory 
            Set<MachineHistory> machineHis = machine.getMachineHistory();
            MachineHistory history = new MachineHistory();
            history.setActionBy(actionBy);
            history.setActionDate(StandardUtil.getCurrentDate());
            history.setStatus(status.toString());
            history.setCaseNumber(caseNumber);
            history.setMachine(machine);
            machineHistoryRepository.save(history);
            machineHis.add(history);
            machine.setMachineHistory(machineHis); 
            machineRepository.saveAndFlush(machine);
            LOGGER.debug("machine :{} is status : {} ",machine.getCode() , machine.getStatus() );
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);   
        }
    }
    @Override
    @Transactional
    public Long submitToASM(String json,MultipartHttpServletRequest multipartHttpServletRequest){
        try{
 //  I > W > R|F > W > F
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);
            CaseManagement updateCase = new JSONDeserializer<CaseManagement>().use(null, CaseManagement.class).deserialize(jsonObject.toString());
            Map result = new HashMap<>();
            if( updateCase.getId() == null ){
                result = saveCase(json,multipartHttpServletRequest);
            }else{
                result = updateCase(json,multipartHttpServletRequest);
            }
            CaseManagement caseMng =  null ;
            if(result.get("caseNumber") != null){
                caseMng = findByCaseNumber(result.get("caseNumber").toString());
            }else {
                throw new RuntimeException("submit to ASM ERROR");
            }

            LOGGER.debug("submitToASM :{} ",caseMng.getCaseNumber());
            // CaseManagement caseMng = caseManagementRepository.findOne(id);
            if(caseMng.getCaseStatus().equalsIgnoreCase("I") || caseMng.getCaseStatus().equalsIgnoreCase("R")  ){
                caseMng.setCaseStatus("W");
                caseMng.setUpdatedBy(updateCase.getUpdatedBy());
                caseMng.setUpdatedDate(StandardUtil.getCurrentDate());
                Set<CaseActivity> caseActivitys = caseMng.getCaseActivitys();
                LOGGER.debug("caseActivity :{}",caseActivitys.size());
                CaseActivity caseAct = new CaseActivity();
                User user = userRepository.findByUsername( updateCase.getUpdatedBy());
                caseAct.setUser(user);
                caseAct.setActionStatus("submit to ASM");
                caseAct.setActionDate(StandardUtil.getCurrentDate());
                caseAct.setCaseManagement(caseMng);    
                caseActivityRepository.save(caseAct);
                // caseActivitys.add(caseAct);
                // caseMng.setCaseActivitys(caseActivitys);
                // caseManagementRepository.save(caseMng);
            }else{
                throw new RuntimeException("Error with flow case status not Init or Reject");
            }

            return Long.valueOf(result.get("id").toString());
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);   
        }
    }


    @Override
    public  CaseManagement findByCaseNumber(String caseNumber){
        try{
            LOGGER.debug("findByCaseNumber :{}",caseNumber);
            return caseManagementRepository.findByCaseNumber(caseNumber);
        }catch(Exception e){
             e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);   
        }
    }

    @Override
    public  List<Map<String,Object>> findHistoryDocByAreaAndDocStatusAndRoleAndCase(String createdBy,Long areaId,String documentStatus,String roleBy,String actionUser , String actionDate){
        try{
            LOGGER.debug("findHistoryDocByAreaAndDocStatusAndRoleAndCase :{} : {} :{} :{}",createdBy,areaId,documentStatus,roleBy);
             User user = userRepository.findByUsername( createdBy );
            String role = user.getRole().getName();



            return caseManagementRepositoryCustom.findHistoryDocByAreaAndDocStatusAndRoleAndCase(createdBy,areaId,documentStatus,roleBy,actionUser,actionDate,role);
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public  List<Map<String,Object>> findCaseManagementforChangeMachineByCriteria(String keyword, String customerType,String hospitalName){
        try{

            return caseManagementRepositoryCustom.findCaseManagementforChangeMachineByCriteria(keyword,customerType,hospitalName);
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }

   @Override
   public  List<Map<String,Object>> findCaseByCriteria(String date, String caseNumber , String areaId ,String documentStatus ,Integer firstResult ,Integer maxResult,String caseType,String name){
     try{
        LOGGER.info("findCaseByCriteria : {}",date);
        return caseManagementRepositoryCustom.findCaseByCriteria(date,caseNumber,areaId,documentStatus,firstResult,maxResult,caseType,name);
     }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
   } 

   @Override
   public List<Map<String,Object>> findCaseforReturnCaseByCustomer(String caseType,String customer,String caseNumber){
       try{
        LOGGER.info("findCaseforReturnCaseByCustomer : {} :{} :{}",caseType,customer,caseNumber);
        return caseManagementRepositoryCustom.findCaseforReturnCaseByCustomer(caseType,customer,caseNumber);
       }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }  
   }

   @Override
   public Map<String,Object> uploadTest(MultipartHttpServletRequest multipartHttpServletRequest){
    LOGGER.info("-========= uploadTest ============-");
         try{
            String json = multipartHttpServletRequest.getParameter("json");
            JSONObject jsonObject = new JSONObject(json);
            MultipartFile idCardFile = multipartHttpServletRequest.getFile("copyIdCard");
            MultipartFile payslipFile = multipartHttpServletRequest.getFile("copyPayslip");
            MultipartFile contractFile = multipartHttpServletRequest.getFile("copyContract");
            CaseManagement caseManagement = new JSONDeserializer<CaseManagement>().use(null, CaseManagement.class).deserialize(jsonObject.toString());
            List<Map<String,Object>> machineData = new JSONDeserializer<List<Map<String,Object>>>().deserialize(jsonObject.get("machines").toString());
            String caseType = caseManagement.getCaseType() == null ? "CR" :caseManagement.getCaseType() ;
            String caseNumber = generateCaseNumber(caseType);
    
            Map<String ,Object> result = new HashMap<>();
            result.put("caseNumberDummy",caseNumber);
            List<Map<String,Object>> objFile = new ArrayList<>();

            if(idCardFile!=null){
                Map<String,Object> fileData = new HashMap<>();
                fileData.put("fileType","ID");
                fileData.put("fileName",idCardFile.getOriginalFilename());
                objFile.add(fileData);
            }
            if(payslipFile!=null){
                Map<String,Object> fileData = new HashMap<>();
                fileData.put("fileType","PS");
                fileData.put("fileName",payslipFile.getOriginalFilename());
                objFile.add(fileData);
            }
            if(contractFile!=null){
                Map<String,Object> fileData = new HashMap<>();
                fileData.put("fileType","CT");
                fileData.put("fileName",contractFile.getOriginalFilename());
                objFile.add(fileData);
            }
            result.put("file",objFile);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
   }

   @Override
   @Transactional
   public Map<String,Object> rejectFromAsm(MultipartHttpServletRequest multipartHttpServletRequest){
    try{
        LOGGER.debug("Reject From Asm");
        String json = multipartHttpServletRequest.getParameter("json");
        JSONObject jsonObject = new JSONObject(json);
        Map<String,Object> caseManagerData = new JSONDeserializer<Map<String,Object>>().deserialize(jsonObject.toString());
        saveFromASM(multipartHttpServletRequest);
        CaseManagement caseManagement = caseManagementRepository.findOne( Long.valueOf(caseManagerData.get("id").toString() ) );
        caseManagement.setCaseStatus("R");
        caseManagementRepository.save(caseManagement);
        Set<CaseActivity> caseActivitys = caseManagement.getCaseActivitys();
        LOGGER.debug("caseActivity :{}",caseActivitys.size());
        CaseActivity caseAct = new CaseActivity();
        User user = userRepository.findByUsername( "asm" );
        caseAct.setUser(user);
        caseAct.setActionStatus("Reject from ASM ");
        caseAct.setActionDate(StandardUtil.getCurrentDate());
        caseAct.setCaseManagement(caseManagement);    
        caseActivityRepository.save(caseAct);
        Map<String,Object> resultResult = new HashMap<>();
        resultResult.put("status","success");
        resultResult.put("caseNumber",caseManagement.getCaseNumber());
        resultResult.put("caseStatus",caseManagement.getCaseStatus());
        resultResult.put("actionRole","ASM");
        return resultResult;

    }catch(Exception e){
         e.printStackTrace();
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
         throw new RuntimeException(e);
    }
   }


   @Override
   @Transactional
   public Map<String,Object> approveFromAsm(MultipartHttpServletRequest multipartHttpServletRequest){
    try{
        LOGGER.debug("Approve From Asm");

        String json = multipartHttpServletRequest.getParameter("json");
        JSONObject jsonObject = new JSONObject(json);
        Map<String,Object> caseManagerData = new JSONDeserializer<Map<String,Object>>().deserialize(jsonObject.toString());
        saveFromASM(multipartHttpServletRequest);
        CaseManagement caseManagement = caseManagementRepository.findOne( Long.valueOf(caseManagerData.get("id").toString() ) );
        caseManagement.setCaseStatus("A");
        caseManagement.setAssignAsm("asm");
        caseManagementRepository.save(caseManagement);
        Set<CaseActivity> caseActivitys = caseManagement.getCaseActivitys();
        LOGGER.debug("caseActivity :{}",caseActivitys.size());
        CaseActivity caseAct = new CaseActivity();
        User user = userRepository.findByUsername( "asm" );
        caseAct.setUser(user);
        caseAct.setActionStatus("Approve from ASM ");
        caseAct.setActionDate(StandardUtil.getCurrentDate());
        caseAct.setCaseManagement(caseManagement);    
        caseActivityRepository.save(caseAct);

        Map<String,Object> resultResult = new HashMap<>();
        // resultResult.put("caseNumber",caseManagement.getCaseNumber());
        resultResult.put("status","success");
        resultResult.put("caseNumber",caseManagement.getCaseNumber());
        resultResult.put("caseStatus",caseManagement.getCaseStatus());
        resultResult.put("actionRole","ASM");
        return resultResult;
    }catch(Exception e){
         e.printStackTrace();
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
         throw new RuntimeException(e);
    }
   }

   @Override
   @Transactional
   public void saveFromASM(MultipartHttpServletRequest multipartHttpServletRequest){
    try{
        String json = multipartHttpServletRequest.getParameter("json");
        LOGGER.debug("SaveFromASM :{}",json);

        JSONObject jsonObject = new JSONObject(json);
        Map<String,Object> caseManagerData = new JSONDeserializer<Map<String,Object>>().deserialize(jsonObject.toString());
        CaseManagement caseManagement = caseManagementRepository.findOne( Long.valueOf(caseManagerData.get("id").toString() ) );
        caseManagement.setUpdatedBy("asm");// change
        caseManagement.setUpdatedDate(StandardUtil.getCurrentDate());
        caseManagement.setAsmRemark( caseManagerData.get("asmRemark") ==null?"": caseManagerData.get("asmRemark").toString()  );
        caseManagement.setFlagCheckIdCard( caseManagerData.get("flagCheckIdCard") ==null?"N": caseManagerData.get("flagCheckIdCard").toString()  );
        caseManagement.setFlagCheckPayslip( caseManagerData.get("flagCheckPayslip") ==null?"N": caseManagerData.get("flagCheckPayslip").toString()  );
        caseManagement.setFlagCheckContract( caseManagerData.get("flagCheckContract") ==null?"N": caseManagerData.get("flagCheckContract").toString()  );
        caseManagement.setFlagCheckPrescription( caseManagerData.get("flagCheckPrescription") ==null?"N": caseManagerData.get("flagCheckPrescription").toString()  );
        caseManagement.setFlagCheckInstallation( caseManagerData.get("flagCheckInstallation") ==null?"N": caseManagerData.get("flagCheckInstallation").toString()  );
        caseManagement.setElectronicConsent( caseManagerData.get("electronicConsent") ==null?null: caseManagerData.get("electronicConsent").toString()  );
        caseManagementRepository.save(caseManagement);
    }catch(Exception e){
         e.printStackTrace();
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
         throw new RuntimeException(e);
    }
   }

   @Override
   @Transactional
   public  Map<String,Object> saveChangeCase(String json,MultipartHttpServletRequest multipartHttpServletRequest){
     try{
        LOGGER.debug("saveChangeCase :{} ",json);
        JSONObject jsonObject = new JSONObject(json);
        CaseManagement changeCase = new JSONDeserializer<CaseManagement>().use(null, CaseManagement.class).deserialize(json);
        List<Map<String,Object>> machineData = new JSONDeserializer<List<Map<String,Object>>>().deserialize(jsonObject.get("machines").toString());
        String refCaseId = jsonObject.get("refCaseId").toString();
        CaseManagement refCase = caseManagementRepository.findOne(Long.valueOf(refCaseId));
        User actionBy = userRepository.findByUsername(changeCase.getCreatedBy()); // fixed action user
        
        String oldCaseNumber  = refCase.getCaseNumber();
        String[] subCaseNumber = oldCaseNumber.split("/");
        String newCaseNumber = subCaseNumber[0]+"/02/"+subCaseNumber[2];
        if( refCase.getCloseFlag()!= null && "Y".equalsIgnoreCase(refCase.getCloseFlag()) ){
            throw new RuntimeException(" Can't Create change case ( this case already reference case ) ");
        }
        // close Case
        refCase.setCloseFlag("Y"); 
        refCase.setUpdatedDate( StandardUtil.getCurrentDate() );
        refCase.setUpdatedBy(changeCase.getCreatedBy());
        caseManagementRepository.save(refCase);
        // close old case
        changeCase.setRefCase(refCase);
        changeCase.setAmount(refCase.getAmount());
        changeCase.setCreatedDate( StandardUtil.getCurrentDate() );
        changeCase.setCaseNumber(newCaseNumber);
        changeCase.setCaseStatus("I");
        changeCase.setCaseType("CH");

        changeCase.setCustomer(refCase.getCustomer());
        Integer machineRunning = 1;
            for(Map<String,Object> machineInfo: machineData){   
                String machineType =  machineInfo.get("machineType") ==null?"" :machineInfo.get("machineType").toString();
                String modelRef = machineInfo.get("modelRef") ==null?"" :machineInfo.get("modelRef").toString();
                String serialNo = machineInfo.get("serialNo") ==null?"" :machineInfo.get("serialNo").toString();
                serialNo =  !"AUTO".equalsIgnoreCase(serialNo) ? serialNo  : "";
                // generate Machine by Condition
                Long machineId = autoGenerateMachineByTypeAndStatusEqActive(machineType,modelRef,serialNo);  
                // update Status Machine 
                updateMachineStatus(machineId , 0 , newCaseNumber ,"SYSTEM");
                Machine machineUsed = machineRepository.findOne(machineId);
                if(machineRunning == 1){
                    changeCase.setMachine1(machineUsed);
                }else if(machineRunning == 2 ){
                    changeCase.setMachine2(machineUsed);
                }else if(machineRunning == 3 ){
                    changeCase.setMachine3(machineUsed);
                }else if(machineRunning == 4 ){
                    changeCase.setMachine4(machineUsed);
                }else if(machineRunning == 5 ){
                    changeCase.setMachine5(machineUsed);
                }else if(machineRunning == 6 ){
                    changeCase.setMachine6(machineUsed);
                }else if(machineRunning == 7 ){
                    changeCase.setMachine7(machineUsed);
                }else if(machineRunning == 8 ){
                    changeCase.setMachine8(machineUsed);
                }else if(machineRunning == 9 ){
                    changeCase.setMachine9(machineUsed);
                }else if(machineRunning == 10 ){
                    changeCase.setMachine10(machineUsed);
                }else{
                    throw new RuntimeException("Oversize of machine");
                }
                machineRunning++;
            }
            LOGGER.debug("end with machine");
            LOGGER.debug("user :{}",actionBy.getId());
            changeCase.setAreaId(actionBy.getBranch().getId());
            Map<String,Object> returnResult = new HashMap<>();
            CaseActivity caseActivity = new CaseActivity();
            Set<CaseActivity> activitys = new HashSet<>();
            caseActivity.setUser(actionBy);
            caseActivity.setActionStatus("create Change Machine");
            caseActivity.setActionDate(StandardUtil.getCurrentDate());
            caseActivity.setCaseManagement(changeCase);
            activitys.add(caseActivity);
            changeCase.setCaseActivitys(activitys);
            caseManagementRepository.save(changeCase);


            LOGGER.debug("upload file ");
            MultipartFile idCardFile = multipartHttpServletRequest.getFile("copyIdCard");
            MultipartFile payslipFile = multipartHttpServletRequest.getFile("copyPayslip");
            MultipartFile contractFile = multipartHttpServletRequest.getFile("copyContract");
            String path=PATH_FILE;
            if(idCardFile!=null){
                FileUpload file = new FileUpload();
                byte[] bytes = idCardFile.getBytes();
                String FileName = changeCase.getId() + "_" + "ID";
                FileCopyUtils.copy(bytes, new FileOutputStream(path+FileName));
                file.setFileName(idCardFile.getOriginalFilename());
                file.setFileType("ID");
                file.setUpdatdDate(StandardUtil.getCurrentDate());
                file.setCaseManagement(changeCase);
                fileUploadRepository.save(file);
                    file.setFileUrl(IPSERVER+"?caseId="+changeCase.getId()+"&fileType="+file.getFileType());

                // file.setFileUrl(IPSERVER+"casemanagement/downloadFileByCaseIdAndFileType?&caseId="+changeCase.getId()+"&fileType="+file.getFileType());
                fileUploadRepository.save(file);
            }
            if(payslipFile!=null){
                FileUpload file = new FileUpload();
                byte[] bytes = payslipFile.getBytes();
                String FileName = changeCase.getId() + "_" + "PS";
                FileCopyUtils.copy(bytes, new FileOutputStream(path+FileName));

                file.setFileName(payslipFile.getOriginalFilename());
                file.setFileType("PS");
                file.setUpdatdDate(StandardUtil.getCurrentDate());
                file.setCaseManagement(changeCase);
                fileUploadRepository.save(file);
                    file.setFileUrl(IPSERVER+"?caseId="+changeCase.getId()+"&fileType="+file.getFileType());

                // file.setFileUrl(IPSERVER+"casemanagement/downloadFileByCaseIdAndFileType?&caseId="+changeCase.getId()+"&fileType="+file.getFileType());
                fileUploadRepository.save(file);
            }
            if(contractFile!=null){
                FileUpload file = new FileUpload();
                byte[] bytes = contractFile.getBytes();
                String FileName = changeCase.getId() + "_" + "CT";
                FileCopyUtils.copy(bytes, new FileOutputStream(path+FileName));

                file.setFileName(contractFile.getOriginalFilename());
                file.setFileType("CT");
                file.setUpdatdDate(StandardUtil.getCurrentDate());
                file.setCaseManagement(changeCase);
                fileUploadRepository.save(file);
                    file.setFileUrl(IPSERVER+"?caseId="+changeCase.getId()+"&fileType="+file.getFileType());

                // file.setFileUrl(IPSERVER+"casemanagement/downloadFileByCaseIdAndFileType?&caseId="+changeCase.getId()+"&fileType="+file.getFileType());
                fileUploadRepository.save(file);
            }


            returnResult.put("caseType",changeCase.getCaseType());
            returnResult.put("status","success");
            returnResult.put("caseNumber",changeCase.getCaseNumber());

            return returnResult;
     }catch(Exception e){
          e.printStackTrace();
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
         throw new RuntimeException(e);
     }
   }

   // @Override
   // @Transactional
   // public  Map<String,Object> saveReturnCase(String json,MultipartHttpServletRequest multipartHttpServletRequest){
   //   try{
   //      LOGGER.debug("saveReturnCase :{} ",json);
   //      JSONObject jsonObject = new JSONObject(json);
   //      CaseManagement changeCase = new JSONDeserializer<CaseManagement>().use(null, CaseManagement.class).deserialize(json);
   //      List<Map<String,Object>> machineData = new JSONDeserializer<List<Map<String,Object>>>().deserialize(jsonObject.get("machines").toString());
   //      String refCaseId = jsonObject.get("refCaseId").toString();
   //      CaseManagement refCase = caseManagementRepository.findOne(Long.valueOf(refCaseId));
   //      User actionBy = userRepository.findByUsername(changeCase.getCreatedBy()); // fixed action user
        
   //      String oldCaseNumber  = refCase.getCaseNumber();
   //      String[] subCaseNumber = oldCaseNumber.split("/");
   //      String newCaseNumber = subCaseNumber[0]+"/02/"+subCaseNumber[2];
   //      if( refCase.getCloseFlag()!= null && "Y".equalsIgnoreCase(refCase.getCloseFlag()) ){
   //          throw new RuntimeException(" Can't Create change case ( this case already reference case ) ");
   //      }
   //      // close Case
   //      refCase.setCloseFlag("Y"); 
   //      refCase.setUpdatedDate( StandardUtil.getCurrentDate() );
   //      refCase.setUpdatedBy(changeCase.getCreatedBy());
   //      caseManagementRepository.save(refCase);
   //      // close old case
   //      changeCase.setRefCase(refCase);
   //      changeCase.setCreatedDate( StandardUtil.getCurrentDate() );
   //      changeCase.setCaseNumber(newCaseNumber);
   //      changeCase.setCaseStatus("I");
   //      changeCase.setCaseType("CH");
   //      changeCase.setCustomer(refCase.getCustomer());
   //      Integer machineRunning = 1;
   //          for(Map<String,Object> machineInfo: machineData){   
   //              String machineType =  machineInfo.get("machineType") ==null?"" :machineInfo.get("machineType").toString();
   //              String modelRef = machineInfo.get("modelRef") ==null?"" :machineInfo.get("modelRef").toString();
   //              String serialNo = machineInfo.get("serialNo") ==null?"" :machineInfo.get("serialNo").toString();
   //              serialNo =  !"AUTO".equalsIgnoreCase(serialNo) ? serialNo  : "";
   //              // generate Machine by Condition
   //              Long machineId = autoGenerateMachineByTypeAndStatusEqActive(machineType,modelRef,serialNo);  
   //              // update Status Machine 
   //              updateMachineStatus(machineId , 0 , newCaseNumber ,"SYSTEM");
   //              Machine machineUsed = machineRepository.findOne(machineId);
   //              if(machineRunning == 1){
   //                  changeCase.setMachine1(machineUsed);
   //              }else if(machineRunning == 2 ){
   //                  changeCase.setMachine2(machineUsed);
   //              }else if(machineRunning == 3 ){
   //                  changeCase.setMachine3(machineUsed);
   //              }else if(machineRunning == 4 ){
   //                  changeCase.setMachine4(machineUsed);
   //              }else if(machineRunning == 5 ){
   //                  changeCase.setMachine5(machineUsed);
   //              }else if(machineRunning == 6 ){
   //                  changeCase.setMachine6(machineUsed);
   //              }else if(machineRunning == 7 ){
   //                  changeCase.setMachine7(machineUsed);
   //              }else if(machineRunning == 8 ){
   //                  changeCase.setMachine8(machineUsed);
   //              }else if(machineRunning == 9 ){
   //                  changeCase.setMachine9(machineUsed);
   //              }else if(machineRunning == 10 ){
   //                  changeCase.setMachine10(machineUsed);
   //              }else{
   //                  throw new RuntimeException("Oversize of machine");
   //              }
   //              machineRunning++;
   //          }
   //          LOGGER.debug("end with machine");
   //          LOGGER.debug("user :{}",actionBy.getId());
   //          changeCase.setAreaId(actionBy.getBranch().getId());
   //          Map<String,Object> returnResult = new HashMap<>();
   //          CaseActivity caseActivity = new CaseActivity();
   //          Set<CaseActivity> activitys = new HashSet<>();
   //          caseActivity.setUser(actionBy);
   //          caseActivity.setActionStatus("create Change Machine");
   //          caseActivity.setActionDate(StandardUtil.getCurrentDate());
   //          caseActivity.setCaseManagement(changeCase);
   //          activitys.add(caseActivity);
   //          changeCase.setCaseActivitys(activitys);
   //          caseManagementRepository.save(changeCase);
   //          returnResult.put("caseType",changeCase.getCaseType());
   //          returnResult.put("status","success");
   //          returnResult.put("caseNumber",changeCase.getCaseNumber());

   //          return returnResult;
   //   }catch(Exception e){
   //        e.printStackTrace();
   //       LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
   //       throw new RuntimeException(e);
   //   }
   // }

   @Override
   @Transactional
   public  Map<String,Object> confirmByTS(String json){
    LOGGER.info("confirmByTs ");
    try{
        JSONObject jsonObject = new JSONObject(json);
       
        CaseManagement updateCase = new JSONDeserializer<CaseManagement>().use(null, CaseManagement.class).deserialize(jsonObject.toString());
        Long id = updateCase.getId();
        //Old case
        LOGGER.debug("updateCase  :{} ",id);
        CaseManagement caseManagement = caseManagementRepository.findOne(id);
        caseManagement.setFlagCheckMachine1(updateCase.getFlagCheckMachine1());
        caseManagement.setFlagCheckMachine2(updateCase.getFlagCheckMachine2());
        caseManagement.setFlagCheckMachine3(updateCase.getFlagCheckMachine3());
        caseManagement.setFlagCheckMachine4(updateCase.getFlagCheckMachine4());
        caseManagement.setFlagCheckMachine5(updateCase.getFlagCheckMachine5());
        caseManagement.setFlagCheckMachine6(updateCase.getFlagCheckMachine6());
        caseManagement.setFlagCheckMachine7(updateCase.getFlagCheckMachine7());
        caseManagement.setFlagCheckMachine8(updateCase.getFlagCheckMachine8());
        caseManagement.setFlagCheckMachine9(updateCase.getFlagCheckMachine9());
        caseManagement.setFlagCheckMachine10(updateCase.getFlagCheckMachine10());
        caseManagement.setUpdatedDate( StandardUtil.getCurrentDate() );
        caseManagement.setUpdatedBy(updateCase.getUpdatedBy());

        if(updateCase.getCloseFlag()!=null){
            caseManagement.setAssignTs(updateCase.getUpdatedBy());
        }

        if(updateCase.getNote()!=null){
            caseManagement.setNote(updateCase.getNote());
        }
        caseManagementRepository.save(caseManagement);

        // LOGGER.debug("caseActivity :{}",caseActivitys.size());
        if(updateCase.getCloseFlag()!=null){
            CaseActivity caseAct = new CaseActivity();
            User user = userRepository.findByUsername( updateCase.getUpdatedBy() );
            caseAct.setUser(user);
            caseAct.setActionStatus("Send from TS ");
            caseAct.setActionDate(StandardUtil.getCurrentDate());
            caseAct.setCaseManagement(caseManagement);    
            caseActivityRepository.save(caseAct);
        }


        
        Map<String,Object> returnResult = new HashMap();
        returnResult.put("caseType",caseManagement.getCaseType());
        returnResult.put("status","success");
        returnResult.put("message","TS send success");
        returnResult.put("caseNumber",caseManagement.getCaseNumber());

        return returnResult;
    }catch(Exception e){
          e.printStackTrace();
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
         throw new RuntimeException(e);
    }
   } 

   @Override
   @Transactional
   public  Map<String,Object> confirmByFN(String json){
    LOGGER.info("confirmByFN ");
    try{
        JSONObject jsonObject = new JSONObject(json);
        CaseManagement updateCase = new JSONDeserializer<CaseManagement>().use(null, CaseManagement.class).deserialize(json);
        Long id = updateCase.getId();
        CaseManagement caseMng = caseManagementRepository.findOne(id);
        caseMng.setPayDate(updateCase.getPayDate());
        caseMng.setPaymentType(updateCase.getPaymentType());
        caseMng.setBank(updateCase.getBank());
        caseMng.setAmount(updateCase.getAmount());
        caseMng.setReceiptNo(updateCase.getReceiptNo());
        caseMng.setReceiptDate(updateCase.getReceiptDate());
        caseMng.setReceipientName(updateCase.getReceipientName());
        caseMng.setReceiptAddress1(updateCase.getReceiptAddress1());
        caseMng.setReceiptAddress2(updateCase.getReceiptAddress2());
        caseMng.setReceiptAddress3(updateCase.getReceiptAddress3());
        caseMng.setReceiptAddress4(updateCase.getReceiptAddress4());
        caseMng.setReceiptAddress5(updateCase.getReceiptAddress5());
        caseMng.setReceiptAddress6(updateCase.getReceiptAddress6());
        caseMng.setUpdatedDate( StandardUtil.getCurrentDate() );
        caseMng.setUpdatedBy(updateCase.getUpdatedBy());
        caseMng.setAssignFn(updateCase.getUpdatedBy());
        if(updateCase.getNote()!=null){
            caseMng.setNote(updateCase.getNote());
        }

        caseManagementRepository.save(caseMng);

        CaseActivity caseAct = new CaseActivity();
        User user = userRepository.findByUsername( updateCase.getUpdatedBy() );
        caseAct.setUser(user);
        caseAct.setActionStatus("Send from FN ");
        caseAct.setActionDate(StandardUtil.getCurrentDate());
        caseAct.setCaseManagement(caseMng);    
        caseActivityRepository.save(caseAct);

        Map<String,Object> returnResult = new HashMap();
        returnResult.put("caseType",caseMng.getCaseType());
        returnResult.put("status","success");
        returnResult.put("message","FN send success");
        returnResult.put("caseNumber",caseMng.getCaseNumber());

        return returnResult;
    }catch(Exception e){
          e.printStackTrace();
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
         throw new RuntimeException(e);
    }
   } 

   @Override
   @Transactional
   public  Map<String,Object> confirmByCS(String json){
    LOGGER.info("confirmByCS ");
    try{
        JSONObject caseManagement = new JSONObject(json);
        // Map caseManagement = new JSONDeserializer<Map>().use(null, Map.class).deserialize(json);
        Long id  = Long.valueOf( caseManagement.get("id").toString());
        CaseManagement caseMng = caseManagementRepository.findOne(id);
        caseMng.setUpdatedDate( StandardUtil.getCurrentDate() );
        caseMng.setUpdatedBy( caseManagement.get("updatedBy").toString()  );
        caseMng.setAssignCs( caseManagement.get("updatedBy").toString() );
        caseMng.setDeliveryProvider(caseManagement.get("deliveryProvider").toString());
        caseMng.setDeliveryName(caseManagement.get("deliveryName").toString());
        caseMng.setDeliveryTelNo(caseManagement.get("deliveryTelNo").toString());
	    caseMng.setDeliveryNote(caseManagement.get("deliveryNote").toString());
        caseMng.setNote(caseManagement.get("deliveryNote").toString());
        
        Date deliverDate =new SimpleDateFormat("dd-MM-yyyy").parse(caseManagement.get("deliveryDate").toString());
        caseMng.setDeliveryDate(  new java.sql.Timestamp(deliverDate.getTime()));
        caseManagementRepository.save(caseMng);
        Set<CaseActivity> caseActivitys = caseMng.getCaseActivitys();
        LOGGER.debug("caseActivity :{}",caseActivitys.size());
        
        CaseActivity caseAct = new CaseActivity();
        User user = userRepository.findByUsername(caseMng.getUpdatedBy() );
        caseAct.setUser(user);
        caseAct.setActionStatus("send from Cs");
        caseAct.setActionDate(StandardUtil.getCurrentDate());
        caseAct.setCaseManagement(caseMng);    
        
    	caseActivityRepository.save(caseAct);

        Map<String,Object> returnResult = new HashMap();
        returnResult.put("caseType",caseMng.getCaseType());
        returnResult.put("status","success");
        returnResult.put("message","Cs send success");
        returnResult.put("caseNumber",caseMng.getCaseNumber());

        return returnResult;
    }catch(Exception e){
          e.printStackTrace();
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
         throw new RuntimeException(e);
    }
   } 


   @Override
   @Transactional
   public  Map<String,Object> mockSaveChangeCase(String json,MultipartHttpServletRequest multipartHttpServletRequest){
     try{
        LOGGER.debug("mockSaveChangeCase :{} ",json);
        JSONObject jsonObject = new JSONObject(json);
        CaseManagement changeCase = new JSONDeserializer<CaseManagement>().use(null, CaseManagement.class).deserialize(json);
        List<Map<String,Object>> machineData = new JSONDeserializer<List<Map<String,Object>>>().deserialize(jsonObject.get("machines").toString());
        String refCaseId = jsonObject.get("refCaseId").toString();
        CaseManagement refCase = caseManagementRepository.findOne(Long.valueOf(refCaseId));
        User actionBy = userRepository.findByUsername(changeCase.getCreatedBy()); // fixed action user
        
        String oldCaseNumber  = refCase.getCaseNumber();
        String[] subCaseNumber = oldCaseNumber.split("/");
        String newCaseNumber = subCaseNumber[0]+"/02/"+subCaseNumber[2];
        if( refCase.getCloseFlag()!= null && "Y".equalsIgnoreCase(refCase.getCloseFlag())){
            throw new RuntimeException(" Can't Create change case ( this case already reference case ) ");
        }
        // close Case
        
        // refCase.setCloseFlag("Y"); 
        // refCase.setUpdatedDate( StandardUtil.getCurrentDate() );
        // refCase.setUpdatedBy(changeCase.getCreatedBy());
        // caseManagementRepository.save(refCase);
        
        // close old case
        changeCase.setRefCase(refCase);
        changeCase.setCreatedDate( StandardUtil.getCurrentDate() );
        changeCase.setCaseNumber(newCaseNumber);
        changeCase.setCaseStatus("I");
        changeCase.setCaseType("CH");
        changeCase.setCustomer(refCase.getCustomer());
        Integer machineRunning = 1;
            for(Map<String,Object> machineInfo: machineData){   
                String machineType =  machineInfo.get("machineType") ==null?"" :machineInfo.get("machineType").toString();
                String modelRef = machineInfo.get("modelRef") ==null?"" :machineInfo.get("modelRef").toString();
                String serialNo = machineInfo.get("serialNo") ==null?"" :machineInfo.get("serialNo").toString();
                serialNo =  !"AUTO".equalsIgnoreCase(serialNo) ? serialNo  : "";
                // generate Machine by Condition
                Long machineId = autoGenerateMachineByTypeAndStatusEqActive(machineType,modelRef,serialNo);  
                // update Status Machine 
                updateMachineStatus(machineId , 0 , newCaseNumber ,"SYSTEM");
                Machine machineUsed = machineRepository.findOne(machineId);
                if(machineRunning == 1){
                    changeCase.setMachine1(machineUsed);
                }else if(machineRunning == 2 ){
                    changeCase.setMachine2(machineUsed);
                }else if(machineRunning == 3 ){
                    changeCase.setMachine3(machineUsed);
                }else if(machineRunning == 4 ){
                    changeCase.setMachine4(machineUsed);
                }else if(machineRunning == 5 ){
                    changeCase.setMachine5(machineUsed);
                }else if(machineRunning == 6 ){
                    changeCase.setMachine6(machineUsed);
                }else if(machineRunning == 7 ){
                    changeCase.setMachine7(machineUsed);
                }else if(machineRunning == 8 ){
                    changeCase.setMachine8(machineUsed);
                }else if(machineRunning == 9 ){
                    changeCase.setMachine9(machineUsed);
                }else if(machineRunning == 10 ){
                    changeCase.setMachine10(machineUsed);
                }else{
                    throw new RuntimeException("Oversize of machine");
                }
                machineRunning++;
            }
            LOGGER.debug("end with machine");
            LOGGER.debug("user :{}",actionBy.getId());
            changeCase.setAreaId(actionBy.getBranch().getId());
            Map<String,Object> returnResult = new HashMap<>();
            CaseActivity caseActivity = new CaseActivity();
            Set<CaseActivity> activitys = new HashSet<>();
            caseActivity.setUser(actionBy);
            caseActivity.setActionStatus("create Change Machine");
            caseActivity.setActionDate(StandardUtil.getCurrentDate());
            caseActivity.setCaseManagement(changeCase);
            activitys.add(caseActivity);
            changeCase.setCaseActivitys(activitys);
            caseManagementRepository.save(changeCase);
            returnResult.put("caseType",changeCase.getCaseType());
            returnResult.put("status","success");
            returnResult.put("caseNumber",changeCase.getCaseNumber());

            return returnResult;
     }catch(Exception e){
          e.printStackTrace();
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
         throw new RuntimeException(e);
     }
   }


    @Override
    @Transactional
    public void changeCaseToASM(String json,MultipartHttpServletRequest multipartHttpServletRequest){
        try{
 //  I > W > R|F > W > F
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);
            CaseManagement updateCase = new JSONDeserializer<CaseManagement>().use(null, CaseManagement.class).deserialize(jsonObject.toString());
            Map result = new HashMap<>();
            if( updateCase.getId() == null ){
                result = saveChangeCase(json,multipartHttpServletRequest);
            }else{
                result = updateCase(json,multipartHttpServletRequest);
            }
            CaseManagement caseMng =  null ;
            if(result.get("caseNumber") != null){
                caseMng = findByCaseNumber(result.get("caseNumber").toString());
            }else {
                throw new RuntimeException("submit to ASM ERROR");
            }

            LOGGER.debug("changeCaseToASM :{} ",caseMng.getCaseNumber());
            // CaseManagement caseMng = caseManagementRepository.findOne(id);
            if(caseMng.getCaseStatus().equalsIgnoreCase("I") || caseMng.getCaseStatus().equalsIgnoreCase("R")  ){
                caseMng.setCaseStatus("W");
                caseMng.setUpdatedBy(updateCase.getUpdatedBy());
                caseMng.setUpdatedDate(StandardUtil.getCurrentDate());
                Set<CaseActivity> caseActivitys = caseMng.getCaseActivitys();
                LOGGER.debug("caseActivity :{}",caseActivitys.size());
                CaseActivity caseAct = new CaseActivity();
                User user = userRepository.findByUsername( updateCase.getUpdatedBy());
                caseAct.setUser(user);
                caseAct.setActionStatus("submit to ASM");
                caseAct.setActionDate(StandardUtil.getCurrentDate());
                caseAct.setCaseManagement(caseMng);    
                caseActivityRepository.save(caseAct);
                // caseActivitys.add(caseAct);
                // caseMng.setCaseActivitys(caseActivitys);
                // caseManagementRepository.save(caseMng);
            }else{
                throw new RuntimeException("Error with flow case status not Init or Reject");
            }
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);   
        }
    }



  @Override
   @Transactional
   public  Map<String,Object> saveReturnCase(String json,MultipartHttpServletRequest multipartHttpServletRequest){
     try{
        LOGGER.debug("saveReturnCase :{} ",json);
        JSONObject jsonObject = new JSONObject(json);
        CaseManagement changeCase = new JSONDeserializer<CaseManagement>().use(null, CaseManagement.class).deserialize(json);
        // List<Map<String,Object>> machineData = new JSONDeserializer<List<Map<String,Object>>>().deserialize(jsonObject.get("machines").toString());
        String refCaseId = jsonObject.get("refCaseId").toString();
        CaseManagement refCase = caseManagementRepository.findOne(Long.valueOf(refCaseId));
        User actionBy = userRepository.findByUsername(changeCase.getCreatedBy()); // fixed action user
        
        String oldCaseNumber  = refCase.getCaseNumber();
        String[] subCaseNumber = oldCaseNumber.split("/");
        Integer  run = Integer.valueOf( subCaseNumber[1]) +1;
        String runningSub = "0"+run  ;
        String newCaseNumber = subCaseNumber[0]+"/"+runningSub+"/"+subCaseNumber[2];
        if( refCase.getCloseFlag()!= null && "Y".equalsIgnoreCase(refCase.getCloseFlag()) ){
            throw new RuntimeException(" Can't Create return machine ( this case already reference case ) ");
        }
        // close Case
        refCase.setCloseFlag("Y"); 
        refCase.setUpdatedDate( StandardUtil.getCurrentDate() );
        refCase.setUpdatedBy(changeCase.getCreatedBy());
        caseManagementRepository.save(refCase);
        // close old case
        changeCase.setRefCase(refCase);
        changeCase.setAmount(refCase.getAmount());
        changeCase.setCreatedDate( StandardUtil.getCurrentDate() );
        changeCase.setCaseNumber(newCaseNumber);
        changeCase.setCaseStatus("I");
        changeCase.setCaseType("RT");
        changeCase.setCustomer(refCase.getCustomer());

        changeCase.setMachine1(refCase.getMachine1() == null? null: refCase.getMachine1() );
        changeCase.setMachine2(refCase.getMachine2() == null? null: refCase.getMachine2() );
        changeCase.setMachine3(refCase.getMachine3() == null? null: refCase.getMachine3() );
        changeCase.setMachine4(refCase.getMachine4()== null? null: refCase.getMachine4());
        changeCase.setMachine5(refCase.getMachine5() == null? null: refCase.getMachine5());
        changeCase.setMachine6(refCase.getMachine6()== null? null: refCase.getMachine6());
        changeCase.setMachine7(refCase.getMachine7()== null? null: refCase.getMachine7());
        changeCase.setMachine8(refCase.getMachine8()== null? null: refCase.getMachine8());
        changeCase.setMachine9(refCase.getMachine9()== null? null: refCase.getMachine9());
        changeCase.setMachine10(refCase.getMachine10()== null? null: refCase.getMachine10());


        Integer machineRunning =1;
            // for(Map<String,Object> machineInfo: machineData){   
            //     String machineType =  machineInfo.get("machineType") ==null?"" :machineInfo.get("machineType").toString();
            //     String modelRef = machineInfo.get("modelRef") ==null?"" :machineInfo.get("modelRef").toString();
            //     String serialNo = machineInfo.get("serialNo") ==null?"" :machineInfo.get("serialNo").toString();
            //     serialNo =  !"AUTO".equalsIgnoreCase(serialNo) ? serialNo  : "";
            //     // generate Machine by Condition
            //     Long machineId = autoGenerateMachineByTypeAndStatusEqActive(machineType,modelRef,serialNo);  
            //     // update Status Machine 
            //     updateMachineStatus(machineId , 0 , newCaseNumber ,"SYSTEM");
            //     Machine machineUsed = machineRepository.findOne(machineId);
            //     if(machineRunning == 1){
            //         changeCase.setMachine1(machineUsed);
            //     }else if(machineRunning == 2 ){
            //         changeCase.setMachine2(machineUsed);
            //     }else if(machineRunning == 3 ){
            //         changeCase.setMachine3(machineUsed);
            //     }else if(machineRunning == 4 ){
            //         changeCase.setMachine4(machineUsed);
            //     }else if(machineRunning == 5 ){
            //         changeCase.setMachine5(machineUsed);
            //     }else if(machineRunning == 6 ){
            //         changeCase.setMachine6(machineUsed);
            //     }else if(machineRunning == 7 ){
            //         changeCase.setMachine7(machineUsed);
            //     }else if(machineRunning == 8 ){
            //         changeCase.setMachine8(machineUsed);
            //     }else if(machineRunning == 9 ){
            //         changeCase.setMachine9(machineUsed);
            //     }else if(machineRunning == 10 ){
            //         changeCase.setMachine10(machineUsed);
            //     }else{
            //         throw new RuntimeException("Oversize of machine");
            //     }
            //     machineRunning++;
            // }
            LOGGER.debug("end with machine");
            LOGGER.debug("user :{}",actionBy.getId());
            changeCase.setAreaId(actionBy.getBranch().getId());
            Map<String,Object> returnResult = new HashMap<>();
            CaseActivity caseActivity = new CaseActivity();
            Set<CaseActivity> activitys = new HashSet<>();
            caseActivity.setUser(actionBy);
            caseActivity.setActionStatus("create Change Machine");
            caseActivity.setActionDate(StandardUtil.getCurrentDate());
            caseActivity.setCaseManagement(changeCase);
            activitys.add(caseActivity);
            changeCase.setCaseActivitys(activitys);
            caseManagementRepository.save(changeCase);

            LOGGER.debug("upload file ");
            MultipartFile idCardFile = multipartHttpServletRequest.getFile("copyIdCard");
            MultipartFile payslipFile = multipartHttpServletRequest.getFile("copyPayslip");
            MultipartFile contractFile = multipartHttpServletRequest.getFile("copyContract");
            String path=PATH_FILE;
            if(idCardFile!=null){
                FileUpload file = new FileUpload();
                byte[] bytes = idCardFile.getBytes();
                String FileName = changeCase.getId() + "_" + "ID";
                FileCopyUtils.copy(bytes, new FileOutputStream(path+FileName));
                file.setFileName(idCardFile.getOriginalFilename());
                file.setFileType("ID");
                file.setUpdatdDate(StandardUtil.getCurrentDate());
                file.setCaseManagement(changeCase);
                fileUploadRepository.save(file);
                    file.setFileUrl(IPSERVER+"?caseId="+changeCase.getId()+"&fileType="+file.getFileType());

                // file.setFileUrl(IPSERVER+"casemanagement/downloadFileByCaseIdAndFileType?&caseId="+changeCase.getId()+"&fileType="+file.getFileType());
                fileUploadRepository.save(file);
            }
            if(payslipFile!=null){
                FileUpload file = new FileUpload();
                byte[] bytes = payslipFile.getBytes();
                String FileName = changeCase.getId() + "_" + "PS";
                FileCopyUtils.copy(bytes, new FileOutputStream(path+FileName));

                file.setFileName(payslipFile.getOriginalFilename());
                file.setFileType("PS");
                file.setUpdatdDate(StandardUtil.getCurrentDate());
                file.setCaseManagement(changeCase);
                fileUploadRepository.save(file);
                    file.setFileUrl(IPSERVER+"?caseId="+changeCase.getId()+"&fileType="+file.getFileType());

                // file.setFileUrl(IPSERVER+"casemanagement/downloadFileByCaseIdAndFileType?&caseId="+changeCase.getId()+"&fileType="+file.getFileType());
                fileUploadRepository.save(file);
            }
            if(contractFile!=null){
                FileUpload file = new FileUpload();
                byte[] bytes = contractFile.getBytes();
                String FileName = changeCase.getId() + "_" + "CT";
                FileCopyUtils.copy(bytes, new FileOutputStream(path+FileName));

                file.setFileName(contractFile.getOriginalFilename());
                file.setFileType("CT");
                file.setUpdatdDate(StandardUtil.getCurrentDate());
                file.setCaseManagement(changeCase);
                fileUploadRepository.save(file);
                file.setFileUrl(IPSERVER+"?caseId="+changeCase.getId()+"&fileType="+file.getFileType());
                
                // file.setFileUrl(IPSERVER+"casemanagement/downloadFileByCaseIdAndFileType?&caseId="+changeCase.getId()+"&fileType="+file.getFileType());
                fileUploadRepository.save(file);
            }


            returnResult.put("caseType",changeCase.getCaseType());
            returnResult.put("status","success");
            returnResult.put("caseNumber",changeCase.getCaseNumber());

            return returnResult;
     }catch(Exception e){
          e.printStackTrace();
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
         throw new RuntimeException(e);
     }
   }


   @Override
   @Transactional
   public  Map<String,Object> sendReturnCaseToNextRole(String json,MultipartHttpServletRequest multipartHttpServletRequest){
     try{

             ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);
            CaseManagement updateCase = new JSONDeserializer<CaseManagement>().use(null, CaseManagement.class).deserialize(jsonObject.toString());
            Map result = new HashMap<>();
            if( updateCase.getId() == null ){
                result = saveReturnCase(json,multipartHttpServletRequest);
            }else{
                result = updateCase(json,multipartHttpServletRequest);
            }
            CaseManagement caseMng =  null ;
            if(result.get("caseNumber") != null){
                caseMng = findByCaseNumber(result.get("caseNumber").toString());
            }else {
                throw new RuntimeException("submit to Other Role ERROR");
            }
            Map resultResult = new HashMap<>();
             if(caseMng.getCaseStatus().equalsIgnoreCase("I") || caseMng.getCaseStatus().equalsIgnoreCase("R")  ){
                caseMng.setCaseStatus("A");
                caseMng.setUpdatedBy(updateCase.getUpdatedBy());
                caseMng.setUpdatedDate(StandardUtil.getCurrentDate());
                Set<CaseActivity> caseActivitys = caseMng.getCaseActivitys();
                LOGGER.debug("caseActivity :{}",caseActivitys.size());
                CaseActivity caseAct = new CaseActivity();
                User user = userRepository.findByUsername( updateCase.getUpdatedBy() );
                caseAct.setUser(user);
                caseAct.setActionStatus("submit return machine to Other role");
                caseAct.setActionDate(StandardUtil.getCurrentDate());
                caseAct.setCaseManagement(caseMng);    
                caseActivityRepository.save(caseAct);

                resultResult.put("caseNumber",caseMng.getCaseNumber());
                resultResult.put("caseStatus",caseMng.getCaseStatus());
                resultResult.put("actionRole","SALE");
                resultResult.put("status","success");
              
                return resultResult;

            }else{
                throw new RuntimeException("Error with flow case status not Init or Reject");
            }


     }catch(Exception e){
          e.printStackTrace();
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
         throw new RuntimeException(e);
     }
   }


    @Override
   public  List<Map<String,Object>> findCaseByCriteriaforTS(String date, String caseNumber , String areaId ,String documentStatus ,Integer firstResult ,Integer maxResult,String username){
     try{
        LOGGER.info("findCaseforOtherRole : {}",date);
        return caseManagementRepositoryCustom.findCaseforOtherRole(date,caseNumber,areaId,"F",firstResult,maxResult,null,"TS",username);
     }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
   } 
   @Override
   public  List<Map<String,Object>> findCaseByCriteriaforFN(String date, String caseNumber , String areaId ,String documentStatus ,Integer firstResult ,Integer maxResult,String username){
     try{
        LOGGER.info("findCaseforOtherRole : {}",date);
        return caseManagementRepositoryCustom.findCaseforOtherRole(date,caseNumber,areaId,"F",firstResult,maxResult,null,"FN",username);
     }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
   } 
   @Override
   public  List<Map<String,Object>> findCaseByCriteriaforCS(String date, String caseNumber , String areaId ,String documentStatus ,Integer firstResult ,Integer maxResult,String username){
     try{
        LOGGER.info("findCaseforOtherRole : {}",date);
        return caseManagementRepositoryCustom.findCaseforOtherRole(date,caseNumber,areaId,"F",firstResult,maxResult,null,"CS",username);
     }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
   } 


   @Override
   public  List<Map<String,Object>> findCaseByCriteriaforBU(String date, String caseNumber , String areaId ,String documentStatus ,Integer firstResult ,Integer maxResult,String username){
     try{
        LOGGER.info("findCaseforOtherRole : {}",date);
        return caseManagementRepositoryCustom.findCaseforOtherRole(date,caseNumber,areaId,"A",firstResult,maxResult,null,"BU",username);
     }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
   } 


   @Override
   public List<Map<String,Object>>  countCaseOverAll(String caseStatus,String startDate, String endDate,String areaId){
    try{
        return caseManagementRepositoryCustom.countCaseOverAll(caseStatus,startDate,endDate,areaId);       
    }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
   }

   @Override
   public Map<String,Object> countCaseShowInDashboard(String startDate, String endDate,String createdBy,String areaId ){
    try{
        Map<String,Object> result = new HashMap<>();

        User user = userRepository.findByUsername( createdBy );
        String role = user.getRole().getName();

        List<Map<String,Object>> listCR = caseManagementRepositoryCustom.countCaseShowInDashboard("CR",startDate,endDate,createdBy,areaId,role);
        List<Map<String,Object>> listAR = caseManagementRepositoryCustom.countCaseShowInDashboard("AR",startDate,endDate,createdBy,areaId,role);
        List<Map<String,Object>> listCH = caseManagementRepositoryCustom.countCaseShowInDashboard("CH",startDate,endDate,createdBy,areaId,role);
        List<Map<String,Object>> listRT = caseManagementRepositoryCustom.countCaseShowInDashboard("RT",startDate,endDate,createdBy,areaId,role);
        
        result.put("CR",listCR);
        result.put("AR",listAR);
        result.put("CH",listCH);
        result.put("RT",listRT);
        // List<Map<String,Object>>
        return result;     
    }catch(Exception e){
        e.printStackTrace();
        LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
        throw new RuntimeException(e);
    }
   }


   @Override
   public List<Map<String,Object>>  getCaseDetailShowInDashboard(String caseStatus,String startDate, String endDate,String areaId,String caseType){
    try{
        return caseManagementRepositoryCustom.getCaseDetailShowInDashboard(caseStatus,startDate,endDate,areaId,caseType);       
    }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
   }


   @Override
   @Transactional
   public Map<String,Object> confirmByBU(String json){
     LOGGER.info("confirmByBU");
    try{
        JSONObject updateCase = new JSONObject(json);
        // Map updateCase = new JSONDeserializer<Map>().use(null, Map.class).deserialize(json);
        Long id = Long.valueOf( updateCase.get("id").toString());
        CaseManagement caseMng = caseManagementRepository.findOne(id);
        caseMng.setUpdatedBy(updateCase.get("updatedBy").toString());
        caseMng.setAssignBu(updateCase.get("updatedBy").toString());
        caseMng.setUpdatedDate(StandardUtil.getCurrentDate());
        caseMng.setBuNote(updateCase.get("buNote").toString());
        caseMng.setNote(updateCase.get("buNote").toString());

        if( "A".equalsIgnoreCase(  updateCase.get("status").toString() ) ){
            caseMng.setCaseStatus("F");
        }else{
            caseMng.setCaseStatus("R");
        }
        caseManagementRepository.save(caseMng);

        CaseActivity caseAct = new CaseActivity();
        User user = userRepository.findByUsername( updateCase.get("updatedBy").toString() );
        String action = "A".equalsIgnoreCase(updateCase.get("status").toString() ) ? "Approve by BU : "+caseMng.getBuNote() : "Reject by Bu : "+caseMng.getBuNote(); 
        caseAct.setUser(user);
        caseAct.setActionStatus(action);
        caseAct.setActionDate(StandardUtil.getCurrentDate());
        caseAct.setCaseManagement(caseMng);    
        caseActivityRepository.save(caseAct);

        Map<String,Object> returnResult = new HashMap();
        returnResult.put("caseType",caseMng.getCaseType());
        returnResult.put("status","success");
        returnResult.put("message","BU action success");
        returnResult.put("caseNumber",caseMng.getCaseNumber());

        return returnResult;
    }catch(Exception e){
         e.printStackTrace();
        LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
        throw new RuntimeException(e);
    }
   }

   @Override
   public void uploadDigitalSignature(String json,MultipartFile file){
        try{
            LOGGER.debug("uploadDigitalSignature :{}",json);
            JSONObject jsonObject = new JSONObject(json);
            String username = jsonObject.get("username").toString();
            User user = userRepository.findByUsername(username);
            
             if(file!=null){
                user.setDigitalSignature(file.getOriginalFilename());
                byte[] bytes = file.getBytes();
                String FileName = username;
                FileCopyUtils.copy(bytes, new FileOutputStream(signaturePath+FileName));
                userRepository.save(user);
            }

        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
   }


   public XSSFWorkbook getInstallations(CaseManagement caseMng){
    try{
        InputStream inp = null;
        if("MC1".equalsIgnoreCase(caseMng.getMachine1().getMachineType())){
            inp = new FileInputStream(INSTALLATION_FILE); 
        }else{
            inp = new FileInputStream(INSTALLATION_CLIARIA_FILE); 
        }
        XSSFWorkbook  workbook  = new XSSFWorkbook(inp);
        inp.close();
        XSSFSheet sheet = workbook.getSheetAt(0);
          
        int useRow = 11;
        // if(workbook!=null){
            LOGGER.debug("readFile");
            sheet.getRow(7).getCell(1).setCellValue(format.format(new Date()));
            
            sheet.getRow(7).getCell(4).setCellValue(caseMng.getInstallation().getInstallationPlace() == null ? "" :caseMng.getInstallation().getInstallationPlace() );
            Machine machine1 =caseMng.getMachine1();
            Machine machine2 =caseMng.getMachine2();
            Machine machine3 =caseMng.getMachine3();
            Machine machine4 =caseMng.getMachine4();
            Machine machine5 =caseMng.getMachine5();
            Machine machine6 =caseMng.getMachine6();
            Machine machine7 =caseMng.getMachine7();
            Machine machine8 =caseMng.getMachine8();
            Machine machine9 =caseMng.getMachine9();
            Machine machine10 =caseMng.getMachine10();

            if(machine1!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine1.getCode() == null ? "":machine1.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine1.getName() == null ? "":machine1.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine1.getSerialNumber() == null ? "":machine1.getSerialNumber() );
                sheet.getRow(useRow).getCell(6).setCellValue("1" );
                useRow++;
            }
            if(machine2!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine2.getCode() == null ? "":machine2.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine2.getName() == null ? "":machine2.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine2.getSerialNumber() == null ? "":machine2.getSerialNumber() );
                sheet.getRow(useRow).getCell(6).setCellValue("1" );
                useRow++;
            }
            if(machine3!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine3.getCode() == null ? "":machine3.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine3.getName() == null ? "":machine3.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine3.getSerialNumber() == null ? "":machine3.getSerialNumber() );
                sheet.getRow(useRow).getCell(6).setCellValue("1" );
                useRow++;
            }
            if(machine4!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine4.getCode() == null ? "":machine4.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine4.getName() == null ? "":machine4.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine4.getSerialNumber() == null ? "":machine4.getSerialNumber() );
                sheet.getRow(useRow).getCell(6).setCellValue("1" );
                useRow++;
            }
            if(machine5!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine5.getCode() == null ? "":machine5.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine5.getName() == null ? "":machine5.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine5.getSerialNumber() == null ? "":machine5.getSerialNumber() );
                sheet.getRow(useRow).getCell(6).setCellValue("1" );
                useRow++;
            }
            if(machine6!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine6.getCode() == null ? "":machine6.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine6.getName() == null ? "":machine6.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine6.getSerialNumber() == null ? "":machine6.getSerialNumber() );
                sheet.getRow(useRow).getCell(6).setCellValue("1" );
                useRow++;
            }
            if(machine7!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine7.getCode() == null ? "":machine7.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine7.getName() == null ? "":machine7.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine7.getSerialNumber() == null ? "":machine7.getSerialNumber() );
                sheet.getRow(useRow).getCell(6).setCellValue("1" );
                useRow++;
            }
            if(machine8!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine8.getCode() == null ? "":machine8.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine8.getName() == null ? "":machine8.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine8.getSerialNumber() == null ? "":machine8.getSerialNumber() );
                sheet.getRow(useRow).getCell(6).setCellValue("1" );
                useRow++;
            }
            if(machine9!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine9.getCode() == null ? "":machine9.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine9.getName() == null ? "":machine9.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine9.getSerialNumber() == null ? "":machine9.getSerialNumber() );
                sheet.getRow(useRow).getCell(6).setCellValue("1" );
                useRow++;
            }
            if(machine10!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine10.getCode() == null ? "":machine10.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine10.getName() == null ? "":machine10.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine10.getSerialNumber() == null ? "":machine10.getSerialNumber() );
                sheet.getRow(useRow).getCell(6).setCellValue("1" );
                useRow++;
            }

        String assignBU = caseMng.getAssignBu();
        User bu = userRepository.findByUsername(assignBU);
        InputStream signnature = new FileInputStream(signaturePath+assignBU);
        byte[] bytes = IOUtils.toByteArray(signnature);
        int digitalSignatureByte = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        signnature.close(); 
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor my_anchor = new XSSFClientAnchor();
        my_anchor.setCol1(5);
        my_anchor.setRow1(25);   //   installation 
         // my_anchor.setRow1(36);      //swap 
         // my_anchor.setRow1(25);      //return 

        XSSFPicture  my_picture = drawing.createPicture(my_anchor, digitalSignatureByte);
        my_picture.resize(0.3); 

           return workbook; 
    }catch(Exception e){
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
        throw new RuntimeException(e);
    }

   }

   public XSSFWorkbook getInstallationChangeMachine(CaseManagement caseMng){
     try{
         InputStream inp = null;
        
        if("MC1".equalsIgnoreCase(caseMng.getMachine1().getMachineType())){
            inp = new FileInputStream(INSTALLATION_FILE_SWAP); 
        }else{
            inp = new FileInputStream(INSTALLATION_CLIARIA_FILE_SWAP); 
        }

        XSSFWorkbook  workbook  = new XSSFWorkbook(inp);
        inp.close();
        XSSFSheet sheet = workbook.getSheetAt(0);
        int useRow = 11;
        int returnRow = 22;
        // if(workbook!=null){
            LOGGER.debug("readFile");
            sheet.getRow(7).getCell(2).setCellValue(format.format(new Date()));
            sheet.getRow(7).getCell(4).setCellValue(caseMng.getInstallation().getInstallationPlace() == null ? "" :caseMng.getInstallation().getInstallationPlace() );
            Machine machine1 =caseMng.getMachine1();
            Machine machine2 =caseMng.getMachine2();
            Machine machine3 =caseMng.getMachine3();
            Machine machine4 =caseMng.getMachine4();
            Machine machine5 =caseMng.getMachine5();
            Machine machine6 =caseMng.getMachine6();
            Machine machine7 =caseMng.getMachine7();
            Machine machine8 =caseMng.getMachine8();
            Machine machine9 =caseMng.getMachine9();
            Machine machine10 =caseMng.getMachine10();
             if(machine1!=null){
                sheet.getRow(useRow).getCell(1).setCellValue(machine1.getCode() == null ? "":machine1.getCode() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine1.getName() == null ? "":machine1.getName() );
                sheet.getRow(useRow).getCell(3).setCellValue(machine1.getSerialNumber() == null ? "":machine1.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1" );

                sheet.getRow(returnRow).getCell(1).setCellValue(machine1.getCode() == null ? "":machine1.getCode() );
                sheet.getRow(returnRow).getCell(2).setCellValue(machine1.getName() == null ? "":machine1.getName() );
                sheet.getRow(returnRow).getCell(3).setCellValue(machine1.getSerialNumber() == null ? "":machine1.getSerialNumber() );
                sheet.getRow(returnRow).getCell(7).setCellValue("1" );

                useRow++;
                returnRow++;
            }
            if(machine2!=null){
                sheet.getRow(useRow).getCell(1).setCellValue(machine2.getCode() == null ? "":machine2.getCode() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine2.getName() == null ? "":machine2.getName() );
                sheet.getRow(useRow).getCell(3).setCellValue(machine2.getSerialNumber() == null ? "":machine2.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1" );

                sheet.getRow(returnRow).getCell(1).setCellValue(machine2.getCode() == null ? "":machine2.getCode() );
                sheet.getRow(returnRow).getCell(2).setCellValue(machine2.getName() == null ? "":machine2.getName() );
                sheet.getRow(returnRow).getCell(3).setCellValue(machine2.getSerialNumber() == null ? "":machine2.getSerialNumber() );
                sheet.getRow(returnRow).getCell(7).setCellValue("1" );

                useRow++;
                returnRow++;
            }
            if(machine3!=null){
                sheet.getRow(useRow).getCell(1).setCellValue(machine3.getCode() == null ? "":machine3.getCode() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine3.getName() == null ? "":machine3.getName() );
                sheet.getRow(useRow).getCell(3).setCellValue(machine3.getSerialNumber() == null ? "":machine3.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1" );

                sheet.getRow(returnRow).getCell(1).setCellValue(machine3.getCode() == null ? "":machine3.getCode() );
                sheet.getRow(returnRow).getCell(2).setCellValue(machine3.getName() == null ? "":machine3.getName() );
                sheet.getRow(returnRow).getCell(3).setCellValue(machine3.getSerialNumber() == null ? "":machine3.getSerialNumber() );
                sheet.getRow(returnRow).getCell(7).setCellValue("1" );

                useRow++;
                returnRow++;
            }
            if(machine4!=null){
                sheet.getRow(useRow).getCell(1).setCellValue(machine4.getCode() == null ? "":machine4.getCode() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine4.getName() == null ? "":machine4.getName() );
                sheet.getRow(useRow).getCell(3).setCellValue(machine4.getSerialNumber() == null ? "":machine4.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1");

                sheet.getRow(returnRow).getCell(1).setCellValue(machine4.getCode() == null ? "":machine4.getCode() );
                sheet.getRow(returnRow).getCell(2).setCellValue(machine4.getName() == null ? "":machine4.getName() );
                sheet.getRow(returnRow).getCell(3).setCellValue(machine4.getSerialNumber() == null ? "":machine4.getSerialNumber() );
                sheet.getRow(returnRow).getCell(7).setCellValue("1" );

                useRow++;
                returnRow++;
            }
            if(machine5!=null){
                sheet.getRow(useRow).getCell(1).setCellValue(machine5.getCode() == null ? "":machine5.getCode() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine5.getName() == null ? "":machine5.getName() );
                sheet.getRow(useRow).getCell(3).setCellValue(machine5.getSerialNumber() == null ? "":machine5.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1");
                
                sheet.getRow(returnRow).getCell(1).setCellValue(machine5.getCode() == null ? "":machine5.getCode() );
                sheet.getRow(returnRow).getCell(2).setCellValue(machine5.getName() == null ? "":machine5.getName() );
                sheet.getRow(returnRow).getCell(3).setCellValue(machine5.getSerialNumber() == null ? "":machine5.getSerialNumber() );
                sheet.getRow(returnRow).getCell(7).setCellValue("1" );

                useRow++;
                returnRow++;
            }
            if(machine6!=null){
                sheet.getRow(useRow).getCell(1).setCellValue(machine6.getCode() == null ? "":machine6.getCode() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine6.getName() == null ? "":machine6.getName() );
                sheet.getRow(useRow).getCell(3).setCellValue(machine6.getSerialNumber() == null ? "":machine6.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1");
                
                sheet.getRow(returnRow).getCell(1).setCellValue(machine6.getCode() == null ? "":machine6.getCode() );
                sheet.getRow(returnRow).getCell(2).setCellValue(machine6.getName() == null ? "":machine6.getName() );
                sheet.getRow(returnRow).getCell(3).setCellValue(machine6.getSerialNumber() == null ? "":machine6.getSerialNumber() );
                sheet.getRow(returnRow).getCell(7).setCellValue("1" );

                useRow++;
                returnRow++;
            }
            if(machine7!=null){
                sheet.getRow(useRow).getCell(1).setCellValue(machine7.getCode() == null ? "":machine7.getCode() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine7.getName() == null ? "":machine7.getName() );
                sheet.getRow(useRow).getCell(3).setCellValue(machine7.getSerialNumber() == null ? "":machine7.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1");
                
                sheet.getRow(returnRow).getCell(1).setCellValue(machine7.getCode() == null ? "":machine7.getCode() );
                sheet.getRow(returnRow).getCell(2).setCellValue(machine7.getName() == null ? "":machine7.getName() );
                sheet.getRow(returnRow).getCell(3).setCellValue(machine7.getSerialNumber() == null ? "":machine7.getSerialNumber() );
                sheet.getRow(returnRow).getCell(7).setCellValue("1");

                useRow++;
                returnRow++;
            }
            if(machine8!=null){
                sheet.getRow(useRow).getCell(1).setCellValue(machine8.getCode() == null ? "":machine8.getCode() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine8.getName() == null ? "":machine8.getName() );
                sheet.getRow(useRow).getCell(3).setCellValue(machine8.getSerialNumber() == null ? "":machine8.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1");
                
                sheet.getRow(returnRow).getCell(1).setCellValue(machine8.getCode() == null ? "":machine8.getCode() );
                sheet.getRow(returnRow).getCell(2).setCellValue(machine8.getName() == null ? "":machine8.getName() );
                sheet.getRow(returnRow).getCell(3).setCellValue(machine8.getSerialNumber() == null ? "":machine8.getSerialNumber() );
                sheet.getRow(returnRow).getCell(7).setCellValue("1");

                useRow++;
                returnRow++;
            }
            if(machine9!=null){
                 sheet.getRow(useRow).getCell(1).setCellValue(machine9.getCode() == null ? "":machine9.getCode() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine9.getName() == null ? "":machine9.getName() );
                sheet.getRow(useRow).getCell(3).setCellValue(machine9.getSerialNumber() == null ? "":machine9.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1");
                
                sheet.getRow(returnRow).getCell(1).setCellValue(machine9.getCode() == null ? "":machine9.getCode() );
                sheet.getRow(returnRow).getCell(2).setCellValue(machine9.getName() == null ? "":machine9.getName() );
                sheet.getRow(returnRow).getCell(3).setCellValue(machine9.getSerialNumber() == null ? "":machine9.getSerialNumber() );
                sheet.getRow(returnRow).getCell(7).setCellValue("1");

                useRow++;
                returnRow++;
            }
            if(machine10!=null){
                 sheet.getRow(useRow).getCell(1).setCellValue(machine10.getCode() == null ? "":machine10.getCode() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine10.getName() == null ? "":machine10.getName() );
                sheet.getRow(useRow).getCell(3).setCellValue(machine10.getSerialNumber() == null ? "":machine10.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1");
                
                sheet.getRow(returnRow).getCell(1).setCellValue(machine10.getCode() == null ? "":machine10.getCode() );
                sheet.getRow(returnRow).getCell(2).setCellValue(machine10.getName() == null ? "":machine10.getName() );
                sheet.getRow(returnRow).getCell(3).setCellValue(machine10.getSerialNumber() == null ? "":machine10.getSerialNumber() );
                sheet.getRow(returnRow).getCell(7).setCellValue("1");

                useRow++;
            }

        String assignBU = caseMng.getAssignBu();
        User bu = userRepository.findByUsername(assignBU);
        InputStream signnature = new FileInputStream(signaturePath+assignBU);

        byte[] bytes = IOUtils.toByteArray(signnature);
        int digitalSignatureByte = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        signnature.close(); 
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor my_anchor = new XSSFClientAnchor();
        my_anchor.setCol1(5);
         my_anchor.setRow1(36);      //swap 
        XSSFPicture  my_picture = drawing.createPicture(my_anchor, digitalSignatureByte);
        my_picture.resize(0.3); 

            return workbook;
     }catch(Exception e){
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
        throw new RuntimeException(e);
    }
   }


    public XSSFWorkbook getInstallationReturn(CaseManagement caseMng){
     try{
         InputStream inp =  null;
         if("MC1".equalsIgnoreCase(caseMng.getMachine1().getMachineType())){
            inp = new FileInputStream(INSTALLATION_FILE_RETURN); 
        }else{
            inp = new FileInputStream(INSTALLATION_CLIARIA_FILE_RETURN); 
        }

          
        XSSFWorkbook  workbook  = new XSSFWorkbook(inp);
        inp.close();

        XSSFSheet sheet = workbook.getSheetAt(0);
        int useRow = 11;
        int returnRow = 22;
        // if(workbook!=null){
            LOGGER.debug("readFile");
            sheet.getRow(7).getCell(2).setCellValue(format.format(new Date()));
            sheet.getRow(7).getCell(4).setCellValue(caseMng.getInstallation().getInstallationPlace() == null ? "" :caseMng.getInstallation().getInstallationPlace() );
            Machine machine1 =caseMng.getMachine1();
            Machine machine2 =caseMng.getMachine2();
            Machine machine3 =caseMng.getMachine3();
            Machine machine4 =caseMng.getMachine4();
            Machine machine5 =caseMng.getMachine5();
            Machine machine6 =caseMng.getMachine6();
            Machine machine7 =caseMng.getMachine7();
            Machine machine8 =caseMng.getMachine8();
            Machine machine9 =caseMng.getMachine9();
            Machine machine10 =caseMng.getMachine10();
            
            if(machine1!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine1.getCode() == null ? "":machine1.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine1.getName() == null ? "":machine1.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine1.getSerialNumber() == null ? "":machine1.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1" );
                useRow++;
            }
            if(machine2!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine2.getCode() == null ? "":machine2.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine2.getName() == null ? "":machine2.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine2.getSerialNumber() == null ? "":machine2.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1" );
                useRow++;
            }
            if(machine3!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine3.getCode() == null ? "":machine3.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine3.getName() == null ? "":machine3.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine3.getSerialNumber() == null ? "":machine3.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1" );
                useRow++;
            }
            if(machine4!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine4.getCode() == null ? "":machine4.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine4.getName() == null ? "":machine4.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine4.getSerialNumber() == null ? "":machine4.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1" );
                useRow++;
            }
            if(machine5!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine5.getCode() == null ? "":machine5.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine5.getName() == null ? "":machine5.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine5.getSerialNumber() == null ? "":machine5.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1" );
                useRow++;
            }
            if(machine6!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine6.getCode() == null ? "":machine6.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine6.getName() == null ? "":machine6.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine6.getSerialNumber() == null ? "":machine6.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1" );
                useRow++;
            }
            if(machine7!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine7.getCode() == null ? "":machine7.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine7.getName() == null ? "":machine7.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine7.getSerialNumber() == null ? "":machine7.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1" );
                useRow++;
            }
            if(machine8!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine8.getCode() == null ? "":machine8.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine8.getName() == null ? "":machine8.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine8.getSerialNumber() == null ? "":machine8.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1" );
                useRow++;
            }
            if(machine9!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine9.getCode() == null ? "":machine9.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine9.getName() == null ? "":machine9.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine9.getSerialNumber() == null ? "":machine9.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1" );
                useRow++;
            }
            if(machine10!=null){
                sheet.getRow(useRow).getCell(0).setCellValue(machine10.getCode() == null ? "":machine10.getCode() );
                sheet.getRow(useRow).getCell(1).setCellValue(machine10.getName() == null ? "":machine10.getName() );
                sheet.getRow(useRow).getCell(2).setCellValue(machine10.getSerialNumber() == null ? "":machine10.getSerialNumber() );
                sheet.getRow(useRow).getCell(7).setCellValue("1" );
                useRow++;
            }

        String assignBU = caseMng.getAssignBu();
        User bu = userRepository.findByUsername(assignBU);
        InputStream signnature = new FileInputStream(signaturePath+assignBU);
        byte[] bytes = IOUtils.toByteArray(signnature);
        int digitalSignatureByte = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        signnature.close(); 
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor my_anchor = new XSSFClientAnchor();
        my_anchor.setCol1(5);
         my_anchor.setRow1(25);     
        XSSFPicture  my_picture = drawing.createPicture(my_anchor, digitalSignatureByte);
        my_picture.resize(0.3); 

            return workbook;
     }catch(Exception e){
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
        throw new RuntimeException(e);
    }
   }


   @Override
   public   XSSFWorkbook downloadFormInstallation(Long id){
    LOGGER.info("downloadFormInstallation : {}",id);
    try{
        XSSFWorkbook  workbook  = null;
        CaseManagement caseMng = caseManagementRepository.findOne(id);
        String caseType =    caseMng.getCaseType();
        if("AR".equalsIgnoreCase(caseType) || "CR".equalsIgnoreCase(caseType) ){
            workbook = getInstallations(caseMng);
        }else if("CH".equalsIgnoreCase(caseType) ){
            workbook = getInstallationChangeMachine(caseMng);
        }else if("RT".equalsIgnoreCase(caseType) ){
            workbook = getInstallationReturn(caseMng);
        }
        return workbook;
    }catch(Exception e){
        LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
        throw new RuntimeException(e);
    }
   }


   @Override
   public void sendDeposit(String json,MultipartFile file){
    try{
        LOGGER.info("sendDeposit :{}",json);
        JSONObject jsonObject = new JSONObject(json);
        CaseManagement caseManagement= caseManagementRepository.findOne(  Long.valueOf( jsonObject.get("id").toString()));
        caseManagement.setUpdatedDate(StandardUtil.getCurrentDate() );
        caseManagement.setUpdatedBy(  jsonObject.get("updatedBy").toString()  );
        caseManagement.setDepositName(jsonObject.get("depositName").toString());
        caseManagement.setDepositPaymentType(jsonObject.get("depositPaymentType").toString());
        caseManagement.setDepositBankName(jsonObject.get("depositBankName").toString());
        caseManagement.setDepositAmount(new BigDecimal ( jsonObject.get("depositAmount").toString()));
        Date depositDate =new SimpleDateFormat("dd-MM-yyyy").parse(jsonObject.get("depositDate").toString());
        caseManagement.setDepositDate(  new java.sql.Timestamp(depositDate.getTime()));
        // caseManagement.setAssignFn(jsonObject.get("updatedBy").toString());
        
        caseManagement.setDepositBy(jsonObject.get("updatedBy").toString());
        caseManagement.setFlagDeposit("Y");

        caseManagementRepository.save(caseManagement);
    }catch(Exception e){
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
        throw new RuntimeException(e);
    }
   }



   @Override
   public   XSSFWorkbook downloadFormPrescription(Long id){
    LOGGER.info("downloadFormPrescription : {}",id);
    try{
        InputStream inp = new FileInputStream(PRESCRIPTION_FILE); 
        XSSFWorkbook  workbook  = new XSSFWorkbook(inp);
        inp.close();
        XSSFSheet sheet = workbook.getSheetAt(0);
        int useRow = 11;
        if(workbook!=null){
            CaseManagement caseMng = caseManagementRepository.findOne(id);
            LOGGER.debug("readFile");
            Prescription prescription = caseMng.getPrescription();
            NurseMenu nurseMenu = prescription.getNurseMenu();
            MakeAdjustment makeAdjustment = prescription.getMakeAdjustment();
            ChangePrograme changePrograme = prescription.getChangePrograme();
            Opd opd = changePrograme.getOpd();
            Tidal tidal = changePrograme.getTidal();

            sheet.getRow(5).getCell(2).setCellValue(prescription.getPatientName());
            sheet.getRow(6).getCell(2).setCellValue(prescription.getHospitalName() == null ?"": prescription.getHospitalName());
            sheet.getRow(5).getCell(5).setCellValue(caseMng.getCaseNumber());
            sheet.getRow(6).getCell(5).setCellValue(prescription.getSerialNumber());
            sheet.getRow(12).getCell(2).setCellValue(nurseMenu.getModes() == null ?"":nurseMenu.getModes());
            sheet.getRow(13).getCell(2).setCellValue(nurseMenu.getMinDrawTime() == null ?"":nurseMenu.getMinDrawTime());
            sheet.getRow(14).getCell(2).setCellValue(nurseMenu.getMinDrawVol() == null ?"":nurseMenu.getMinDrawVol());
            sheet.getRow(15).getCell(2).setCellValue(nurseMenu.getNegOfTime() == null ?"":nurseMenu.getNegOfTime());
            sheet.getRow(16).getCell(2).setCellValue(nurseMenu.getPostOfLimit() == null ?"":nurseMenu.getPostOfLimit());
            sheet.getRow(17).getCell(2).setCellValue(nurseMenu.getSmartDwells() == null ?"":nurseMenu.getSmartDwells());
            sheet.getRow(18).getCell(2).setCellValue(nurseMenu.getHeaterBagEmpty() == null ?"":nurseMenu.getHeaterBagEmpty());
            sheet.getRow(19).getCell(2).setCellValue(nurseMenu.getTidalFullDrns() == null ?"":nurseMenu.getTidalFullDrns());
            sheet.getRow(20).getCell(2).setCellValue(nurseMenu.getLanguage() == null ?"":nurseMenu.getLanguage());
            sheet.getRow(21).getCell(2).setCellValue(nurseMenu.getFlush() == null ?"":nurseMenu.getFlush());
            sheet.getRow(22).getCell(2).setCellValue(nurseMenu.getProgramLocked() == null ?"":nurseMenu.getProgramLocked());
            sheet.getRow(23).getCell(2).setCellValue(nurseMenu.getWeightReset() == null ?"":nurseMenu.getWeightReset());
            sheet.getRow(12).getCell(4).setCellValue(makeAdjustment.getAdjustBrightness()==null?"":makeAdjustment.getAdjustBrightness());
            sheet.getRow(13).getCell(4).setCellValue(makeAdjustment.getAdjustLoundNess()==null?"":makeAdjustment.getAdjustLoundNess());
            sheet.getRow(14).getCell(4).setCellValue(makeAdjustment.getAutoDim()==null?"":makeAdjustment.getAutoDim());
            sheet.getRow(15).getCell(4).setCellValue(makeAdjustment.getSetClock()==null?"":makeAdjustment.getSetClock());
            sheet.getRow(16).getCell(4).setCellValue(makeAdjustment.getSetDail()==null?"":makeAdjustment.getSetDail());
            sheet.getRow(17).getCell(4).setCellValue(makeAdjustment.getDrainTimeMin()==null?"":makeAdjustment.getDrainTimeMin());
            sheet.getRow(18).getCell(4).setCellValue(makeAdjustment.getDrainAlram()==null?"":makeAdjustment.getDrainAlram());
            sheet.getRow(19).getCell(4).setCellValue(makeAdjustment.getComfortControll()==null?"":makeAdjustment.getComfortControll());
            sheet.getRow(20).getCell(4).setCellValue(makeAdjustment.getLastManualDrain()==null?"":makeAdjustment.getLastManualDrain());
            sheet.getRow(22).getCell(4).setCellValue(makeAdjustment.getAlram()==null?"":makeAdjustment.getAlram());
            sheet.getRow(23).getCell(4).setCellValue(makeAdjustment.getUrTarget()==null?"":makeAdjustment.getUrTarget());
            sheet.getRow(12).getCell(6).setCellValue(opd.getTherapy()==null?"":opd.getTherapy());
            sheet.getRow(13).getCell(6).setCellValue(opd.getTotalVol()==null?"":opd.getTotalVol());
            sheet.getRow(14).getCell(6).setCellValue(opd.getTherapyTime()==null?"":opd.getTherapyTime());
            sheet.getRow(15).getCell(6).setCellValue(opd.getFillVol()==null?"":opd.getFillVol());
            sheet.getRow(16).getCell(6).setCellValue(opd.getLastFillVol()==null?"":opd.getLastFillVol());
            sheet.getRow(17).getCell(6).setCellValue(opd.getDextRose()==null?"":opd.getDextRose());
            sheet.getRow(18).getCell(6).setCellValue(opd.getWeightUnit()==null?"":opd.getWeightUnit());
            sheet.getRow(19).getCell(6).setCellValue(opd.getPatientWeight()==null?"":opd.getPatientWeight());
            sheet.getRow(12).getCell(8).setCellValue(tidal.getTherapy() ==null?"":tidal.getTherapy());
            sheet.getRow(13).getCell(8).setCellValue(tidal.getTotalVol() ==null?"":tidal.getTotalVol());
            sheet.getRow(14).getCell(8).setCellValue(tidal.getTherapyTime() ==null?"":tidal.getTherapyTime());
            sheet.getRow(15).getCell(8).setCellValue(tidal.getFillVol() ==null?"":tidal.getFillVol());
            sheet.getRow(16).getCell(8).setCellValue(tidal.getTidalVol() ==null?"":tidal.getTidalVol());
            sheet.getRow(17).getCell(8).setCellValue(tidal.getTotalOf() ==null?"":tidal.getTotalOf());
            sheet.getRow(18).getCell(8).setCellValue(tidal.getLastFillVol() ==null?"":tidal.getLastFillVol());
            sheet.getRow(19).getCell(8).setCellValue(tidal.getDextRose() ==null?"":tidal.getDextRose());
            sheet.getRow(20).getCell(8).setCellValue(tidal.getWeightUnit()==null?"":tidal.getWeightUnit());
            sheet.getRow(21).getCell(8).setCellValue(tidal.getFullDrainEvery() ==null?"":tidal.getFullDrainEvery());
            sheet.getRow(22).getCell(8).setCellValue(tidal.getPatientWeight() ==null?"":tidal.getPatientWeight());
       
            sheet.getRow(28).getCell(3).setCellValue(caseMng.getCreatedBy() ==null?"":caseMng.getCreatedBy());
            sheet.getRow(28).getCell(5).setCellValue(format.format(new Date()));

        }

        return workbook;
    }catch(Exception e){
        LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
        throw new RuntimeException(e);
    }
   }


   public  List<Map<String,Object>> listDepositFn(String createdBy){
    try{
        return caseManagementRepositoryCustom.listDepositFn(createdBy);
    }catch(Exception e){
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
        throw new RuntimeException(e);
    }
   }
   public List<Map<String,Object>> listDepositTS(String createdBy){
     try{
        return caseManagementRepositoryCustom.listDepositTS(createdBy);
    }catch(Exception e){
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
        throw new RuntimeException(e);
    }
   }

   @Override
   public XSSFWorkbook downloadFormReceipt(Long id){
     try{
         InputStream inp = new FileInputStream(RECEIPT_FILE); 
        XSSFWorkbook  workbook  = new XSSFWorkbook(inp);
        inp.close();
        XSSFSheet sheet = workbook.getSheetAt(0);
         if(workbook!=null){
            CaseManagement caseMng = caseManagementRepository.findOne(id);
            String createDate =  caseMng.getReceiptDate() == null? "": FULL_DATE_FORMAT.format(caseMng.getReceiptDate()); 
            sheet.getRow(6).getCell(23).setCellValue(caseMng.getReceiptNo());
            sheet.getRow(11).getCell(21).setCellValue(createDate);

          
            sheet.getRow(9).getCell(4).setCellValue(caseMng.getReceipientName());
            sheet.getRow(10).getCell(4).setCellValue(caseMng.getReceiptAddress1());
            sheet.getRow(11).getCell(4).setCellValue(caseMng.getReceiptAddress2());
            
            Double amount  =  caseMng.getAmount() == null ?  new BigDecimal("0").doubleValue() : caseMng.getAmount().doubleValue()  ;  
            Double vat = new BigDecimal("0.07").doubleValue();
            vat = amount * 7d / 107d ;
            Double excludeVat  = amount - vat;

            sheet.getRow(21).getCell(18).setCellValue(excludeVat);
            sheet.getRow(21).getCell(25).setCellValue(excludeVat);
            sheet.getRow(31).getCell(25).setCellValue(excludeVat);
            
            sheet.getRow(32).getCell(25).setCellValue(vat );
            sheet.getRow(33).getCell(25).setCellValue(amount );


        }
        return workbook;
     }catch(Exception e){
         LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
        throw new RuntimeException(e);
    }
   }


   @Override
   public void uploadFile(String json,MultipartFile file){
    try{
            LOGGER.debug("uploadDigitalSignature :{}",json);
            JSONObject jsonObject = new JSONObject(json);
            String id = jsonObject.get("id").toString();
            String fileType = jsonObject.get("fileType").toString();
            // User user = userRepository.findByUsername(username);
            CaseManagement caseManagement = caseManagementRepository.findOne(Long.valueOf(id));
            // MultipartFile idCardFile = multipartHttpServletRequest.getFile("file");
            String path=PATH_FILE;
             
             // if(file!=null){
               FileUpload newfile = new FileUpload();
                byte[] bytes = file.getBytes();
                String FileName = caseManagement.getId() + "_" + fileType;
                FileCopyUtils.copy(bytes, new FileOutputStream(path+FileName));
                newfile.setFileName(file.getOriginalFilename());
                newfile.setFileType(fileType);
                newfile.setUpdatdDate(StandardUtil.getCurrentDate());
                newfile.setCaseManagement(caseManagement);
                fileUploadRepository.save(newfile);
                newfile.setFileUrl(IPSERVER+"?caseId="+caseManagement.getId()+"&fileType="+newfile.getFileType());
                // file.setFileUrl(IPSERVER+"casemanagement/downloadFileByCaseIdAndFileType?&caseId="+changeCase.getId()+"&fileType="+file.getFileType());
                fileUploadRepository.save(newfile);
            // }

        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
   }

   @Override
     public List<Map<String,Object>> listSwapMachine(String createdBy){
        try{
            return caseManagementRepositoryCustom.listSwapMachine(createdBy);
        }catch(Exception e){
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
     }


     @Override
     @Transactional
      public void returnMachine(String json){
        try{
            LOGGER.info("returnMachine : {}",json);
            JSONObject jsonObject = new JSONObject(json);
            String id = jsonObject.get("id").toString();
            String updatedBy = jsonObject.get("updatedBy").toString();
            String machineStatus = jsonObject.get("machineStatus").toString();
            CaseManagement caseManagement = caseManagementRepository.findOne(Long.valueOf(id));
            CaseManagement refCase = caseManagement.getRefCase();
            caseManagement.setUpdatedBy(updatedBy);
            caseManagement.setUpdatedDate(StandardUtil.getCurrentDate());
            caseManagement.setReturnBy(updatedBy);
            caseManagement.setFlagReturn("Y");
            // caseManagement.setAssignTs(updatedBy);
            // caseManagement.setAssignTs(updatedBy);
            // caseManagement.setAssignTs(updatedBy);
           

            if(refCase!=null){
                Machine machine1 =refCase.getMachine1();
                Machine machine2 =refCase.getMachine2();
                Machine machine3 =refCase.getMachine3();
                Machine machine4 =refCase.getMachine4();
                Machine machine5 =refCase.getMachine5();
                Machine machine6 =refCase.getMachine6();
                Machine machine7 =refCase.getMachine7();
                Machine machine8 =refCase.getMachine8();
                Machine machine9 =refCase.getMachine9();
                Machine machine10 =refCase.getMachine10();
                if(machine1!=null){
                    machine1.setStatus(Integer.valueOf(machineStatus));
                    machine1.setUpdatedBy(updatedBy);
                    machine1.setUpdatedDate(StandardUtil.getCurrentDate());
                    machineRepository.save(machine1);
                }
                 if(machine2!=null){
                    machine2.setStatus(Integer.valueOf(machineStatus));
                    machine2.setUpdatedBy(updatedBy);
                    machine1.setUpdatedDate(StandardUtil.getCurrentDate());
                    machineRepository.save(machine2);
                }
                 if(machine3!=null){
                    machine3.setStatus(Integer.valueOf(machineStatus));
                    machine3.setUpdatedBy(updatedBy);
                    machine3.setUpdatedDate(StandardUtil.getCurrentDate());
                    machineRepository.save(machine3);
                }
                 if(machine4!=null){
                    machine4.setStatus(Integer.valueOf(machineStatus));
                    machine4.setUpdatedBy(updatedBy);
                    machine4.setUpdatedDate(StandardUtil.getCurrentDate());
                    machineRepository.save(machine4);
                }
                 if(machine5!=null){
                    machine5.setStatus(Integer.valueOf(machineStatus));
                    machine5.setUpdatedBy(updatedBy);
                    machine5.setUpdatedDate(StandardUtil.getCurrentDate());
                    machineRepository.save(machine5);
                }
                 if(machine6!=null){
                    machine6.setStatus(Integer.valueOf(machineStatus));
                    machine6.setUpdatedBy(updatedBy);
                    machine6.setUpdatedDate(StandardUtil.getCurrentDate());
                    machineRepository.save(machine6);
                }
                 if(machine7!=null){
                    machine7.setStatus(Integer.valueOf(machineStatus));
                    machine7.setUpdatedBy(updatedBy);
                    machine7.setUpdatedDate(StandardUtil.getCurrentDate());
                    machineRepository.save(machine7);
                }
                 if(machine8!=null){
                    machine8.setStatus(Integer.valueOf(machineStatus));
                    machine8.setUpdatedBy(updatedBy);
                    machine8.setUpdatedDate(StandardUtil.getCurrentDate());
                    machineRepository.save(machine8);
                }
                 if(machine9!=null){
                    machine9.setStatus(Integer.valueOf(machineStatus));
                    machine9.setUpdatedBy(updatedBy);
                    machine9.setUpdatedDate(StandardUtil.getCurrentDate());
                    machineRepository.save(machine9);
                }
                 if(machine10!=null){
                    machine10.setStatus(Integer.valueOf(machineStatus));
                    machine10.setUpdatedBy(updatedBy);
                    machine10.setUpdatedDate(StandardUtil.getCurrentDate());
                    machineRepository.save(machine10);
                }
            }

            LOGGER.info("returnMachine complete");

        }catch(Exception e){
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
      }

    @Override
    public String getPayslipById(Long id){
        try{
            InputStream inputStream =null;
             String fileName = id + "_" + "PS";
            String pathFile ="/home/me/devNew/img/";

             String encodeImage = "";

            inputStream = new FileInputStream(pathFile+fileName);
            byte[] bytes= IOUtils.toByteArray(inputStream);
            byte[] encoded= Base64.encode(bytes);
            encodeImage = new String(encoded);
            return encodeImage;

        }catch(Exception e){
              LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }

}
