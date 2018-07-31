package com.demo.spring.SpringBootOAuth2.repository;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Repository
public class CaseManagementRepositoryCustom {

    @PersistenceContext
    private EntityManager em;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


        public List<String> getLastedCaseNumberByCriteria(String type,String createdDate){
            try{
                // List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
                List<String> listfromQuery = new ArrayList<>();
                StringBuilder criteriaSqlData = new StringBuilder();
                criteriaSqlData.append(" SELECT MAX(CM.CASE_NUMBER)  FROM CASE_MANAGEMENT CM  ");
                criteriaSqlData.append(" WHERE CM.CASE_TYPE = :type ");
                criteriaSqlData.append(" AND TRUNC(CM.CREATED_DATE) = TO_DATE(:createdDate,'MM-YYYY') ");
                Query query = em.createNativeQuery(criteriaSqlData.toString());
                query.setParameter("type",type);
                query.setParameter("createdDate",createdDate);

                 listfromQuery = query.getResultList();
                 LOGGER.debug("get max :{}",listfromQuery);
                 return listfromQuery;
            }catch(Exception e){
                 e.printStackTrace();   
                 throw new RuntimeException(e.getMessage());   
            }
        }

      public Long autoGenerateMachineByTypeAndStatusEqActive(String machineType,String modelRef,String serialNumber){
        try{
             LOGGER.debug("autoGenerateMachineByTypeAndStatusEqActive result type  ");
             Long result = null;
             List<Long> resultList = new ArrayList<>();
             List<Object[]> listfromQuery = new ArrayList<>();
             StringBuilder criteriaSqlData = new StringBuilder();
             criteriaSqlData.append(" SELECT M.ID , M.CODE FROM MACHINE M  ");
             criteriaSqlData.append(" WHERE M.MACHINE_TYPE = :machineType  and M.STATUS = 1  and M.MODEL_REF = :modelRef");
             if( !"".equalsIgnoreCase(serialNumber) ) criteriaSqlData.append(" AND M.SERIAL_NUMBER  = :serialNumber ");
             criteriaSqlData.append(" ORDER BY M.CODE ");
             Query query = em.createNativeQuery(criteriaSqlData.toString());
             query.setParameter("machineType",machineType);
             query.setParameter("modelRef",modelRef);
             if( !"".equalsIgnoreCase(serialNumber) ) query.setParameter("serialNumber",serialNumber);
             listfromQuery = query.getResultList();
             for(Object[] col : listfromQuery){
                Long id = col[0] == null? null : Long.valueOf( col[0].toString() );
                resultList.add(id);
             }
             if(resultList.size() == 0){
                throw new RuntimeException("Machine not Available active size = 0");
             }else{
                result = resultList.get(0);
             }
             LOGGER.debug("autoGenerateMachineByTypeAndStatusEqActive result type: {} ref :{} ID :{} ",machineType,modelRef ,result);

             return result;
        }catch(Exception e){
             e.printStackTrace();   
             throw new RuntimeException(e.getMessage()); 
        }
      }


}