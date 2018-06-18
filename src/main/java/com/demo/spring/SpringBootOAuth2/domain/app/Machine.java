package com.demo.spring.SpringBootOAuth2.domain.app;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.demo.spring.SpringBootOAuth2.domain.general.BaseMasterEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
@Entity
@Data
public class Machine extends BaseMasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    private Long version;

    private String machineType;
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp startUsed;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp endUsed;
}