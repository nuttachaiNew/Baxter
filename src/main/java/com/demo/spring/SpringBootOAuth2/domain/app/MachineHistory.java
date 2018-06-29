package com.demo.spring.SpringBootOAuth2.domain.app;

import com.demo.spring.SpringBootOAuth2.domain.general.BaseMasterEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
@Entity
@Data
public class MachineHistory extends BaseMasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "machine")
    Machine machine;

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

}