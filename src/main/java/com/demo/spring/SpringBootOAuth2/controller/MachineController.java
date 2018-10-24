package com.demo.spring.SpringBootOAuth2.controller;

import com.demo.spring.SpringBootOAuth2.domain.app.Machine;
import com.demo.spring.SpringBootOAuth2.repository.UserRepository;
import com.demo.spring.SpringBootOAuth2.service.MachineService;
import flexjson.JSONSerializer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/machines")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @Autowired
    private UserRepository userRepository;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value ="/insert", method = RequestMethod.POST)
    public ResponseEntity<String> saveMachine(HttpServletRequest request , @RequestBody String json){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            String user = null;
            Map<String,String> result = machineService.saveMachine(json,user);
            headers.add("errorStatus", "N");
            headers.add("errsg", null);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(result), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value ="/update", method = RequestMethod.POST)
    public ResponseEntity<String> updateMachine(HttpServletRequest request , @RequestBody String json){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            String user = null;
            Map<String,String> result = machineService.updateMachine(json,user);
            headers.add("errorStatus", "N");
            headers.add("errsg", null);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(result), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }

	@RequestMapping(value ="/findAllMachine", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> getMachineAll(){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            List<Machine> machineList = machineService.findAllMachines();
            headers.add("errorStatus", "N");
            headers.add("errsg", null);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(machineList), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }

    }

    @RequestMapping(value ="/findById", method = RequestMethod.GET)
    ResponseEntity<String> getMachineById(@RequestParam(value = "id",required = false)String id){


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            Machine machine = machineService.findById(Long.valueOf(id));
            headers.add("errorStatus", "N");
            headers.add("errsg", null);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(machine), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value ="/findMachineByCriteria", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> findMachineByCriteria(@RequestParam(value = "machineType",required = false)String machineType,
                                                 @RequestParam(value = "status",required = false)String status,
                                                 @RequestParam(value = "search",required = false)String search,
                                                 @RequestParam(value = "usedDateFrom",required = false)String usedDateFrom,
                                                 @RequestParam(value = "usedDateTo",required = false)String usedDateTo
                                                 ){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            JSONObject object = new JSONObject();
            object.put("machineType",machineType);
            object.put("status",status);
            object.put("search",search);
            object.put("usedDateFrom",usedDateFrom);
            object.put("usedDateTo",usedDateTo);
            List<Map<String,Object>> machineList = machineService.findMachineByCriteria(object);
            headers.add("errorStatus", "N");
            headers.add("errsg", null);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(machineList), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }

    }

    @RequestMapping(value ="/inActiveMachine", method = RequestMethod.POST)
    ResponseEntity<String> inActiveMachine(HttpServletRequest request , @RequestBody String json){


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            String user = null;
            JSONObject object = new JSONObject(json);
            machineService.inActiveMachine(object,user);
            headers.add("errorStatus", "N");
            headers.add("errsg", null);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize("SUCCESS"), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }


    @RequestMapping(value ="/findMachineByMachineType", method = RequestMethod.GET)
    ResponseEntity<String> findMachineByMachineType(@RequestParam(value = "machineType",required = false)String machineType){


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            List<Map<String,Object>> result = machineService.findMachineByMachineType(machineType,null);
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(result), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value ="/deleteMachine", method = RequestMethod.POST)
    public ResponseEntity<String> deleteMachine(@RequestBody String json){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        try {
            JSONObject object = new JSONObject(json);
            Map<String,String> result = machineService.deleteMachine(object);
            headers.add("errorStatus", "N");
            headers.add("errorMsg", null);
            return new ResponseEntity<String>(new JSONSerializer().deepSerialize(result), headers, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Exception : {}",ex);
            headers.add("errorStatus", "E");
            headers.add("errorMsg", ex.getMessage());
            return new ResponseEntity<String>(null, headers, HttpStatus.OK);
        }
    }

}