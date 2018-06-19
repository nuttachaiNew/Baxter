package com.demo.spring.SpringBootOAuth2.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryCustom{

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public List<Map<String,Object>> findUserByUserName(String userName){

        try{
            List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
            List<Object[]> listfromQuery = new ArrayList<Object[]>();
            String sql = "";
            if(userName != null){
                sql = " SELECT R.ROLE_NAME ,U.password , U.username FROM APP_USER U JOIN app_role R on u.role_id = R.id WHERE U.USERNAME = '"+userName+"' ";
            }

            StringBuilder sqlStatement = new StringBuilder();
            sqlStatement.append(sql);
            Query query = em.createNativeQuery(sqlStatement.toString());
            listfromQuery = query.getResultList();


            if(listfromQuery != null){
                for(Object [] o :listfromQuery ){
                    Map<String,Object> mapResult = new HashMap();
                    mapResult.put("ROLE_NAME",o[0]);
                    mapResult.put("PASSWORD",o[1]);
                    mapResult.put("USERNAME",o[2]);
                    result.add(mapResult);
                }
            }

            return result;

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}