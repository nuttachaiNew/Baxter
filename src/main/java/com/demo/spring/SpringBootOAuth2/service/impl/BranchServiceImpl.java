package com.demo.spring.SpringBootOAuth2.service.impl;

import com.demo.spring.SpringBootOAuth2.domain.app.Branch;
import com.demo.spring.SpringBootOAuth2.repository.BranchRepository;
import com.demo.spring.SpringBootOAuth2.service.BranchService;
import com.google.gson.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.demo.spring.SpringBootOAuth2.util.*;

@Service
public class BranchServiceImpl implements BranchService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    BranchService branchService;

    JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            return json == null ? null : new Date(json.getAsLong());
        }
    };

    @Override
    public Branch findById(Long id) {
        try {
            LOGGER.info("Branch Id");
            LOGGER.info("id : {}", id);
            return branchRepository.findOne(id);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Exception : {}", e);
            throw new RuntimeException(e);

        }
    }

    @Override
    public List<Branch> findAllBranch() {
        try {
            LOGGER.info("Branch findAll");
            return branchRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Exception : {}", e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public Map<String, String> saveBranch(String json) {
        Map<String, String> result = new HashMap<>();
        try {
            LOGGER.info("Save Branch");
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);
            Branch branch = mapper.readValue(jsonObject.toString(), Branch.class);
            branch.setCreatedDate(StandardUtil.getCurrentDate());
            branchRepository.saveAndFlush(branch);
            result.put("code", branch.getCode());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Exception : {}", e);
            throw new RuntimeException(e);
        }
    }

    protected Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapter(Date.class, deser).create();

    @Override
    public Map<String, String> updateBranch(String json) {
        Map<String, String> result = new HashMap<>();
        try {
            LOGGER.info("Update Branch");
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);
            Branch newBranch = mapper.readValue(jsonObject.toString(), Branch.class);
            Branch oldBranch = branchRepository.findOne(newBranch.getId());
            newBranch = mapper.readerForUpdating(oldBranch).readValue(gson.toJson(newBranch));
            newBranch.setUpdatdDate(StandardUtil.getCurrentDate());
            branchRepository.saveAndFlush(newBranch);
            result.put("code", newBranch.getCode());
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Exception : {} ", e);
            throw new RuntimeException(e);
        }
    }


    @Override
    @Transactional
    public Map<String, String> deleteBranch(String json){
        Map<String, String> result = new HashMap<>();
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("id");
            if (jsonArray != null && jsonArray.length() > 0){
                for(int i = 0 ; i < jsonArray.length() ; i++ ){
                    Long id = Long.valueOf(jsonArray.get(i).toString());
                    Branch branch = branchRepository.findOne(id);
                    if(branch != null){
                        branchRepository.delete(branch);
                    }
                }
            }
            result.put("msg","delete "+String.valueOf(jsonArray.length())+" success");
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {} ",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Branch> findByCodeLikeOrNameLike(String criteria){
        LOGGER.info("Find Code Or Name :{}",criteria);
        try{
            return branchRepository.findByCodeLikeOrNameLike(criteria , criteria);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {} ",e);
            throw new RuntimeException(e);
        }
    }



}

