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
    private Long version;

    @OneToOne(fetch = FetchType.LAZY)
    private CaseManagement refCase;
    private String caseStatus;
    private String caseNumber;
    private String caseType;
    private String shareSource;
    private String electronicConsetFlag;
    private String electronicConset;

    private String assignTs;
    private String assignFn;
    private String assignCs;
    private String assignAsm;
    private Long areaId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Timestamp activityDate;


    @OneToOne(fetch = FetchType.LAZY)
    private User actionUser;

    @OneToOne(fetch = FetchType.LAZY)
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

    @OneToOne(fetch = FetchType.LAZY)
    private Installation installation;

    @OneToOne(fetch = FetchType.LAZY)
    private Prescription prescription;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseManagement")
    private Set<FileUpload> fileUploads;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseManagement")
    private Set<CaseActivity> caseActivitys;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseManagement")
    private Set<LineApprove> lineApproves;

}