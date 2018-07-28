package com.demo.spring.SpringBootOAuth2.domain.app;

import com.demo.spring.SpringBootOAuth2.domain.general.BaseMasterEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
@Entity
@Data
public class Customer extends BaseMasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    private Long version;
    private String telNo;
    private String email;
    private String customerType;
    private String hospitalName;
    private String nationId;    
    private String patientName;    
    private String patientLastName;    
    private String currentAddress1;
    private String currentAddress2;
    private String currentSubDistrict;
    private String currentDistrict;
    private String currentProvince;
    private String currentZipCode;
    private String shippingAddress1;
    private String shippingAddress2;
    private String shippingSubDistrict;
    private String shippingDistrict;
    private String shippingProvince;
    private String shippingZipCode;



    // public Long getId() {
    //     return id;
    // }

    // public void setId(Long id) {
    //     this.id = id;
    // }

    // public Long getVersion() {
    //     return version;
    // }

    // public void setVersion(Long version) {
    //     this.version = version;
    // }

    // public String getAddress(String address){
    //     return address;
    // }

    // public void setAddress(String address){
    //     this.address = address;
    // }

    // public String getTelephoneNumber(String telephoneNumber){
    //     return telephoneNumber;
    // }

    // public void setTelephoneNumber(String telephoneNumber){
    //     this.telephoneNumber = telephoneNumber;
    // }

    // public String getEmail(String email){
    //     return email;
    // }

    // public void setEmail(String email){
    //     this.email = email;
    // }

    // public String getIdCard(String idCard){
    //     return idCard;
    // }

    // public void setIdCard(String idCard){
    //     this.idCard = idCard;
    // }


}