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
             LOGGER.debug("autoGenerateMachineByTypeAndStatusEqActive  type :{} :{} :{}  ",machineType,modelRef,serialNumber);
             Long result = null;
             List<Long> resultList = new ArrayList<>();
             List<Object[]> listfromQuery = new ArrayList<>();
             StringBuilder criteriaSqlData = new StringBuilder();
             criteriaSqlData.append("\n SELECT M.ID , M.CODE FROM MACHINE M  ");
             criteriaSqlData.append("\n WHERE M.MACHINE_TYPE = :machineType  and M.STATUS = 1  and M.MODEL_REF = :modelRef");
             if( !"".equalsIgnoreCase(serialNumber) ) criteriaSqlData.append("\n AND M.SERIAL_NUMBER  = :serialNumber ");
             criteriaSqlData.append("\n ORDER BY M.CODE ");
            LOGGER.debug("criteriaSqlData : {}",criteriaSqlData);
             Query query = em.createNativeQuery(criteriaSqlData.toString());
             query.setParameter("machineType",machineType);
             query.setParameter("modelRef",modelRef);
             if( !"".equalsIgnoreCase(serialNumber) ) query.setParameter("serialNumber",serialNumber);
            LOGGER.debug("before ");
             
            listfromQuery = query.getResultList();
            LOGGER.debug("after ");
         
             for(Object[] col : listfromQuery){
                Long id = col[0] == null? null : Long.valueOf( col[0].toString() );
                resultList.add(id);
             }
            LOGGER.debug("resultList : {}",resultList);
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

    public List<Map<String,Object>> findHistoryDocByAreaAndDocStatusAndRoleAndCase(String createdBy,Long areaId,String documentStatus,String roleBy,String actionUser , String actionDate){
        try{
            LOGGER.debug("findHistoryDocByAreaAndDocStatusAndRoleAndCase :{}:{}:{}",createdBy,areaId,documentStatus);
            actionDate = "".equalsIgnoreCase(actionDate) ? null :actionDate;
            createdBy = "".equalsIgnoreCase(createdBy) ? null :createdBy;
            actionUser = "".equalsIgnoreCase(actionUser) ? null :actionUser;
            
            List<Object[] > listfromQuery = new ArrayList<>();
            StringBuilder criteriaSqlData = new StringBuilder();
            List<Map<String,Object>> results = new ArrayList<>();
            criteriaSqlData.append(" SELECT  CM.CASE_NUMBER , CA.ACTION_STATUS , CA.ACTION_DATE , AU.USERNAME  , CM.CASE_TYPE ,CM.ID ,AR.ROLE_NAME");
            criteriaSqlData.append("\n FROM CASE_ACTIVITY CA ");
            criteriaSqlData.append("\n JOIN CASE_MANAGEMENT CM ON CM.ID = CA.CASE_MANAGEMENT ");
            criteriaSqlData.append("\n JOIN APP_USER AU ON AU.ID = CA.USER_ID ");
            criteriaSqlData.append("\n JOIN APP_ROLE AR ON AR.ID = AU.ROLE_ID ");
            criteriaSqlData.append("\n WHERE CM.CASE_STATUS IN (:documentStatus) ");
            if(areaId != null){
               criteriaSqlData.append("\n AND CM.AREA_ID = :areaId ");   
            }
            if(createdBy !=null ){
                criteriaSqlData.append("\n AND CM.CREATED_BY = :createdBy ");
            }
            if(actionUser!= null){
               criteriaSqlData.append("\n AND ( AU.USERNAME like :actionUser OR  AU.FIRST_NAME like :actionUser  OR AU.LAST_NAME like :actionUser ) ");   
            }
            if(actionDate!=null){
                criteriaSqlData.append("\n AND TRUNC(CA.action_Date) = TO_DATE(:actionDate,'DD-MM-YYYY') ");
            }

            criteriaSqlData.append("\n ORDER BY CA.ACTION_DATE DESC,CM.CASE_NUMBER ASC ");
            Query query = em.createNativeQuery(criteriaSqlData.toString());
            query.setParameter("documentStatus",Arrays.asList(documentStatus.split(",")) );
            if(areaId!=null) query.setParameter("areaId",areaId);
            if(createdBy!=null) query.setParameter("createdBy",createdBy);
            if(actionUser!=null) query.setParameter("actionUser","%"+actionUser+"%");
            if(actionDate!=null) query.setParameter("actionDate",actionDate);
            LOGGER.debug("statement : {}",criteriaSqlData);
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

    public List<Map<String,Object>> findCaseByCriteria(String date, String caseNumber , String areaId , String documentStatus ,Integer firstResult ,Integer maxResult,String caseType,String name){
        try{
            caseNumber = caseNumber == null?"":caseNumber;
            date = "".equalsIgnoreCase(date) ? null : date;
            LOGGER.debug("findCaseByCriteria :{} :{} :{} :{} :{}",date,caseNumber,documentStatus,firstResult,maxResult);
            List<Object[] > listfromQuery = new ArrayList<>();
            StringBuilder criteriaSqlData = new StringBuilder();
            List<Map<String,Object>> results = new ArrayList<>();
            criteriaSqlData.append(" SELECT CM.ID , CM.CASE_NUMBER , CM.CREATED_DATE , CM.CASE_TYPE , NVL(CUST.PATIENT_NAME,CUST.HOSPITAL_NAME) CUST_NAME , CUST.CUSTOMER_TYPE ,CM.CASE_STATUS ");
            criteriaSqlData.append(" FROM CASE_MANAGEMENT CM   ");
            criteriaSqlData.append(" JOIN CUSTOMER CUST ON CUST.ID  = CM.CUSTOMER_ID   ");
            criteriaSqlData.append(" WHERE 1 =1 ");
            if(date!=null) criteriaSqlData.append(" AND TRUNC(CM.CREATED_DATE) = TO_DATE(:date,'DD-MM-YYYY') ");
            // if(date!=null) criteriaSqlData.append(" AND TO_CHAR(CM.CREATED_DATE,'MM-YYYY') = :date ");
            criteriaSqlData.append(" AND CM.CASE_NUMBER  LIKE :caseNumber  ");
            if(areaId!=null)   criteriaSqlData.append(" AND CM.AREA_ID  = :areaId  ");
            if(documentStatus!=null )  criteriaSqlData.append(" AND CM.CASE_STATUS  = :documentStatus  ");
            if(caseType!=null) criteriaSqlData.append(" AND CM.CASE_TYPE  = :caseType  ");
            if(name!=null)  criteriaSqlData.append(" AND  (CUST.PATIENT_NAME LIKE :name OR CUST.HOSPITAL_NAME  LIKE :name  ) ");
            criteriaSqlData.append(" ORDER BY  CM.CREATED_DATE DESC,  CM.CASE_NUMBER  ASC ");
            Query query = em.createNativeQuery(criteriaSqlData.toString());
            if(date!=null)query.setParameter("date",date );
            query.setParameter("caseNumber","%"+caseNumber+"%" );
           if(name!=null) query.setParameter("name","%"+name+"%" );


            // SALE send I  , R 
            // ASM send W 
            // Other F
            if(documentStatus!=null ) query.setParameter("documentStatus",Arrays.asList(documentStatus.split(",")) );
            if(areaId!=null) query.setParameter("areaId",areaId );
            if(caseType!=null)query.setParameter("caseType",caseType);
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
            criteriaSqlData.append(" ORDER BY   CM.CREATED_DATE DESC,  CM.CASE_NUMBER  ASC");

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
            criteriaSqlData.append(" ORDER BY CM.CREATED_DATE DESC,  CM.CASE_NUMBER  ASC");

            LOGGER.debug("statement :{} ",criteriaSqlData);
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



    public List<Map<String,Object>> findCaseforOtherRole(String date, String caseNumber , String areaId , String documentStatus ,Integer firstResult ,Integer maxResult,String caseType,String role){
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
            if(date!=null && !"".equalsIgnoreCase(date)  ) criteriaSqlData.append(" AND TO_CHAR(CM.CREATED_DATE,'MM-YYYY') = :date ");
            criteriaSqlData.append(" AND CM.CASE_NUMBER  LIKE :caseNumber  ");
            if(areaId!=null && !"".equalsIgnoreCase(areaId))   criteriaSqlData.append(" AND CM.AREA_ID  = :areaId  ");
            if(documentStatus!=null && !"".equalsIgnoreCase(documentStatus) )  criteriaSqlData.append(" AND CM.CASE_STATUS  = :documentStatus   AND CM.CLOSE_FLAG IS NULL  ");
            if(caseType!=null && !"".equalsIgnoreCase(caseType)) criteriaSqlData.append(" AND CM.CASE_TYPE  = :caseType  ");
            if("BU".equalsIgnoreCase(role) )   criteriaSqlData.append(" AND CM.CASE_TYPE IN ('CR','AR') ");
            if("TS".equalsIgnoreCase(role) )  criteriaSqlData.append(" AND CM.assign_TS  IS NULL  ");
            if("FN".equalsIgnoreCase(role) )  criteriaSqlData.append(" AND CM.assign_FN  IS NULL  ");
            if("CS".equalsIgnoreCase(role) )  criteriaSqlData.append(" AND CM.assign_CS  IS NULL  ");

            criteriaSqlData.append(" ORDER BY CM.CREATED_DATE DESC,  CM.CASE_NUMBER  ASC  ");
            Query query = em.createNativeQuery(criteriaSqlData.toString());
            if(date!=null && !"".equalsIgnoreCase(date))query.setParameter("date",date );
            query.setParameter("caseNumber","%"+caseNumber+"%" );
            // SALE send I  , R 
            // ASM send W 
            // Other F
            if(documentStatus!=null && !"".equalsIgnoreCase(documentStatus) ) query.setParameter("documentStatus",Arrays.asList(documentStatus.split(",")) );
            if(areaId!=null && !"".equalsIgnoreCase(areaId)) query.setParameter("areaId",areaId );
            if(caseType!=null && !"".equalsIgnoreCase(caseType))query.setParameter("caseType",caseType);
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



    public List<Map<String,Object>>  countCaseOverAll(String caseStatus,String startDate, String endDate,String areaId){
        try{
            LOGGER.debug("countCaseOverAll : {} :{} :{}",caseStatus,startDate,endDate);
            List<Object[]> listfromQuery = new ArrayList<>();
            StringBuilder criteriaSqlData = new StringBuilder();
            List<Map<String,Object>> results = new ArrayList<>();
           criteriaSqlData.append(" SELECT  \n");
           criteriaSqlData.append(" CASE CM.CASE_TYPE   \n");
           criteriaSqlData.append("   WHEN 'CH' THEN 'Change Case'  \n");
           criteriaSqlData.append("   WHEN 'RT' THEN 'Return Case'   \n");
           criteriaSqlData.append("   WHEN 'CR' THEN 'Chronic Case'   \n");
           criteriaSqlData.append("   ELSE 'Acute Case'  END CASE_STATUS ,count(1)    \n");

           criteriaSqlData.append(" FROM CASE_MANAGEMENT CM  \n");
           criteriaSqlData.append(" WHERE CM.CASE_STATUS = :caseStatus  \n");
           criteriaSqlData.append(" AND TRUNC(CM.CREATED_DATE) BETWEEN TO_DATE(:startDate,'DD-MM-YYYY') AND TO_DATE(:endDate,'DD-MM-YYYY')  \n");
           if(areaId !=null) criteriaSqlData.append(" AND CM.AREA_ID = :areaId   \n"); 

           criteriaSqlData.append(" GROUP BY CM.CASE_TYPE  \n");

             Query query = em.createNativeQuery(criteriaSqlData.toString());
             query.setParameter("startDate",startDate );
             query.setParameter("endDate",endDate );
             query.setParameter("caseStatus",caseStatus );
             if(areaId!=null) query.setParameter("areaId",areaId);
            listfromQuery = query.getResultList();
            for(Object[] col : listfromQuery){
                Map<String,Object> activity = new HashMap<>();
                activity.put("caseType",col[0]);  
                activity.put("count",col[1]);  
                results.add(activity);
             }
             return results;
        }catch(Exception e){
             e.printStackTrace();   
             throw new RuntimeException(e.getMessage());
        }
    }







    public List<Map<String,Object>>  countCaseShowInDashboard(String caseType,String startDate, String endDate ,String createdBy,String areaId ){
        try{
            LOGGER.debug("countCaseShowInDashboard : {} :{} :{}",caseType,startDate,endDate);
            List<Object[]> listfromQuery = new ArrayList<>();
            StringBuilder criteriaSqlData = new StringBuilder();
            List<Map<String,Object>> results = new ArrayList<>();
            criteriaSqlData.append("SELECT  ");
            criteriaSqlData.append(" CASE CM.CASE_STATUS   ");
            criteriaSqlData.append(" WHEN 'I' THEN 'Create'  ");
            criteriaSqlData.append(" WHEN 'W' THEN 'Sent'  ");
            criteriaSqlData.append(" WHEN 'F' THEN 'Approve'  ");
            criteriaSqlData.append(" WHEN 'R' THEN 'Reject'  ");
            criteriaSqlData.append(" END CASE_STATUS  ");
            criteriaSqlData.append(" ,COUNT(1) QTY  ");
            criteriaSqlData.append(" FROM CASE_MANAGEMENT CM  ");
            criteriaSqlData.append(" WHERE    ");
            criteriaSqlData.append(" CM.CASE_TYPE = :caseType  ");
            criteriaSqlData.append(" AND TRUNC(CM.CREATED_DATE) BETWEEN TO_DATE(:startDate,'DD-MM-YYYY') AND TO_DATE(:endDate,'DD-MM-YYYY')  ");
            if(createdBy!=null)criteriaSqlData.append(" AND CM.CREATED_BY= :createdBy ");
            if(areaId !=null) criteriaSqlData.append(" AND CM.AREA_ID = :areaId   \n"); 
           
            criteriaSqlData.append(" GROUP BY CM.CASE_STATUS  ");


             Query query = em.createNativeQuery(criteriaSqlData.toString());
          
             query.setParameter("startDate",startDate );
             query.setParameter("endDate",endDate );
             query.setParameter("caseType",caseType );
             if(createdBy!=null)query.setParameter("createdBy",createdBy);
             if(areaId!=null)query.setParameter("areaId",areaId);

            listfromQuery = query.getResultList();
            for(Object[] col : listfromQuery){
                Map<String,Object> activity = new HashMap<>();
                activity.put("caseStatus",col[0]);  
                activity.put("count",col[1]);  
                results.add(activity);
             }
             return results;
        }catch(Exception e){
             e.printStackTrace();   
             throw new RuntimeException(e.getMessage());
        }
    }





    public List<Map<String,Object>>  getCaseDetailShowInDashboard(String caseStatus,String startDate, String endDate,String areaId,String caseType){
        try{
            LOGGER.debug("getCaseDetailShowInDashboard : {} :{} :{}",caseStatus,startDate,endDate);
            List<Object[]> listfromQuery = new ArrayList<>();
            StringBuilder criteriaSqlData = new StringBuilder();
            List<Map<String,Object>> results = new ArrayList<>();
           criteriaSqlData.append(" SELECT  \n");
           criteriaSqlData.append(" ROWNUM ,  CM.CASE_NUMBER  , cs.patient_name || ' '||cs.patient_last_name customer  \n");
           criteriaSqlData.append(",cs.current_address1 || ' '|| cs.current_address2 || ' '|| cs.current_sub_district || ' ' ||cs.current_district|| ' ' || cs.current_district || ' ' || cs.current_province  || ' ' || cs.current_zip_code address   \n");
           criteriaSqlData.append(" , '' activeDate ");
           criteriaSqlData.append(" FROM CASE_MANAGEMENT CM  \n");
           criteriaSqlData.append(" JOIN CUSTOMER CS ON CS.ID = CM.CUSTOMER_ID ");
           criteriaSqlData.append(" WHERE CM.CASE_STATUS = :caseStatus  \n");
           criteriaSqlData.append(" AND CM.CASE_TYPE = :caseType ");
           criteriaSqlData.append(" AND TRUNC(CM.CREATED_DATE) BETWEEN TO_DATE(:startDate,'DD-MM-YYYY') AND TO_DATE(:endDate,'DD-MM-YYYY')  \n");
           if(areaId !=null) criteriaSqlData.append(" AND CM.AREA_ID = :areaId   \n"); 
           criteriaSqlData.append(" ORDER BY CM.CASE_NUMBER ");


             Query query = em.createNativeQuery(criteriaSqlData.toString());
             query.setParameter("startDate",startDate );
             query.setParameter("endDate",endDate );
             query.setParameter("caseStatus",caseStatus );
             query.setParameter("caseType",caseType );
             if(areaId!=null) query.setParameter("areaId",areaId);
            listfromQuery = query.getResultList();
            for(Object[] col : listfromQuery){
                Map<String,Object> activity = new HashMap<>();
                activity.put("order",col[0]);
                activity.put("caseNumber",col[1]);
                activity.put("customer",col[2]);
                activity.put("address",col[3]);
                activity.put("activeDate",col[4]);
                results.add(activity);
             }
             return results;
        }catch(Exception e){
             e.printStackTrace();   
             throw new RuntimeException(e.getMessage());
        }
    }





}