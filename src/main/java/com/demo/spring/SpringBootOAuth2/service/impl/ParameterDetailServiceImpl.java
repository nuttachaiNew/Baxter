package com.demo.spring.SpringBootOAuth2.service.impl;

import com.demo.spring.SpringBootOAuth2.domain.app.ParameterDetail;
import com.demo.spring.SpringBootOAuth2.repository.ParameterDetailRepositoryCustom;
import com.demo.spring.SpringBootOAuth2.service.ParameterDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParameterDetailServiceImpl implements ParameterDetailService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ParameterDetailRepositoryCustom parameterDetailRepository_custom;


    @Override
    public ParameterDetail findParameterDetailByCodeAndAppParameter(String code, Long idAppParameter) {
        return parameterDetailRepository_custom.findParameterDetailByCodeAndAppParameter(code,idAppParameter);
    }
}