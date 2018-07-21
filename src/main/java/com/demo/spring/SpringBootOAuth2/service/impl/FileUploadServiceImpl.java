package com.demo.spring.SpringBootOAuth2.service.impl;

import com.demo.spring.SpringBootOAuth2.domain.app.FileUpload;
import com.demo.spring.SpringBootOAuth2.repository.FileUploadRepositoryCustom;
import com.demo.spring.SpringBootOAuth2.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FileUploadRepositoryCustom fileUploadRepository_custom;


    @Override
    public FileUpload findFileUploadByCaseIdAndFileType(Long caseId, String fileType) {
        return fileUploadRepository_custom.findFileUploadByCaseIdAndFileType(caseId,fileType);
    }
}