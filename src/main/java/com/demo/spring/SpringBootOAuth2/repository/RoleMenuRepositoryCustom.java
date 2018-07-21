package com.demo.spring.SpringBootOAuth2.repository;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.StringBuilder;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RoleMenuRepositoryCustom {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @PersistenceContext
    private EntityManager em;

    public List<Map<String,Object>> findByRoleId(Long id){
      LOGGER.info("findByRoleId :{} ",id);
      List<Object[]> listfromQuery = new ArrayList<Object[]>();
      List<Map<String,Object>> result = new ArrayList<>();
        
        try{
            StringBuilder sqlStatement = new StringBuilder();  
            sqlStatement.append(" SELECT  \n");
            sqlStatement.append(" RM.ID ID ,   \n");
            sqlStatement.append(" AR.ID ROLE_ID,  M.ID MENU_ID,  \n");
            sqlStatement.append(" AR.CODE ROLE_CODE, AR.ROLE_NAME ROLE_NAME, \n");
            sqlStatement.append(" M.CODE MENU_CODE,  M.NAME MENU_NAME, M.URL MENU_URL,M.MENU_GROUP MENU_GROUP \n");
            sqlStatement.append(" FROM ROLE_MENU RM \n");
            sqlStatement.append(" JOIN MENU M ON M.ID = RM.MENU \n");
            sqlStatement.append(" JOIN APP_ROLE AR ON AR.ID = RM.ROLE \n");
            sqlStatement.append(" WHERE AR.ID = :roleId ");
            sqlStatement.append(" ORDER BY AR.CODE, M.CODE \n");
            LOGGER.debug("sqlStatement :{}",sqlStatement);
            Query query = em.createNativeQuery(sqlStatement.toString());
            query.setParameter("roleId",id);
            listfromQuery = query.getResultList();
            for(Object [] o :listfromQuery ){
                    Map<String,Object> mapResult = new HashMap();
                    mapResult.put("id",o[0]);
                    mapResult.put("roleId",o[1]);
                    mapResult.put("menuId",o[2]);
                    mapResult.put("roleCode",o[3]);
                    mapResult.put("roleName",o[4]);
                    mapResult.put("menuCode",o[5]);
                    mapResult.put("menuName",o[6]);
                    mapResult.put("menuUrl",o[7]);
                    mapResult.put("menuGroup",o[8]);
                   
                    result.add(mapResult);
                }

            return result;
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

     public List<Map<String,Object>> findAllRoleMenu(){
      LOGGER.info("findAllRoleMenu ");
      List<Object[]> listfromQuery = new ArrayList<Object[]>();
      List<Map<String,Object>> result = new ArrayList<>();
        
        try{
            StringBuilder sqlStatement = new StringBuilder();  
            sqlStatement.append(" SELECT  \n");
            sqlStatement.append(" RM.ID ID ,   \n");
            sqlStatement.append(" AR.ID ROLE_ID,  M.ID MENU_ID,  \n");
            sqlStatement.append(" AR.CODE ROLE_CODE, AR.ROLE_NAME ROLE_NAME, \n");
            sqlStatement.append(" M.CODE MENU_CODE,  M.NAME MENU_NAME, M.URL MENU_URL,M.MENU_GROUP MENU_GROUP \n");
            sqlStatement.append(" FROM ROLE_MENU RM \n");
            sqlStatement.append(" JOIN MENU M ON M.ID = RM.MENU \n");
            sqlStatement.append(" JOIN APP_ROLE AR ON AR.ID = RM.ROLE \n");
            sqlStatement.append(" ORDER BY AR.CODE, M.CODE \n");
            LOGGER.debug("sqlStatement :{}",sqlStatement);
            Query query = em.createNativeQuery(sqlStatement.toString());
            listfromQuery = query.getResultList();
            for(Object [] o :listfromQuery ){
                    Map<String,Object> mapResult = new HashMap();
                    mapResult.put("id",o[0]);
                    mapResult.put("roleId",o[1]);
                    mapResult.put("menuId",o[2]);
                    mapResult.put("roleCode",o[3]);
                    mapResult.put("roleName",o[4]);
                    mapResult.put("menuCode",o[5]);
                    mapResult.put("menuName",o[6]);
                    mapResult.put("menuUrl",o[7]);
                    mapResult.put("menuGroup",o[8]);
                    result.add(mapResult);
                }
            return result;
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

}