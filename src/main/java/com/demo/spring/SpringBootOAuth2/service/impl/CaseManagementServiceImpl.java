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

import com.demo.spring.SpringBootOAuth2.repository.CaseManagementRepository;
import com.demo.spring.SpringBootOAuth2.repository.CustomerRepository;

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

  private static DecimalFormat FORMAT_RUNNING = new DecimalFormat("00");
  private static DecimalFormat FORMAT_YEAR = new DecimalFormat("0000");
  private static DecimalFormat FORMAT_MONTH = new DecimalFormat("00");
  private static final SimpleDateFormat GEN_CASE_DATEFORMAT = new SimpleDateFormat("MM-yyyy", Locale.US);


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
            Parameter parameter = parameterService.findAppParameterByAppParameterCode(ConstantVariableUtil.PARAMETER_PATH_FILE_UPLOAD);
            ParameterDetail parameterDetail = parameterDetailService.findParameterDetailByCodeAndAppParameter(ConstantVariableUtil.PARAMETER_DETAIL_PATH_FILE_UPLOAD,parameter.getId());

            FileUpload fileUpload = fileUploadService.findFileUploadByCaseIdAndFileType(caseId,fileTpye);

            if(fileUpload != null){

                String fileName = caseId + "_" + fileTpye;
                String pathFile = parameterDetail.getParameterValue();
                String originalFilename = fileUpload.getFileName();

                inputStream = new FileInputStream(pathFile+fileName+ "." +originalFilename.split("\\.")[1]);
            }else{
                throw new RuntimeException("File not found.");
            }

        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
        }
        return inputStream;
    }

    @Override
    @Transactional
    public Map<String,Object> saveCase(String json,MultipartHttpServletRequest multipartHttpServletRequest){
        try{
            LOGGER.debug("saveCase :{} ",json);
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);
            // CaseManagement caseManagement = mapper.readValue(jsonObject.toString(),CaseManagement.class);
            CaseManagement caseManagement = new JSONDeserializer<CaseManagement>().use(null, CaseManagement.class).deserialize(jsonObject.toString());

            
            Map<String,Object> customerDtl = new JSONDeserializer<Map<String,Object>>().deserialize(jsonObject.get("customer").toString());
            List<Map<String,Object>> machineData = new JSONDeserializer<List<Map<String,Object>>>().deserialize(jsonObject.get("machine").toString());
           
            // create Customer
            Customer customer = new Customer();
            customer.setCustomerType( customerDtl.get("customerType")==null?null:customerDtl.get("customerType").toString() );
            customer.setHospitalName( customerDtl.get("hospitalName")==null?null:customerDtl.get("hospitalName").toString() );
            customer.setPatientName( customerDtl.get("patientName")==null?null:customerDtl.get("patientName").toString() );
            customer.setNationId( customerDtl.get("nationId")==null?null:customerDtl.get("nationId").toString() );
            customer.setTelNo( customerDtl.get("telNo")==null?null:customerDtl.get("telNo").toString() );
            customer.setCurrentAddress1( customerDtl.get("currentAddress1")==null?null:customerDtl.get("currentAddress1").toString() );
            customer.setCurrentAddress2( customerDtl.get("currentAddress2")==null?null:customerDtl.get("currentAddress2").toString() );
            customer.setCurrentSubDistrict( customerDtl.get("currentSubDistrict")==null?null:customerDtl.get("currentSubDistrict").toString() );
            customer.setCurrentDistrict( customerDtl.get("currentDistrict")==null?null:customerDtl.get("currentDistrict").toString() );
            customer.setCurrentProvince( customerDtl.get("currentProvince")==null?null:customerDtl.get("currentProvince").toString() );
            customer.setCurrentZipCode( customerDtl.get("currentZipCode")==null?null:customerDtl.get("currentZipCode").toString() );
            
            customer.setShippingAddress1( customerDtl.get("shippingAddress1")==null?null:customerDtl.get("shippingAddress1").toString() );
            customer.setShippingAddress2( customerDtl.get("shippingAddress2")==null?null:customerDtl.get("shippingAddress2").toString() );
            customer.setShippingSubDistrict( customerDtl.get("shippingSubDistrict")==null?null:customerDtl.get("shippingSubDistrict").toString() );
            customer.setShippingDistrict( customerDtl.get("shippingDistrict")==null?null:customerDtl.get("shippingDistrict").toString() );
            customer.setShippingProvince( customerDtl.get("shippingProvince")==null?null:customerDtl.get("shippingProvince").toString() );
            customer.setShippingZipCode( customerDtl.get("shippingZipCode")==null?null:customerDtl.get("shippingZipCode").toString() );
            
            customerRepository.saveAndFlush(customer);
            // 
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
            caseManagement.setCaseStatus("I");
            // init caseActivity
            CaseActivity caseActivity = new CaseActivity();
            Set<CaseActivity> activitys = new HashSet<>();
            User user = userRepository.findByUsername(caseManagement.getCreatedBy());
            caseActivity.setUser(user);
            caseActivity.setActionStatus("create case");
            caseActivity.setActionDate(StandardUtil.getCurrentDate());
            caseActivity.setCaseManagement(caseManagement);
            caseActivityRepository.save(caseActivity);
            activitys.add(caseActivity);
            caseManagement.setAreaId(user.getBranch().getId());
            caseManagement.setCaseActivitys(activitys);
            caseManagementRepository.save(caseManagement);
            Map<String,Object> returnResult = new HashMap<>();
            returnResult.put("status","success");
            returnResult.put("caseNumber",caseManagement.getCaseNumber());

            return returnResult;
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
            String createDate =  GEN_CASE_DATEFORMAT.format(today); 
             LOGGER.info("generateCaseNumber :{} :{} ",caseType,createDate);

            List<String> caseNumberMax = caseManagementRepositoryCustom.getLastedCaseNumberByCriteria(caseType,createDate);
            String maxNumber = "";
            String caseNumber = "";
            if( caseNumberMax.size()>0 ){
                maxNumber = caseNumberMax.get(0);
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
    public void submitToASM(Long id,String updBy){
        try{
 //  I > W > R|F > W > F
            LOGGER.debug("submitToASM :{} :{}",id,updBy);
            CaseManagement caseMng = caseManagementRepository.findOne(id);
            if(caseMng.getCaseStatus().equalsIgnoreCase("I") || caseMng.getCaseStatus().equalsIgnoreCase("R")  ){
                caseMng.setCaseStatus("W");
                caseMng.setUpdatedBy(updBy);
                caseMng.setUpdatedDate(StandardUtil.getCurrentDate());
                Set<CaseActivity> caseActivitys = caseMng.getCaseActivitys();
                CaseActivity caseAct = new CaseActivity();
                User user = userRepository.findByUsername( updBy );
                caseAct.setUser(user);
                caseAct.setActionStatus("submit to ASM");
                caseAct.setActionDate(StandardUtil.getCurrentDate());
                caseAct.setCaseManagement(caseMng);    
                caseActivityRepository.save(caseAct);
                caseActivitys.add(caseAct);
                caseMng.setCaseActivitys(caseActivitys);
                caseManagementRepository.save(caseMng);
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
    public  List<Map<String,Object>> findHistoryDocByAreaAndDocStatusAndRoleAndCase(String createdBy,Long areaId,String documentStatus,String roleBy){
        try{
            LOGGER.debug("findHistoryDocByAreaAndDocStatusAndRoleAndCase :{} : {} :{} :{}",createdBy,areaId,documentStatus,roleBy);
            return caseManagementRepositoryCustom.findHistoryDocByAreaAndDocStatusAndRoleAndCase(createdBy,areaId,documentStatus,roleBy);
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }

   @Override
   public  List<Map<String,Object>> findCaseByCriteria(String date, String caseNumber , String areaId ,String description ,Integer firstResult ,Integer maxResult){
     try{
        LOGGER.info("findCaseByCriteria : {}",date);
        return caseManagementRepositoryCustom.findCaseByCriteria(date,caseNumber,areaId,description,firstResult,maxResult);
     }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("ERROR -> : {}-{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
   } 

}