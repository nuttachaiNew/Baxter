package com.demo.spring.SpringBootOAuth2.service;

import com.demo.spring.SpringBootOAuth2.domain.app.RandomCity;
import com.demo.spring.SpringBootOAuth2.domain.app.User;

import java.util.List;

public interface GenericService {
    User findByUsername(String username);

    List<User> findAllUsers();

    List<RandomCity> findAllRandomCities();
}

