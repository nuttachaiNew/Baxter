package com.demo.spring.SpringBootOAuth2.domain;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.demo.spring.SpringBootOAuth2.domain.general.BaseMasterEntity;

import lombok.*;
@Entity
@Data
public class Branch extends BaseMasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    private Long version;
}