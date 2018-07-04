package com.demo.spring.SpringBootOAuth2.service;


import com.demo.spring.SpringBootOAuth2.domain.app.Branch;

import java.util.List;
import java.util.Map;

public interface BranchService {
    Branch findById(Long id);
    List<Branch> findAllBranch();
    List<Branch> findByCodeLikeOrNameLike(String  criteria);
    Map<String,String> saveBranch(String json);
    Map<String,String> updateBranch(String json);
    Map<String,String> deleteBranch(String json);


}

