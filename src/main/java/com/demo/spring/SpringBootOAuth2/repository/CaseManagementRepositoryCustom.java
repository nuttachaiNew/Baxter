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
                String result = "";
                StringBuilder criteriaSqlData = new StringBuilder();
                criteriaSqlData.append(" SELECT MAX(CM.CASE_NUMBER)  FROM CASE_MANAGEMENT CM  ");
                criteriaSqlData.append(" WHERE CM.CASE_TYPE = :type ");
                criteriaSqlData.append(" AND TO_CHAR(CM.CREATED_DATE,'MM-YYYY') = :createdDate ");
                Query query = em.createNativeQuery(criteriaSqlData.toString());
                query.setParameter("type",type);
                query.setParameter("createdDate",createdDate);
                 listfromQuery = query.getResultList();

                 LOGGER.debug("get max :{} :{}",listfromQuery,listfromQuery.size());
                 return listfromQuery;
            }catch(Exception e){
                 e.printStackTrace();   
                 throw new RuntimeException(e.getMessage());   
            }
        }

      public Long autoGenerateMachineByTypeAndStatusEqActive(String machineType,String modelRef,String serialNumber){
        try{
             LOGGER.debug("autoGenerateMachineByTypeAndStatusEqActive result type :{} :{} :{}  ",machineType,modelRef,serialNumber);
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
                throw new RuntimeException(" Machine type "+ machineType +"  Model Ref "+modelRef+" not Available active ");
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

    public List<Map<String,Object>> findHistoryDocByAreaAndDocStatusAndRoleAndCase(String createdBy,Long areaId,String documentStatus,String roleBy){
        try{
            LOGGER.debug("findHistoryDocByAreaAndDocStatusAndRoleAndCase :{}:{}:{}",createdBy,areaId,documentStatus);
            List<Object[] > listfromQuery = new ArrayList<>();
            StringBuilder criteriaSqlData = new StringBuilder();
            List<Map<String,Object>> results = new ArrayList<>();
            criteriaSqlData.append(" SELECT  CM.CASE_NUMBER , CA.ACTION_STATUS , CA.ACTION_DATE , AU.USERNAME  , CM.CASE_TYPE ,CM.ID ,AR.ROLE_NAME");
            criteriaSqlData.append(" FROM CASE_ACTIVITY CA ");
            criteriaSqlData.append(" JOIN CASE_MANAGEMENT CM ON CM.ID = CA.CASE_MANAGEMENT ");
            criteriaSqlData.append(" JOIN APP_USER AU ON AU.ID = CA.USER_ID ");
            criteriaSqlData.append(" JOIN APP_ROLE AR ON AR.ID = AU.ROLE ");
            criteriaSqlData.append(" WHERE CM.CASE_STATUS IN (:documentStatus) ");
            if(areaId != null){
               criteriaSqlData.append(" AND CM.AREA_ID = :areaId ");   
            }
            if(createdBy !=null ){
                criteriaSqlData.append(" AND CM.CREATED_BY = :createdBy ");
            }
            criteriaSqlData.append(" WHERE CA.ACTION_DATE DESC,CM.CASE_NUMBER ASC ");
            Query query = em.createNativeQuery(criteriaSqlData.toString());
            query.setParameter("documentStatus",Arrays.asList(documentStatus.split(",")) );
            if(areaId!=null) query.setParameter("areaId",areaId);
            if(createdBy!=null) query.setParameter("createdBy",createdBy);
             listfromQuery = query.getResultList();
             for(Object[] col : listfromQuery){
                Map<String,Object> activity = new HashMap<>();
                activity.put("caseNumber",col[0]);
                activity.put("actionStatus",col[1]);
                activity.put("actionDate",col[2]);
                activity.put("actionBy",col[3]);
                activity.put("caseType",col[4]);
                activity.put("caseId",col[5]);
                activity.put("roleName",col[6]);
                results.add(activity);
             }
             return results;
        }catch(Exception e){
            e.printStackTrace();   
             throw new RuntimeException(e.getMessage()); 
        }
    }

    public List<Map<String,Object>> findCaseByCriteria(String date, String caseNumber , String areaId , String documentStatus ,Integer firstResult ,Integer maxResult){
        try{
            caseNumber = caseNumber == null?"":caseNumber;
            LOGGER.debug("findCaseByCriteria :{} :{} :{} :{} :{}",date,caseNumber,documentStatus,firstResult,maxResult);
            List<Object[] > listfromQuery = new ArrayList<>();
            StringBuilder criteriaSqlData = new StringBuilder();
            List<Map<String,Object>> results = new ArrayList<>();
            criteriaSqlData.append(" SELECT CM.ID , CM.CASE_NUMBER , CM.CREATED_DATE , CM.CASE_TYPE , NVL(CUST.PATIENT_NAME,CUST.HOSPITAL_NAME) CUST_NAME , CUST.CUSTOMER_TYPE ,CM.CASE_STATUS ");
            criteriaSqlData.append(" FROM CASE_MANAGEMENT CM   ");
            criteriaSqlData.append(" JOIN CUSTOMER CUST ON CUST.ID  = CM.CUSTOMER_ID   ");
            criteriaSqlData.append(" WHERE 1 =1 ");
            if(date!=null) criteriaSqlData.append(" AND TO_CHAR(CM.CREATED_DATE,'MM-YYYY') = :date ");
            criteriaSqlData.append(" AND CM.CASE_NUMBER  LIKE :caseNumber  ");
            if(areaId!=null)   criteriaSqlData.append(" AND CM.AREA_ID  = :areaId  ");
            if(documentStatus!=null )  criteriaSqlData.append(" AND CM.CASE_STATUS  = :documentStatus  ");
            criteriaSqlData.append(" ORDER BY  CM.CREATED_DATE ,  CM.CASE_NUMBER   ");
            Query query = em.createNativeQuery(criteriaSqlData.toString());
            if(date!=null)query.setParameter("date",date );
            query.setParameter("caseNumber","%"+caseNumber+"%" );
            if(documentStatus!=null ) query.setParameter("documentStatus",Arrays.asList(documentStatus.split(",")) );
            if(areaId!=null) query.setParameter("areaId",areaId );
            listfromQuery = query.getResultList();
             for(Object[] col : listfromQuery){
                Map<String,Object> activity = new HashMap<>();
                activity.put("id",col[0]);
                activity.put("caseNumber",col[1]);
                activity.put("createdDate",col[2]);
                activity.put("caseType",col[3]);
                activity.put("cutsomerName",col[4]);
                activity.put("cutsomerType",col[5]);
                activity.put("caseStatus",col[6]);
                results.add(activity);
             }
             return results;
        }catch(Exception e){
            e.printStackTrace();   
             throw new RuntimeException(e.getMessage()); 
        }
    }

    public List<Map<String,Object>> findCaseforReturnCaseByCustomer(String caseType,String customer,String caseNumber){
        try{
            LOGGER.debug("findCaseforReturnCaseByCustomer : {} :{} :{}",caseType,customer,caseNumber);
            List<Object[] > listfromQuery = new ArrayList<>();
            StringBuilder criteriaSqlData = new StringBuilder();
            List<Map<String,Object>> results = new ArrayList<>();
            criteriaSqlData.append(" SELECT CM.ID , CM.CASE_NUMBER , CM.CREATED_DATE , CM.CASE_TYPE , NVL(CUST.PATIENT_NAME,CUST.HOSPITAL_NAME) CUST_NAME , CUST.CUSTOMER_TYPE ,CM.CASE_STATUS ");
            criteriaSqlData.append(" FROM CASE_MANAGEMENT CM   ");
            criteriaSqlData.append(" JOIN CUSTOMER CUST ON CUST.ID  = CM.CUSTOMER_ID  ");
            criteriaSqlData.append(" WHERE CM.CASE_STATUS = 'F'  AND CM.CLOSE_FLAG IS NULL  ");
            criteriaSqlData.append(" AND (CUST.patient_Name = :customer) OR  (CUST.Hospital_Name = :customer) ");
            criteriaSqlData.append(" AND CM.CASE_TYPE = :caseType  ");
            criteriaSqlData.append(" AND CM.CASE_NUMBER = :caseNumber  ");
            criteriaSqlData.append(" ORDER BY CM.CASE_NUMBER ");

            Query query = em.createNativeQuery(criteriaSqlData.toString());
            query.setParameter("caseType",caseType );
            query.setParameter("customer",customer );
            query.setParameter("caseNumber",caseNumber );
            listfromQuery = query.getResultList();
            for(Object[] col : listfromQuery){
                Map<String,Object> activity = new HashMap<>();
                activity.put("id",col[0]);
                activity.put("caseNumber",col[1]);
                activity.put("createdDate",col[2]);
                activity.put("caseType",col[3]);
                activity.put("cutsomerName",col[4]);
                activity.put("cutsomerType",col[5]);
                activity.put("caseStatus",col[6]);
                results.add(activity);
             }
             return results;
        }catch(Exception e){
             e.printStackTrace();   
             throw new RuntimeException(e.getMessage());
        }
    }

      public List<Map<String,Object>>  findCaseManagementforChangeMachineByCriteria(String keyword, String customerType,String hospitalName){
        try{
            LOGGER.debug("findCaseManagementforChangeMachineByCriteria : {} :{} :{}",keyword,customerType,hospitalName);
            List<Object[] > listfromQuery = new ArrayList<>();
            StringBuilder criteriaSqlData = new StringBuilder();
            List<Map<String,Object>> results = new ArrayList<>();
            criteriaSqlData.append(" SELECT CM.ID , CM.CASE_NUMBER , CM.CREATED_DATE , CM.CASE_TYPE , NVL(CUST.PATIENT_NAME,CUST.HOSPITAL_NAME) CUST_NAME , CUST.CUSTOMER_TYPE ,CM.CASE_STATUS ");
            criteriaSqlData.append(" FROM CASE_MANAGEMENT CM   ");
            criteriaSqlData.append(" JOIN CUSTOMER CUST ON CUST.ID  = CM.CUSTOMER_ID  ");
            criteriaSqlData.append(" WHERE CM.CASE_STATUS = 'F' AND CM.CLOSE_FLAG IS NULL  ");
            if(keyword!=null) criteriaSqlData.append(" AND (CUST.patient_Name LIKE :keyword  OR CM.CASE_NUMBER = :case ) ") ; 
            if(hospitalName!=null)criteriaSqlData.append(" AND  (CUST.Hospital_Name like :hospitalName) ");
            criteriaSqlData.append(" AND CUST.CUSTOMER_TYPE = :customerType  ");
            // criteriaSqlData.append(" AND CM.CASE_NUMBER = :caseNumber  ");
            criteriaSqlData.append(" ORDER BY CM.CASE_NUMBER ");

            Query query = em.createNativeQuery(criteriaSqlData.toString());
           if(keyword!=null)  query.setParameter("keyword","%"+keyword+"%" );
           if(keyword!=null)  query.setParameter("case",keyword );
            query.setParameter("customerType",customerType );
           if(hospitalName!=null) query.setParameter("hospitalName","%"+hospitalName+"%" );
            listfromQuery = query.getResultList();
            for(Object[] col : listfromQuery){
                Map<String,Object> activity = new HashMap<>();
                activity.put("id",col[0]);
                activity.put("caseNumber",col[1]);
                activity.put("createdDate",col[2]);
                activity.put("caseType",col[3]);
                activity.put("cutsomerName",col[4]);
                activity.put("cutsomerType",col[5]);
                activity.put("caseStatus",col[6]);
                results.add(activity);
             }
             return results;
        }catch(Exception e){
             e.printStackTrace();   
             throw new RuntimeException(e.getMessage());
        }
    }


}