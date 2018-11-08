package com.demo.spring.SpringBootOAuth2.domain.app;

import com.demo.spring.SpringBootOAuth2.domain.general.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.util.Set;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "case_management")
@Data
public class CaseManagement extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @Version
    private Long version;

    @OneToOne(fetch = FetchType.LAZY)
    private CaseManagement refCase;
    private String caseStatus;
    private String caseNumber;
    private String caseType;
    private String shareSource;
    private String electronicConsentFlag;
    private String electronicConsent;

    private String assignTs;
    private String assignFn;
    private String assignCs;
    private String assignAsm;
    private Long areaId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Timestamp activityDate;
    private String asmRemark;
    private String flagCheckIdCard;
    private String flagCheckPayslip;
    private String flagCheckContract;
    private String flagCheckPrescription;
    private String flagCheckInstallation;
     
    //return case & chagecase
    private String reporterName;
    private String reporterLastName;
    private String issueCase;
    private String contactPersonName;
    private String contactPersonLastName;
    private String contactPersonTel;
    private String changeCause; 
    private String closeFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Timestamp activeDate;


    @OneToOne(fetch = FetchType.LAZY)
    private User actionUser;

    @OneToOne(fetch = FetchType.LAZY )
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY)
    private Machine machine1;

    @OneToOne(fetch = FetchType.LAZY)
    private Machine machine2;

    @OneToOne(fetch = FetchType.LAZY)
    private Machine machine3;

    @OneToOne(fetch = FetchType.LAZY)
    private Machine machine4;
    
    @OneToOne(fetch = FetchType.LAZY)
    private Machine machine5;

    @OneToOne(fetch = FetchType.LAZY)
    private Machine machine6;

    @OneToOne(fetch = FetchType.LAZY)
    private Machine machine7;

    @OneToOne(fetch = FetchType.LAZY)
    private Machine machine8;
    
    @OneToOne(fetch = FetchType.LAZY)
    private Machine machine9;

    @OneToOne(fetch = FetchType.LAZY)
    private Machine machine10;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Installation installation;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Prescription prescription;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Payslip payslip;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Receipt receipt;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseManagement" , orphanRemoval = true)
    private Set<FileUpload> fileUploads;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseManagement" , orphanRemoval = true)
    private Set<CaseActivity> caseActivitys;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseManagement" , orphanRemoval = true)
    private Set<LineApprove> lineApproves;

}