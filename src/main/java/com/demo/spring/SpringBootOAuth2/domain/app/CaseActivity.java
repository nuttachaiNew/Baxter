package com.demo.spring.SpringBootOAuth2.domain.app;

import com.demo.spring.SpringBootOAuth2.domain.general.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
@Entity
@Data
public class CaseActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    private String actionStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp actionDate;

    private String remark;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "caseManagements")
    CaseManagement caseManagement;

}