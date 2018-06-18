package com.demo.spring.SpringBootOAuth2.repository;

import com.demo.spring.SpringBootOAuth2.domain.RandomCity;
import org.springframework.data.repository.CrudRepository;

public interface RandomCityRepository extends CrudRepository<RandomCity, Long> {
}