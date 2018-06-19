package com.demo.spring.SpringBootOAuth2.domain.general;

import javax.persistence.MappedSuperclass;


@MappedSuperclass
public class BaseMasterEntity extends BaseModel {

    protected String code;

    protected String name;

    protected String detail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
