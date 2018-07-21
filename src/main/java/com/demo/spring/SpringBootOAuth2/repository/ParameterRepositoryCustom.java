package com.demo.spring.SpringBootOAuth2.repository;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class ParameterRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public List<Map<String,Object>> findParamaterDetailByParameterCode(String code){

        try{

            
            List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
            List<Object[]> listfromQuery = new ArrayList<Object[]>();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            StringBuilder criteriaSqlData = new StringBuilder();
            // criteriaSqlData.append("SELECT M.id ,M.version , M.code , M.name , M.machine_type , M.detail , M.status , M.start_used , M.end_used ");
            // criteriaSqlData.append("FROM Machine M where UPPER(M.machine_type) like UPPER(:machineType) ");
            
            criteriaSqlData.append(" SELECT PD.ID, PD.CODE , PD.PARAMETER_VALUE , PD.PARAMETER_TYPE  , P.PARAMETER_DESCRIPTION  ");
            criteriaSqlData.append(" FROM PARAMETER_DETAIL PD "); 
            criteriaSqlData.append(" JOIN PARAMETER P ON P.ID = PD.PARAMETER ");
            criteriaSqlData.append(" WHERE P.CODE = :code ");
            criteriaSqlData.append(" ORDER BY PD.CODE ");




            Query query = em.createNativeQuery(criteriaSqlData.toString());
            query.setParameter("code",code);
            listfromQuery = query.getResultList();

            if(listfromQuery != null){
                for(Object [] o :listfromQuery ){
                    Map<String,Object> mapResult = new HashMap();
                    mapResult.put("id",o[0]);
                    mapResult.put("code",o[1]);
                    mapResult.put("parameterValue",o[2]);
                    mapResult.put("parameterType",o[3]);
                    mapResult.put("parameterDescription",o[4]);
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