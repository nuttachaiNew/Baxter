package com.demo.spring.SpringBootOAuth2.controller;


import com.demo.spring.SpringBootOAuth2.domain.app.Branch;
import com.demo.spring.SpringBootOAuth2.service.BranchService;
import flexjson.JSONSerializer;
import org.apache.catalina.mapper.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/api/branchs")
// @RequestMapping("/branchs")
public class BranchController {
    @Autowired
    BranchService branchService;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> getBranchById (@RequestParam(value="id",required = false)String id){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json;charset=utf-8");
        try{
            Branch branch = branchService.findById(Long.valueOf(id));
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").prettyPrint(true).deepSerialize(branch), headers, HttpStatus.OK);
        }catch (Exception e){
            headers.add("errorStatus","E");
            headers.add("errorMsg",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/findAllBranch", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> getBranchAll(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json;charset=utf-8");
        try{
            List<Branch> branchList = branchService.findAllBranch();
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").prettyPrint(true).deepSerialize(branchList), headers, HttpStatus.OK);
        }catch (Exception e){
            headers.add("errorStatus","E");
            headers.add("errorMsg",e.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<String> saveBranch(@RequestBody String json){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try{
            Map<String,String> result = branchService.saveBranch(json);
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").prettyPrint(true).deepSerialize(result), headers, HttpStatus.OK);
        }catch (Exception e){
            headers.add("errorStatus","E");
            headers.add("errorMsg",e.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<String> updateBranch(@RequestBody String json){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try{
            Map<String,String> result = branchService.updateBranch(json);
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").prettyPrint(true).deepSerialize(result), headers, HttpStatus.OK);
        }catch (Exception e){
            headers.add("errorStatus","E");
            headers.add("errorMsg",e.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<String> deleteBranch(@RequestBody String json){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try{
            Map<String,String> result = branchService.deleteBranch(json);
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").prettyPrint(true).deepSerialize(result), headers, HttpStatus.OK);
        }catch (Exception e){
            headers.add("errorStatus","E");
            headers.add("errorMsg",e.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/findByCodeLikeOrNameLike", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> findByCodeLikeOrNameLike(@RequestParam(value="criteria",required = false)String criteria){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        LOGGER.info("criteria: {}", criteria);
        try{
            List<Branch> result = branchService.findByCodeLikeOrNameLike(criteria);
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
