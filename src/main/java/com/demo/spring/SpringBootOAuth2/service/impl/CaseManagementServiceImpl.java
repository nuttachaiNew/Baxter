package com.demo.spring.SpringBootOAuth2.service.impl;

import com.demo.spring.SpringBootOAuth2.domain.app.CaseManagement;
import com.demo.spring.SpringBootOAuth2.domain.app.FileUpload;
import com.demo.spring.SpringBootOAuth2.domain.app.Parameter;
import com.demo.spring.SpringBootOAuth2.domain.app.ParameterDetail;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Set;

@Service
public class CaseManagementServiceImpl implements CaseManagementService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CaseManagementRepository caseManagementRepository;

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private ParameterDetailService parameterDetailService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private FileUploadRepository fileUploadRepository;

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

//    @RequestMapping(value = "/referencedoc/{filename}", method = RequestMethod.GET)
//    @ResponseBody
//    public void previewReferenceDoc(@PathVariable("filename") String filename, @RequestParam(value = "extension", required = false) String extension, HttpServletRequest request, HttpServletResponse response) {
//        try {
//            // Get Real Path On Environment
//            String contextPath  = request.getSession().getServletContext().getRealPath("/WEB-INF/file/" + filename);
//            File file = new File(contextPath);
//            logger.debug("contextPath {}" , contextPath);
//            // Set Response
//            if ("jpeg".equalsIgnoreCase(extension) || "png".equalsIgnoreCase(extension) || "gif".equalsIgnoreCase(extension) || "bmp".equalsIgnoreCase(extension)) {
//                response.setContentType("image/" + extension);
//                response.addHeader("Content-Disposition", "inline; filename=" + filename);
//            } else if ("pdf".equalsIgnoreCase(extension)) {
////                response.setContentType("application/" + extension);
//                response.setContentType("application/download");
//                response.addHeader("Content-Disposition", "inline; filename=" + filename + "." + extension);
//            }
//            response.setContentLength((int) file.length());
//            // Create File Stream Object
//            FileInputStream fileInputStream = new FileInputStream(file);
//            OutputStream responseOutputStream = response.getOutputStream();
//            byte[] buffer = new byte[4096];
//            int bytesRead = -1;
//            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
//                //logger.debug("bytesRead {}" , bytesRead);
//                responseOutputStream.write(buffer, 0, bytesRead);
//            }
//            // Close File Stream Object
//            fileInputStream.close();
//            responseOutputStream.close();
//        } catch (Exception e) {
//            logger.error("Error : {}",e.getMessage());
//        }
//    }

}