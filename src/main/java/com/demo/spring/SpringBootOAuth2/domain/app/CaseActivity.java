package com.demo.spring.SpringBootOAuth2.domain.app;

import com.demo.spring.SpringBootOAuth2.domain.general.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class CaseActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST})
    private User user;

    private String actionStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp actionDate;

    private String remark;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "CASE_MANAGEMENT")
    CaseManagement caseManagement;

}