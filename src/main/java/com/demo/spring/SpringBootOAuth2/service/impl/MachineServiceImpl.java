package com.demo.spring.SpringBootOAuth2.service.impl;

import com.demo.spring.SpringBootOAuth2.domain.app.Machine;
import com.demo.spring.SpringBootOAuth2.repository.MachineRepository;
import com.demo.spring.SpringBootOAuth2.service.MachineService;
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
        return machineRepository.findAll();
    }

}