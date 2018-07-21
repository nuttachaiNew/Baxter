package com.demo.spring.SpringBootOAuth2.service.impl;

import com.demo.spring.SpringBootOAuth2.repository.ParameterRepositoryCustom;
import com.demo.spring.SpringBootOAuth2.service.ParameterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParameterServiceImpl implements ParameterService{

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ParameterRepositoryCustom parameterRepositoryCustom;

    JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            return json == null ? null : new Date(json.getAsLong());
        }
    };

    protected Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapter(Date.class, deser).create();

   @Override
    public    List<Map<String,Object>> findParamaterDetailByParameterCode(String code){
        try{
            return parameterRepositoryCustom.findParamaterDetailByParameterCode(code);
        }catch(Exception e){
            e.printStackTrace();
             throw new RuntimeException(e.getMessage());
        }
    }


}
