package com.demo.spring.SpringBootOAuth2.domain.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class ChangePrograme {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    private Long version;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Opd opd;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Tidal tidal;

    private String tidalVol;
    private String tidalTotalUf;
    private String fullDrainEvery;
    private String numberOfDayExchange;
    private String dayFillVol;
}