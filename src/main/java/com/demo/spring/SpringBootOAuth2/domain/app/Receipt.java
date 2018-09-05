package com.demo.spring.SpringBootOAuth2.domain.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
public class Receipt {

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

    private String receiptNo;
    private String receiptName;
    private String address1;
    private String address2;
    private String subDistrict;
    private String district;
    private String province;
    private String postCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Timestamp receiptDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "receipt" , orphanRemoval = true)
    private Set<ReceiptDtl> receiptDtls;

}