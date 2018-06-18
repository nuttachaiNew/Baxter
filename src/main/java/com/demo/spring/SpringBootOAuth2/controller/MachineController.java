package com.demo.spring.SpringBootOAuth2.controller;

import com.demo.spring.SpringBootOAuth2.domain.app.Machine;
import com.demo.spring.SpringBootOAuth2.service.MachineService;
import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machine")
public class MachineController {

    @Autowired
    private MachineService machineService;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value ="/findAllMachine", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> getMachineAll(){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            List<Machine> machineList = machineService.findAllMachines();
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(machineList), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }

    }

    @RequestMapping(value ="/findById", method = RequestMethod.GET)
    ResponseEntity<String> getMachineById(@RequestParam(value = "id",required = false)String id){


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            Machine machine = machineService.findById(Long.valueOf(id));
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(machine), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }

}