package com.demo.spring.SpringBootOAuth2.domain.general;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

@MappedSuperclass
public class BaseModel implements Serializable {

    protected String dataStatus;

    protected String createProg;

    protected String createdBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Timestamp createdDate;

    protected String lastProg;

    protected String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Timestamp updatdDate;

    protected Integer displayOrder;

    protected Integer isActive;

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getCreateProg() {
        return createProg;
    }

    public void setCreateProg(String createProg) {
        this.createProg = createProg;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastProg() {
        return lastProg;
    }

    public void setLastProg(String lastProg) {
        this.lastProg = lastProg;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatdDate() {
        return updatdDate;
    }

    public void setUpdatdDate(Timestamp updatdDate) {
        this.updatdDate = updatdDate;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
}
