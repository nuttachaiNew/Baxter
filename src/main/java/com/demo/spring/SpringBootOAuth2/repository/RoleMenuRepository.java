package com.demo.spring.SpringBootOAuth2.repository;

import com.demo.spring.SpringBootOAuth2.domain.app.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleMenuRepository extends JpaRepository<RoleMenu,Long> {
}