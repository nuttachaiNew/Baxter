package com.demo.spring.SpringBootOAuth2.domain;

import javax.persistence.*;
import lombok.*;
@Entity
@Data
@Table(name = "random_city")
public class RandomCity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    
}