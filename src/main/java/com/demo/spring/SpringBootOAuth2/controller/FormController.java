package com.demo.spring.SpringBootOAuth2.controller;

import com.demo.spring.SpringBootOAuth2.domain.app.User;
import com.demo.spring.SpringBootOAuth2.service.UserService;
import com.demo.spring.SpringBootOAuth2.util.AbstractReportJasperPDF;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.OutputStream;

@RestController
@CrossOrigin
@RequestMapping("/forms")
public class FormController {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @RequestMapping(value = "/downloadFormHC",method = RequestMethod.GET,headers = "Accept=application/json")
    void downloadFormHC(@RequestParam(value = "name",required = false)String name,
                                                           @RequestParam(value = "nationalId",required = false)String nationalId,
                                                           @RequestParam(value = "no",required = false)String no,
                                                           @RequestParam(value = "subDistrict",required = false)String subDistrict,
                                                           @RequestParam(value = "district",required = false)String district,
                                                           @RequestParam(value = "province",required = false)String province,
                                                           @RequestParam(value = "zipcode",required = false)String zipcode,
                                                           @RequestParam(value = "telNo",required = false)String telNo,
                                                           @RequestParam(value = "age",required = false)String age,
                                                           HttpServletResponse response)throws ServletException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        InputStream in = null;
        OutputStream outputStream=null;

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
            response.setContentType("application/x-pdf");
            response.addHeader("Content-Disposition", "attachment; filename=HC.pdf");
            Map<String,Object> map = new HashMap<String,Object>();
            List<JasperPrint> jasperPrintList   = new ArrayList<>();
            map.put("name",name);
            map.put("national_id",nationalId);
            map.put("no",no);
            map.put("sub_district",subDistrict);
            map.put("district",district);
            map.put("province",province);
            map.put("zipcode",zipcode);
            map.put("tel_no",telNo);
            map.put("active_date",format.format(new Date()));
            map.put("age",age);

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
//            return new ResponseEntity<String>(headers, HttpStatus.OK);
        }catch (Exception e) {
            LOGGER.error("ERROR : {}",e);
//            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.OK);
        }finally{
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(in);
        }
    }

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
}
