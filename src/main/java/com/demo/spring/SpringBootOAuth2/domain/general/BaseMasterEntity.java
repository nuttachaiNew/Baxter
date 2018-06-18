package com.demo.spring.SpringBootOAuth2.domain.general;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@MappedSuperclass
public class BaseMasterEntity extends BaseModel {

    protected String code;

    protected String name;

    protected String detail;

}
