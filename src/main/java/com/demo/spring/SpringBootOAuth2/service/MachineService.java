package com.demo.spring.SpringBootOAuth2.service;

import com.demo.spring.SpringBootOAuth2.domain.app.Machine;

import java.util.List;

public interface MachineService {
    Machine findById(Long id);
    List<Machine> findAllMachines();
}

