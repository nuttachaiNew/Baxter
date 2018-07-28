package com.demo.spring.SpringBootOAuth2.domain.app;

import com.demo.spring.SpringBootOAuth2.domain.general.BaseMasterEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;

import javax.persistence.*;
@Entity
public class MachineHistory  {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "machine")
    Machine machine;

    private String actionBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Timestamp actionDate;

    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // protected Timestamp endDate;

    private String status;

    private String caseNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Machine getMachine(){
        return machine;
    }

    public void setMachine(Machine machine){
        this.machine = machine;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getCaseNumber(){
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber){
        this.caseNumber = caseNumber;
    }
    
    public String getActionBy(){
        return actionBy;
    }

    public void setActionBy(String actionBy){
        this.actionBy = actionBy;
    }

    public Timestamp getActionDate(){
        return actionDate;
    }

    public void setActionDate(Timestamp actionDate){
        this.actionDate = actionDate;
    }

    // public Timestamp getEndDate(){
    //     return endDate;
    // }

    // public void setEndDate(Timestamp endDate){
    //     this.endDate = endDate;
    // }
}