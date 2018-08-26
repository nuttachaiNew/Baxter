package com.demo.spring.SpringBootOAuth2.controller;

import com.demo.spring.SpringBootOAuth2.domain.app.Menu;
import com.demo.spring.SpringBootOAuth2.service.MenuService;
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
@CrossOrigin
@RequestMapping("/api/roles")
// @RequestMapping("/menus")
public class MenuController {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MenuService menuService;



    @RequestMapping(value ="/findAllMenus", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> findAllMenus(){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            List<Menu> menuList = menuService.findAll();
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(menuList), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }

    }

    @RequestMapping(value = "/findMenuById", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> findMenuById(@RequestParam(value="id",required = false)Long id){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        LOGGER.info("criteria: {}", id);
        try{
            Menu result = menuService.findById(id);
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
