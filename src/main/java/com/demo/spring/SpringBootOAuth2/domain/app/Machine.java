package com.demo.spring.SpringBootOAuth2.domain.app;

import com.demo.spring.SpringBootOAuth2.domain.general.BaseMasterEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
//@Data
public class Machine extends BaseMasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    private Long version;

    private String machineType;
    private Integer status;

    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // private Timestamp startUsed;

    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // private Timestamp endUsed;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "machine")
    private Set<MachineHistory> machineHistory;

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

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    // public Timestamp getStartUsed() {
    //     return startUsed;
    // }

    // public void setStartUsed(Timestamp startUsed) {
    //     this.startUsed = startUsed;
    // }

    // public Timestamp getEndUsed() {
    //     return endUsed;
    // }

    // public void setEndUsed(Timestamp endUsed) {
    //     this.endUsed = endUsed;
    // }

    public Set<MachineHistory> getMachineHistory() {
        return machineHistory;
    }

    public void setMachineHistory(Set<MachineHistory> machineHistory) {
        this.machineHistory = machineHistory;
    }

}