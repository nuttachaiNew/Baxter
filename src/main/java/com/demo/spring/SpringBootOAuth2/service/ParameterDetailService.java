package com.demo.spring.SpringBootOAuth2.service;

import com.demo.spring.SpringBootOAuth2.domain.app.ParameterDetail;

public interface ParameterDetailService {
    ParameterDetail findParameterDetailByCodeAndAppParameter(String code, Long idAppParameter);
}

