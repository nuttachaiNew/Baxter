package com.demo.spring.SpringBootOAuth2.service;

import com.demo.spring.SpringBootOAuth2.domain.app.Machine;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface MachineService {
    Machine findById(Long id);
    List<Machine> findAllMachines();
    List<Map<String,Object>> findMachineByCriteria(JSONObject jsonObject);
    void deleteMachine(JSONObject jsonObject,String user);
    Map<String,String> saveMachine(String json,String user);
    Map<String,String> updateMachine(String json,String user);
    // void deleteMachine(JSONObject jsonObject);
    // Map<String,String> saveMachine(String json);
    // Map<String,String> updateMachine(String json);
    List<Map<String,Object>> findMachineByMachineType(String type,String ref);
}

