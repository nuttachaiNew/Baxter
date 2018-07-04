package com.demo.spring.SpringBootOAuth2.domain.app;

import com.demo.spring.SpringBootOAuth2.domain.general.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.*;
@Entity
@Table(name = "parameter")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Parameter extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    private Long version;

    @Column(unique = true)
    private String code;

    private String parameterDescription;

     /**
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parameter")
    private Set<ParameterDetail> parameterDetails = new HashSet<ParameterDetail>();


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

    public void setCode(String code){
        this.code = code;
    }
    
    public String getCode(){
        return code;
    }

    public void setParameterDescription(String parameterDescription){
        this.parameterDescription = parameterDescription;
    }
    
    public String getParameterDescription(){
        return parameterDescription;
    }

    public void setParameterDetails(Set<ParameterDetail> parameterDetails){
        this.parameterDetails = parameterDetails;
    }

    public Set<ParameterDetail> getParameterDetails(){
        return parameterDetails;
    }

  
}