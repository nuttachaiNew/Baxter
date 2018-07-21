package com.demo.spring.SpringBootOAuth2.service;

import com.demo.spring.SpringBootOAuth2.domain.app.FileUpload;

public interface FileUploadService {
    FileUpload findFileUploadByCaseIdAndFileType(Long caseId, String fileType);
}

