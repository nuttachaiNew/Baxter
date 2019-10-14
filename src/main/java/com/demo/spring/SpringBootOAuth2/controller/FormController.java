package com.demo.spring.SpringBootOAuth2.controller;

import com.demo.spring.SpringBootOAuth2.domain.app.User;
import com.demo.spring.SpringBootOAuth2.service.UserService;
import com.demo.spring.SpringBootOAuth2.util.AbstractReportJasperPDF;
import com.demo.spring.SpringBootOAuth2.util.ConstantVariableUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.OutputStream;
import com.demo.spring.SpringBootOAuth2.repository.CaseManagementRepository;
import com.demo.spring.SpringBootOAuth2.domain.app.CaseManagement;
import com.demo.spring.SpringBootOAuth2.service.CaseManagementService;
import javax.activation.MimetypesFileTypeMap;

@RestController
@CrossOrigin
@RequestMapping("/forms")
public class FormController {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Autowired
    private CaseManagementService caseManagementService;

    @Autowired
    CaseManagementRepository caseRepository;




    



    @RequestMapping(value = "/downloadFormAccept",method = RequestMethod.GET,headers = "Accept=application/json")
    void downloadFormHC( @RequestParam(value = "caseId",required = false)Long caseId
                                     ,  HttpServletResponse response)throws ServletException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        InputStream in = null;
        OutputStream outputStream=null;

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
            response.setContentType("application/x-pdf");
            response.addHeader("Content-Disposition", "attachment; filename=Acceptance_DOC.pdf");
            Map<String,Object> map = new HashMap<String,Object>();
            List<JasperPrint> jasperPrintList   = new ArrayList<>();
            CaseManagement caseManagement =  caseRepository.findOne(caseId);
            if("CR".equalsIgnoreCase(caseManagement.getCaseType())){
                
                if("MC1".equalsIgnoreCase(caseManagement.getMachine1().getMachineType())){
                    map.put("name",caseManagement.getCustomer().getPatientName() + " "+ caseManagement.getCustomer().getPatientLastName());
                    map.put("national_id",caseManagement.getCustomer().getNationId());
                    map.put("no",caseManagement.getCustomer().getCurrentAddress1());
                    map.put("sub_district",caseManagement.getCustomer().getCurrentSubDistrict());
                    map.put("district",caseManagement.getCustomer().getCurrentDistrict());
                    map.put("province",caseManagement.getCustomer().getCurrentProvince());
                    map.put("zipcode",caseManagement.getCustomer().getCurrentZipCode());
                    map.put("tel_no",caseManagement.getCustomer().getTelNo());
                    map.put("active_date",format.format(new Date()));
                    map.put("age","");

                    User user = userService.findUserByUsername("temp");

                    String jasperFileName1 = "HC1.jasper";
                    String jasperFileName2 = "HC2.jasper";
                    JasperPrint jasperPrint1 = AbstractReportJasperPDF.exportReport(jasperFileName1,Arrays.asList(user),map);
                    JasperPrint jasperPrint2 = AbstractReportJasperPDF.exportReport(jasperFileName2,Arrays.asList(user),map);
                    jasperPrintList.add(jasperPrint1);
                    jasperPrintList.add(jasperPrint2);

                    byte[] b = generateReportForm(jasperPrintList);
                    in = new ByteArrayInputStream(b);
                    outputStream = response.getOutputStream();
                    IOUtils.copy(in, outputStream);
               

                }else{
                        map.put("name",caseManagement.getCustomer().getPatientName() + " "+ caseManagement.getCustomer().getPatientLastName());
                        map.put("national_id",caseManagement.getCustomer().getNationId());
                        map.put("no",caseManagement.getCustomer().getCurrentAddress1());
                        map.put("sub_district",caseManagement.getCustomer().getCurrentSubDistrict());
                        map.put("district",caseManagement.getCustomer().getCurrentDistrict());
                        map.put("province",caseManagement.getCustomer().getCurrentProvince());
                        map.put("zipcode",caseManagement.getCustomer().getCurrentZipCode());
                        map.put("tel_no",caseManagement.getCustomer().getTelNo());
                        map.put("active_date",format.format(new Date()));
                        map.put("age","");
                        map.put("image", ConstantVariableUtil.PATH_IMAGE_FOR_JASPER);

                        User user = userService.findUserByUsername("temp");

                        String jasperFileName1 = "HC_1_1.jasper";
                        String jasperFileName2 = "HC_1_2.jasper";
                        JasperPrint jasperPrint1 = AbstractReportJasperPDF.exportReport(jasperFileName1,Arrays.asList(user),map);
                        JasperPrint jasperPrint2 = AbstractReportJasperPDF.exportReport(jasperFileName2,Arrays.asList(user),map);
                        jasperPrintList.add(jasperPrint1);
                        jasperPrintList.add(jasperPrint2);

                        byte[] b = generateReportForm(jasperPrintList);
                        in = new ByteArrayInputStream(b);
                        outputStream = response.getOutputStream();
                        IOUtils.copy(in, outputStream);

                }

            }else{
                 String date = format.format(new Date());
                String dateSplit[] = date.split("-");
                map.put("day",dateSplit[0]);
                map.put("month",dateSplit[1]);
                map.put("year",dateSplit[2]);
                map.put("customer",caseManagement.getCustomer().getPatientName() + " "+ caseManagement.getCustomer().getPatientLastName());
                map.put("serial",caseManagement.getMachine1().getSerialNumber());

                User user = userService.findUserByUsername("temp");

                String jasperFileName1 = "EQUIPMENT_PLACEMENT_AGREEMENT.jasper";
                JasperPrint jasperPrint1 = AbstractReportJasperPDF.exportReport(jasperFileName1,Arrays.asList(user),map);
                jasperPrintList.add(jasperPrint1);

                byte[] b = generateReportForm(jasperPrintList);
                in = new ByteArrayInputStream(b);
                outputStream = response.getOutputStream();
                IOUtils.copy(in, outputStream);
            }

           
        }catch (Exception e) {
            LOGGER.error("ERROR : {}",e);
        }finally{
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(in);
        }
    }

    @RequestMapping(value = "/downloadFormEquipment",method = RequestMethod.GET,headers = "Accept=application/json")
    void downloadFormEquipment( @RequestParam(value = "caseId",required = false)Long caseId
                                                , HttpServletResponse response)throws ServletException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        InputStream in = null;
        OutputStream outputStream=null;

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
            response.setContentType("application/x-pdf");
            response.addHeader("Content-Disposition", "attachment; filename=Equipment.pdf");
            Map<String,Object> map = new HashMap<String,Object>();
            List<JasperPrint> jasperPrintList   = new ArrayList<>();
            CaseManagement caseManagement =  caseRepository.findOne(caseId);
            String date = format.format(new Date());
            String dateSplit[] = date.split("-");
            map.put("day",dateSplit[0]);
            map.put("month",dateSplit[1]);
            map.put("year",dateSplit[2]);
            map.put("customer",caseManagement.getCustomer().getPatientName() + " "+ caseManagement.getCustomer().getPatientLastName());
            map.put("serial",caseManagement.getMachine1().getSerialNumber());

            User user = userService.findUserByUsername("temp");

            String jasperFileName1 = "EQUIPMENT_PLACEMENT_AGREEMENT.jasper";
            JasperPrint jasperPrint1 = AbstractReportJasperPDF.exportReport(jasperFileName1,Arrays.asList(user),map);
            jasperPrintList.add(jasperPrint1);

            byte[] b = generateReportForm(jasperPrintList);
            in = new ByteArrayInputStream(b);
            outputStream = response.getOutputStream();
            IOUtils.copy(in, outputStream);
        }catch (Exception e) {
            LOGGER.error("ERROR : {}",e);
        }finally{
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(in);
        }
    }


    // @PostMapping("/downloadFormAccept")
    

    public byte[] generateReportForm(List<JasperPrint> jasperPrintList) throws JRException {
        try{
            LOGGER.info("######### -= generateReportForm =- ########");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    JRPdfExporter exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
                    exporter.exportReport();
                    return baos.toByteArray();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }



    @RequestMapping(value = "/downloadFormInstallation",method = RequestMethod.GET,headers = "Accept=application/json")
    public ResponseEntity<String> downloadFormInstallation(@RequestParam(value = "caseId",required = false)Long caseId,
                                                      HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        LOGGER.info("downloadFormInstallation[Controller]");
        try {

            // response.setContentType("application/x-pdf");
            // response.addHeader("Content-Disposition", "attachment; filename=Acceptance_DOC.pdf");
            String mimeType = new MimetypesFileTypeMap().getContentType("installation.xlsx");
            response.setContentType(mimeType);
            response.addHeader("Content-Disposition","attachment; filename*=UTF-8''"+"installation.xlsx");

            XSSFWorkbook workbook = caseManagementService.downloadFormInstallation(caseId);
            if(workbook != null){
                workbook.write(response.getOutputStream());
                response.getOutputStream().flush();
            }
            LOGGER.info("downloadFormInstallation[Controller]=================================");
            headers.add("statusValidate","0");
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            LOGGER.error("downloadFormInstallation[Controller] error msg : {}",e.getMessage());
            headers.add("statusValidate","-1");
            headers.add("errorMsg",e.getMessage());
            return new ResponseEntity<String>(e.getMessage(),headers, HttpStatus.OK);
        }
    }

     @RequestMapping(value = "/downloadFormPrescription",method = RequestMethod.GET,headers = "Accept=application/json")
    public ResponseEntity<String> downloadFormPrescription(@RequestParam(value = "caseId",required = false)Long caseId,
                                                      HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        LOGGER.info("downloadFormPrescription[Controller]");
        try {
             String mimeType = new MimetypesFileTypeMap().getContentType("prescription.xlsx");
            response.setContentType(mimeType);
            response.addHeader("Content-Disposition","attachment; filename*=UTF-8''"+"prescription.xlsx");
            XSSFWorkbook workbook = caseManagementService.downloadFormPrescription(caseId);
            if(workbook != null){
                workbook.write(response.getOutputStream());
                response.getOutputStream().flush();
            }
            LOGGER.info("downloadFormPrescription[Controller]=================================");
            headers.add("statusValidate","0");
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            LOGGER.error("downloadFormPrescription[Controller] error msg : {}",e.getMessage());
            headers.add("statusValidate","-1");
            headers.add("errorMsg",e.getMessage());
            return new ResponseEntity<String>(e.getMessage(),headers, HttpStatus.OK);
        }
    }

     @RequestMapping(value = "/downloadFormReceipt",method = RequestMethod.GET,headers = "Accept=application/json")
    public ResponseEntity<String> downloadFormReceipt(@RequestParam(value = "caseId",required = false)Long caseId,
                                                      HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        LOGGER.info("downloadFormReceipt[Controller]");
        try {
            String mimeType = new MimetypesFileTypeMap().getContentType("recepit.xlsx");
            response.setContentType(mimeType);
            response.addHeader("Content-Disposition","attachment; filename*=UTF-8''"+"recepit.xlsx");
            XSSFWorkbook workbook = caseManagementService.downloadFormReceipt(caseId);
            if(workbook != null){
                workbook.write(response.getOutputStream());
                response.getOutputStream().flush();
            }
            LOGGER.info("downloadFormReceipt[Controller]=================================");
            headers.add("statusValidate","0");
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            LOGGER.error("downloadFormInstallation[Controller] error msg : {}",e.getMessage());
            headers.add("statusValidate","-1");
            headers.add("errorMsg",e.getMessage());
            return new ResponseEntity<String>(e.getMessage(),headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/downloadFormAcceptforWebView",method = RequestMethod.GET,headers = "Accept=application/json")
    void downloadFormAcceptforWebView( @RequestParam(value = "caseId",required = false)Long caseId
                                     ,  HttpServletResponse response)throws ServletException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        InputStream in = null;
        OutputStream outputStream=null;
	//response.setHeader("Content-Disposition", "attachment;filename=ACCEPT.pdf;");
        response.setContentType("application/pdf");

	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
              Map<String,Object> map = new HashMap<String,Object>();
            List<JasperPrint> jasperPrintList   = new ArrayList<>();
            CaseManagement caseManagement =  caseRepository.findOne(caseId);
            if("CR".equalsIgnoreCase(caseManagement.getCaseType())){
                            if("MC1".equalsIgnoreCase(caseManagement.getMachine1().getMachineType())){
                    map.put("name",caseManagement.getCustomer().getPatientName() + " "+ caseManagement.getCustomer().getPatientLastName());
                    map.put("national_id",caseManagement.getCustomer().getNationId());
                    map.put("no",caseManagement.getCustomer().getCurrentAddress1());
                    map.put("sub_district",caseManagement.getCustomer().getCurrentSubDistrict());
                    map.put("district",caseManagement.getCustomer().getCurrentProvince());
                    map.put("province",caseManagement.getCustomer().getCurrentProvince());
                    map.put("zipcode",caseManagement.getCustomer().getCurrentZipCode());
                    map.put("tel_no",caseManagement.getCustomer().getTelNo());
                    map.put("active_date",format.format(new Date()));
                    map.put("age","");

                    User user = userService.findUserByUsername("temp");

                    String jasperFileName1 = "HC1.jasper";
                    String jasperFileName2 = "HC2.jasper";
                    JasperPrint jasperPrint1 = AbstractReportJasperPDF.exportReport(jasperFileName1,Arrays.asList(user),map);
                    JasperPrint jasperPrint2 = AbstractReportJasperPDF.exportReport(jasperFileName2,Arrays.asList(user),map);
                    jasperPrintList.add(jasperPrint1);
                    jasperPrintList.add(jasperPrint2);

                    byte[] b = generateReportForm(jasperPrintList);
                    in = new ByteArrayInputStream(b);
                    outputStream = response.getOutputStream();
                    IOUtils.copy(in, outputStream);
               

                }else{
                        map.put("name",caseManagement.getCustomer().getPatientName() + " "+ caseManagement.getCustomer().getPatientLastName());
                        map.put("national_id",caseManagement.getCustomer().getNationId());
                        map.put("no",caseManagement.getCustomer().getCurrentAddress1());
                        map.put("sub_district",caseManagement.getCustomer().getCurrentSubDistrict());
                        map.put("district",caseManagement.getCustomer().getCurrentProvince());
                        map.put("province",caseManagement.getCustomer().getCurrentProvince());
                        map.put("zipcode",caseManagement.getCustomer().getCurrentZipCode());
                        map.put("tel_no",caseManagement.getCustomer().getTelNo());
                        map.put("active_date",format.format(new Date()));
                        map.put("age","");
                        map.put("image", ConstantVariableUtil.PATH_IMAGE_FOR_JASPER);

                        User user = userService.findUserByUsername("temp");

                        String jasperFileName1 = "HC_1_1.jasper";
                        String jasperFileName2 = "HC_1_2.jasper";
                        JasperPrint jasperPrint1 = AbstractReportJasperPDF.exportReport(jasperFileName1,Arrays.asList(user),map);
                        JasperPrint jasperPrint2 = AbstractReportJasperPDF.exportReport(jasperFileName2,Arrays.asList(user),map);
                        jasperPrintList.add(jasperPrint1);
                        jasperPrintList.add(jasperPrint2);

                        byte[] b = generateReportForm(jasperPrintList);
                        in = new ByteArrayInputStream(b);
                        outputStream = response.getOutputStream();
                        IOUtils.copy(in, outputStream);

                }


            }else{
                 String date = format.format(new Date());
                String dateSplit[] = date.split("-");
                map.put("day",dateSplit[0]);
                map.put("month",dateSplit[1]);
                map.put("year",dateSplit[2]);
                map.put("customer",caseManagement.getCustomer().getPatientName() + " "+ caseManagement.getCustomer().getPatientLastName());
                map.put("serial",caseManagement.getMachine1().getSerialNumber());

                User user = userService.findUserByUsername("temp");

                String jasperFileName1 = "EQUIPMENT_PLACEMENT_AGREEMENT.jasper";
                JasperPrint jasperPrint1 = AbstractReportJasperPDF.exportReport(jasperFileName1,Arrays.asList(user),map);
                jasperPrintList.add(jasperPrint1);

                byte[] b = generateReportForm(jasperPrintList);
                in = new ByteArrayInputStream(b);
                outputStream = response.getOutputStream();
                IOUtils.copy(in, outputStream);
            }

           
        }catch (Exception e) {
            LOGGER.error("ERROR : {}",e);
        }finally{
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(in);
        }
    }

    @RequestMapping(value = "/generateAgreement",method = RequestMethod.GET,headers = "Accept=application/json")
    void generateAgreement( 

        @RequestParam(value = "caseType",required = false)String caseType
       ,@RequestParam(value = "customerName",required = false)String customerName
       ,@RequestParam(value = "customerLastName",required = false)String customerLastName
       ,@RequestParam(value = "nationalId",required = false)String nationalId
       ,@RequestParam(value = "address1",required = false)String address1
       ,@RequestParam(value = "subDistrict",required = false)String subDistrict
       ,@RequestParam(value = "district",required = false)String district
       ,@RequestParam(value = "province",required = false)String province
       ,@RequestParam(value = "zipCode",required = false)String zipCode
       ,@RequestParam(value = "telNo",required = false)String telNo
       ,@RequestParam(value = "serialNo",required = false)String serialNo
       ,@RequestParam(value = "machineType",required = false)String machineType
        
                                     ,  HttpServletResponse response)throws ServletException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        InputStream in = null;
        OutputStream outputStream=null;
    //response.setHeader("Content-Disposition", "attachment;filename=ACCEPT.pdf;");
        response.setContentType("application/pdf");

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
            response.setContentType("application/x-pdf");
            response.addHeader("Content-Disposition", "attachment; filename=Acceptance_DOC.pdf");
            Map<String,Object> map = new HashMap<String,Object>();
            List<JasperPrint> jasperPrintList   = new ArrayList<>();
            if("CR".equalsIgnoreCase(caseType)){
                
                if("MC1".equalsIgnoreCase(machineType)){
                    map.put("name",customerName +" "+customerLastName);
                    map.put("national_id",nationalId);
                    map.put("no",address1);
                    map.put("sub_district",subDistrict);
                    map.put("district",district);
                    map.put("province",province);
                    map.put("zipcode",zipCode);
                    map.put("tel_no",telNo);
                    map.put("active_date",format.format(new Date()));
                    map.put("age","");

                    User user = userService.findUserByUsername("temp");

                    String jasperFileName1 = "HC1.jasper";
                    String jasperFileName2 = "HC2.jasper";
                    JasperPrint jasperPrint1 = AbstractReportJasperPDF.exportReport(jasperFileName1,Arrays.asList(user),map);
                    JasperPrint jasperPrint2 = AbstractReportJasperPDF.exportReport(jasperFileName2,Arrays.asList(user),map);
                    jasperPrintList.add(jasperPrint1);
                    jasperPrintList.add(jasperPrint2);

                    byte[] b = generateReportForm(jasperPrintList);
                    in = new ByteArrayInputStream(b);
                    outputStream = response.getOutputStream();
                    IOUtils.copy(in, outputStream);
               

                }else{
                         map.put("name",customerName +" "+customerLastName);
                        map.put("national_id",nationalId);
                        map.put("no",address1);
                        map.put("sub_district",subDistrict);
                        map.put("district",district);
                        map.put("province",province);
                        map.put("zipcode",zipCode);
                        map.put("tel_no",telNo);
                        map.put("active_date",format.format(new Date()));
                        map.put("age","");
                        map.put("image", ConstantVariableUtil.PATH_IMAGE_FOR_JASPER);

                        User user = userService.findUserByUsername("temp");

                        String jasperFileName1 = "HC_1_1.jasper";
                        String jasperFileName2 = "HC_1_2.jasper";
                        JasperPrint jasperPrint1 = AbstractReportJasperPDF.exportReport(jasperFileName1,Arrays.asList(user),map);
                        JasperPrint jasperPrint2 = AbstractReportJasperPDF.exportReport(jasperFileName2,Arrays.asList(user),map);
                        jasperPrintList.add(jasperPrint1);
                        jasperPrintList.add(jasperPrint2);

                        byte[] b = generateReportForm(jasperPrintList);
                        in = new ByteArrayInputStream(b);
                        outputStream = response.getOutputStream();
                        IOUtils.copy(in, outputStream);

                }

            }else{
                 String date = format.format(new Date());
                String dateSplit[] = date.split("-");
                map.put("day",dateSplit[0]);
                map.put("month",dateSplit[1]);
                map.put("year",dateSplit[2]);
                map.put("customer",customerName +" "+customerLastName);
                map.put("serial",serialNo);

                User user = userService.findUserByUsername("temp");

                String jasperFileName1 = "EQUIPMENT_PLACEMENT_AGREEMENT.jasper";
                JasperPrint jasperPrint1 = AbstractReportJasperPDF.exportReport(jasperFileName1,Arrays.asList(user),map);
                jasperPrintList.add(jasperPrint1);

                byte[] b = generateReportForm(jasperPrintList);
                in = new ByteArrayInputStream(b);
                outputStream = response.getOutputStream();
                IOUtils.copy(in, outputStream);
            }

           
        }catch (Exception e) {
            LOGGER.error("ERROR : {}",e);
        }finally{
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(in);
        }
    }
    

    @RequestMapping(value = "/downloadFormHCCliria",method = RequestMethod.GET,headers = "Accept=application/json")
    void downloadFormHCCliria( @RequestParam(value = "caseId",required = false)Long caseId
            ,  HttpServletResponse response)throws ServletException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        InputStream in = null;
        OutputStream outputStream=null;

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
            Map<String,Object> map = new HashMap<String,Object>();
            List<JasperPrint> jasperPrintList   = new ArrayList<>();
            CaseManagement caseManagement =  caseRepository.findOne(caseId);

                map.put("name",caseManagement.getCustomer().getPatientName() + " "+ caseManagement.getCustomer().getPatientLastName());
                map.put("national_id",caseManagement.getCustomer().getNationId());
                map.put("no",caseManagement.getCustomer().getCurrentAddress1());
                map.put("sub_district",caseManagement.getCustomer().getCurrentSubDistrict());
                map.put("district",caseManagement.getCustomer().getCurrentProvince());
                map.put("province",caseManagement.getCustomer().getCurrentProvince());
                map.put("zipcode",caseManagement.getCustomer().getCurrentZipCode());
                map.put("tel_no",caseManagement.getCustomer().getTelNo());
                map.put("active_date",format.format(new Date()));
                map.put("age","");
                map.put("image", ConstantVariableUtil.PATH_IMAGE_FOR_JASPER);

                User user = userService.findUserByUsername("temp");

                String jasperFileName1 = "HC_1_1.jasper";
                String jasperFileName2 = "HC_1_2.jasper";
                JasperPrint jasperPrint1 = AbstractReportJasperPDF.exportReport(jasperFileName1,Arrays.asList(user),map);
                JasperPrint jasperPrint2 = AbstractReportJasperPDF.exportReport(jasperFileName2,Arrays.asList(user),map);
                jasperPrintList.add(jasperPrint1);
                jasperPrintList.add(jasperPrint2);

                byte[] b = generateReportForm(jasperPrintList);
                in = new ByteArrayInputStream(b);
                outputStream = response.getOutputStream();
                IOUtils.copy(in, outputStream);

        }catch (Exception e) {
            LOGGER.error("ERROR : {}",e);
        }finally{
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(in);
        }
    }

}
