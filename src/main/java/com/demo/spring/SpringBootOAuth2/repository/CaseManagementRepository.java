package com.demo.spring.SpringBootOAuth2.repository;

import com.demo.spring.SpringBootOAuth2.domain.app.CaseManagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseManagementRepository extends JpaRepository<CaseManagement,Long> {
	CaseManagement findByCaseNumber(String caseNumber);
}