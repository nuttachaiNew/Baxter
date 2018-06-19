package com.demo.spring.SpringBootOAuth2.service.impl;

import com.demo.spring.SpringBootOAuth2.domain.app.Machine;
import com.demo.spring.SpringBootOAuth2.repository.MachineRepository;
import com.demo.spring.SpringBootOAuth2.service.MachineService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineServiceImpl implements MachineService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MachineRepository machineRepository;

    @Override
    public Machine findById(Long id) {

        try{
            LOGGER.info("Machine findById");
            LOGGER.info("id : {}",id);
            return machineRepository.findOne(id);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Machine> findAllMachines() {

        try{
            LOGGER.info("Machine findAllMachines");
            return machineRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public List<Machine> findUserByCriteria(JSONObject jsonObject) {
//        machineRepository.();
//    }

    @Override
    public void deleteMachine(JSONObject jsonObject) {

        try{
            LOGGER.info("Machine deleteMachine");

            JSONArray jsonArray = jsonObject.getJSONArray("delete");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    Long id = Long.valueOf(jsonArray.get(i).toString());
                    Machine machine = machineRepository.findOne(id);
                    if(machine != null){
                        machine.setStatus(0);
                        machineRepository.saveAndFlush(machine);
                    }
                }
            }


        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }

    }

}