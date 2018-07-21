package com.demo.spring.SpringBootOAuth2.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface CaseManagementService {
    String uploadfileByCaseIdAndFileType(String name, MultipartFile multipartFile,Long caseId,String fileTpye,String user);
    InputStream downloadFileByCaseIdAndFileType(Long caseId,String fileTpye);
}

