package com.demo.spring.SpringBootOAuth2.domain.app;

import com.demo.spring.SpringBootOAuth2.domain.general.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.sql.Timestamp;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Data
public class FileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    private Long version;

    private String filePath;
    private String fileType;

    protected String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Timestamp updatdDate;


    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "caseManagements")
    CaseManagement caseManagement;

}