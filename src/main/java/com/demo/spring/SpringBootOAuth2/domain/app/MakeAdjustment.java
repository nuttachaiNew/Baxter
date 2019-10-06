package com.demo.spring.SpringBootOAuth2.domain.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class MakeAdjustment {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    private Long version;

    private String adjustBrightness;
    private String adjustLoundNess;
    private String autoDim;
    private String setClock;
    private String setDail;
    private String drainTimeMin;
    private String drainAlram;
    private String comfortControll;
    private String lastManualDrain;
    private String urTarget;
    private String alram;
    private String nurseNetworkMode;
    
 
}