package com.demo.spring.SpringBootOAuth2.repository;

import com.demo.spring.SpringBootOAuth2.domain.app.MachineHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineHistoryRepository extends JpaRepository<MachineHistory,Long> {

}