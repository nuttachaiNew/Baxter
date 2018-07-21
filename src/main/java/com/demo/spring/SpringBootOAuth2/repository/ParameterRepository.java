package com.demo.spring.SpringBootOAuth2.repository;

import com.demo.spring.SpringBootOAuth2.domain.app.Parameter;
import org.springframework.data.repository.CrudRepository;

public interface ParameterRepository extends CrudRepository<Parameter, Long> {
	
}