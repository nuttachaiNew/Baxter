package com.demo.spring.SpringBootOAuth2.domain.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class Opd {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    private Long version;

    private String therapy;
    private String totalVol;
    private String therapyTime;
    private String fillVol;
    private String lastFillVol;
    private String dextRose;
    private String weightUnit;
    private String patientWeight;
    private String tidalVol;
    private String tidalTotalUf;
    private String fullEveryDrain;
    private String numberOfDayExchange;
    private String dayFillVol;

}