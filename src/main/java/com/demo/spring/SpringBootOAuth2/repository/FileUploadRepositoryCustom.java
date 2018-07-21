package com.demo.spring.SpringBootOAuth2.repository;

import com.demo.spring.SpringBootOAuth2.domain.app.FileUpload;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class FileUploadRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public FileUpload findFileUploadByCaseIdAndFileType(Long caseId,String fileType) {

        Criteria criteria = ((Session) em.getDelegate()).createCriteria(FileUpload.class,"FileUpload");
        criteria.createAlias("FileUpload.caseManagements","Case");
        criteria.add(Restrictions.eq("fileType", fileType).ignoreCase());
        criteria.add(Restrictions.eq("Case.id", caseId));
        return (FileUpload)criteria.uniqueResult();
    }
}