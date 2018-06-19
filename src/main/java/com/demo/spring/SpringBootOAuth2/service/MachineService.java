package com.demo.spring.SpringBootOAuth2.service;

import com.demo.spring.SpringBootOAuth2.domain.app.Machine;
import org.json.JSONObject;

import java.util.List;

public interface MachineService {
    Machine findById(Long id);
    List<Machine> findAllMachines();
//    List<Machine> findUserByCriteria(JSONObject jsonObject);
    void deleteMachine(JSONObject jsonObject);
}

