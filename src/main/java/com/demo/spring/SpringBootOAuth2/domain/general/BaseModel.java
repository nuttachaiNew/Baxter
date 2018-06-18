package com.demo.spring.SpringBootOAuth2.domain.general;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
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

}
