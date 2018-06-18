package com.demo.spring.SpringBootOAuth2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="app_role")
public class Role {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
   
    @JsonIgnore
    private Long version;

    @Column(name="role_name")
    private String name;

    @Column(name="description")
    private String detail;
}