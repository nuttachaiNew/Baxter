package com.demo.spring.SpringBootOAuth2.service;

import com.demo.spring.SpringBootOAuth2.domain.app.Menu;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface MenuService {
    Menu findById(Long id);
    List<Menu> findAll();
}

