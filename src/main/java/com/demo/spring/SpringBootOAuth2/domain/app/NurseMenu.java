package com.demo.spring.SpringBootOAuth2.domain.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class NurseMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    private Long version;

    private String modes;
    private String minDrawVol;
    private String minDrawTime;
    private String negOfTime;
    private String postOfLimit;
    private String smartDwells;
    private String heaterBagEmpty;
    private String tidalFullDrns;
    private String language;
    private String flush;
    private String programLocked;
    private String weightReset;

    private String smartDwellsUp;
    private String smartDwellsDown;
    private String nurseNetworkMode;


}