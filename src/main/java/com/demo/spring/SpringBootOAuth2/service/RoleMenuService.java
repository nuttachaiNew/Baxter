package com.demo.spring.SpringBootOAuth2.service;

import com.demo.spring.SpringBootOAuth2.domain.app.RoleMenu;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface RoleMenuService {
	
	void manageRoleMenu(String json);
	List<Map<String,Object>> findByRoleId(Long roleId);
	List<Map<String,Object>> findAll();	

}

