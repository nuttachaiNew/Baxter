package com.demo.spring.SpringBootOAuth2.repository;

import com.demo.spring.SpringBootOAuth2.domain.app.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch,Long> {
}
