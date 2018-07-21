package com.demo.spring.SpringBootOAuth2.controller;

import com.demo.spring.SpringBootOAuth2.domain.app.Menu;
import com.demo.spring.SpringBootOAuth2.service.ParameterService;
import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parameter")
// @RequestMapping("/menus")
public class ParameterController {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ParameterService parameterService;

    @RequestMapping(value = "/findParameterDetailByParameterCode", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> findParameterDetailByParameterCode(@RequestParam(value="code",required = true)String code){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        LOGGER.info("criteria: {}", code);
        try{
            List<Map<String,Object>> result = parameterService.findParamaterDetailByParameterCode(code);
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").prettyPrint(true).deepSerialize(result), headers, HttpStatus.OK);
        }catch (Exception e){
            headers.add("errorStatus","E");
            headers.add("errorMsg",e.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }

}
