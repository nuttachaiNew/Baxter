package com.demo.spring.SpringBootOAuth2.repository;

import com.demo.spring.SpringBootOAuth2.domain.app.CaseActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseActivityRepository extends JpaRepository<CaseActivity,Long> {

}