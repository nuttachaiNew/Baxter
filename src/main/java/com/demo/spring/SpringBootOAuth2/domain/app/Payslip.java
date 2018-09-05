package com.demo.spring.SpringBootOAuth2.domain.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;

@Entity
@Data
public class Payslip {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    private Long version;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Timestamp createdDate;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Timestamp updatedDate;

    private String updatedBy;

     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp payDate;

    private String paymentType;

    private String bank;

    private BigDecimal amount;


}