package com.demo.spring.SpringBootOAuth2.service;

import com.demo.spring.SpringBootOAuth2.domain.app.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {
    Map<String,String> saveRole(String json);
    Map<String,String> updateRole(String json);
    Map<String,String> deleteRole(String json);
    List<Role> findAllRole();
    Role findRoleById(Long id);
    List<Role> findRoleByName(String name);
    List<Role> findRoleByCode(String code);
}
