package com.demo.spring.SpringBootOAuth2.domain.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    private Long version;

    private String patientName;
    private String hospitalName;
    private String serialNumber;
    private Integer setting;

    @OneToOne(fetch = FetchType.LAZY)
    private NurseMenu nurseMenu;

    @OneToOne(fetch = FetchType.LAZY)
    private MakeAdjustment makeAdjustment;
    
    @OneToOne(fetch = FetchType.LAZY)
    private ChangePrograme changePrograme;
    

}