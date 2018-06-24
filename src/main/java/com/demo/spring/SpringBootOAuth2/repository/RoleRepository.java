package com.demo.spring.SpringBootOAuth2.repository;

import com.demo.spring.SpringBootOAuth2.domain.app.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("select r from Role r where r.name like CONCAT('%',:name,'%')")
    List<Role> findByNameLike(@Param("name") String name);

    @Query("select r from Role r where r.code like CONCAT('%',:code,'%')")
    List<Role> findByCodeLike(@Param("code")String code);
}