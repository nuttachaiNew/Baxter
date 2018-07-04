package com.demo.spring.SpringBootOAuth2.repository;

import com.demo.spring.SpringBootOAuth2.domain.app.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface BranchRepository extends JpaRepository<Branch,Long> {

	 @Query("select b from Branch b where b.code like CONCAT('%',:code,'%') " +
            "or b.name like CONCAT('%',:name,'%') ")
	 List<Branch>findByCodeLikeOrNameLike(@Param("code")String code,
                                         @Param("name")String name);
}
