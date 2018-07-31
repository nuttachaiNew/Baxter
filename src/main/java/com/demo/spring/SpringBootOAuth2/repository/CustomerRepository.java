package com.demo.spring.SpringBootOAuth2.repository;

import com.demo.spring.SpringBootOAuth2.domain.app.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

}