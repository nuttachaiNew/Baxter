package com.demo.spring.SpringBootOAuth2.service.impl;

import com.demo.spring.SpringBootOAuth2.domain.app.CaseManagement;
import com.demo.spring.SpringBootOAuth2.domain.app.FileUpload;
import com.demo.spring.SpringBootOAuth2.domain.app.Parameter;
import com.demo.spring.SpringBootOAuth2.domain.app.ParameterDetail;
import com.demo.spring.SpringBootOAuth2.domain.app.Machine;
import com.demo.spring.SpringBootOAuth2.domain.app.MachineHistory;

import com.demo.spring.SpringBootOAuth2.repository.CaseManagementRepository;
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
                    fileUpload.setUpdatedBy(user);
                    fileUpload.setUpdatdDate(StandardUtil.getCurrentDate());
                    fileUploadRepository.saveAndFlush(fileUpload);

                    //---Set FileUpload in set case and save
                    Set<FileUpload> setFileUpload = caseManagement.getFileUploads();
                    setFileUpload.add(fileUpload);
                    caseManagement.setFileUploads(setFileUpload);
                    caseManagementRepository.saveAndFlush(caseManagement);
                }

                File path = new File(pathFile + FileName + typeFile);
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

                String FileName = caseId + "_" + fileTpye;
                String pathFile = parameterDetail.getParameterValue();
                String originalFilename = fileUpload.getFileName();

                inputStream = new FileInputStream(pathFile+FileName+originalFilename.split("\\.")[1]);
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
    public Map<String,Object> saveCase(String json){
        try{
            LOGGER.debug("saveCase :{} ",json);
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);
            CaseManagement caseManagement = mapper.readValue(jsonObject.toString(),CaseManagement.class);
            caseManagement.setCreatedDate(StandardUtil.getCurrentDate());
            caseManagement.setCaseType("CR");
            caseManagementRepository.save(caseManagement);
          
            Map<String,Object> returnResult = new HashMap<>();
            returnResult.put("status","success");
            returnResult.put("caseNumber",caseManagement.getCaseNumber());

            return null;
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
    public synchronized Long autoGenerateMachineByTypeAndStatusEqActive(String machineType,String modelRef){
        LOGGER.info("autoGenerateMachineByTypeAndStatusEqActive : {} :{} ",machineType,modelRef);
        try{
            return caseManagementRepositoryCustom.autoGenerateMachineByTypeAndStatusEqActive(machineType,modelRef);
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

}