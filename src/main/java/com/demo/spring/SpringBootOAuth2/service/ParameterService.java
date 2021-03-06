package com.demo.spring.SpringBootOAuth2.service;

import com.demo.spring.SpringBootOAuth2.domain.app.Menu;
import org.json.JSONObject;
import com.demo.spring.SpringBootOAuth2.domain.app.Parameter;

import java.util.List;
import java.util.Map;

public interface ParameterService {

	List<Map<String,Object>> findParamaterDetailByParameterCode(String code);
 	Parameter findAppParameterByAppParameterCode(String code);
}

