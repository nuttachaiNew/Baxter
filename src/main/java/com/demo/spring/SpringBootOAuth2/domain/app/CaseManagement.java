package com.demo.spring.SpringBootOAuth2.domain.app;

import com.demo.spring.SpringBootOAuth2.domain.general.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.util.Set;
import javax.persistence.*;
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

    private String caseNumber;
    private String shareSource;
    private String electronicCon;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseManagement")
    private Set<FileUpload> fileUploads;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseManagement")
    private Set<CaseActivity> caseActivitys;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseManagement")
    private Set<LineApprove> lineApproves;

}