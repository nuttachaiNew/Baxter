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
public class MachineRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    public List<Map<String,Object>> findMachineByCriteria(JSONObject jsonObject){

        try{

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
            List<Object[]> listfromQuery = new ArrayList<Object[]>();

            String machineType  = jsonObject.get("machineType").toString();
            String status       = jsonObject.get("status").toString();
            String search       = jsonObject.get("search").toString();
            Long usedDateFrom   = (Long) jsonObject.get("usedDateFrom");
            Long usedDateTo     = (Long) jsonObject.get("usedDateTo");
            String usedDateFromStr = null;
            String usedDateToStr = null;

            StringBuilder criteriaSqlData = new StringBuilder();
            criteriaSqlData.append("SELECT M.id ,M.version , M.code , M.name , M.machine_type , M.detail , M.status , M.start_used , M.end_used ");
            criteriaSqlData.append("FROM Machine M where UPPER(M.machine_type) like UPPER(:machineType) ");

            if(machineType == null || machineType.equals("")){
                machineType = "%%";
            }

            if(status != null && !status.equals("")){
                criteriaSqlData.append("and M.status = :status ");
            }

            if(search != null && !search.equals("")){
                search = '%'+search+'%';
                criteriaSqlData.append("and ( UPPER(M.code) like UPPER(:search) or UPPER(M.name) like UPPER(:search) )");
            }

            if(usedDateFrom != null){
                Date date = new Date(usedDateFrom);
                usedDateFromStr = format.format(date);
                criteriaSqlData.append("and TO_CHAR(M.start_used,'DD/MM/YYYY') = :usedDateFrom ");
            }

            if(usedDateTo != null){
                Date date = new Date(usedDateTo);
                usedDateToStr = format.format(date);
                criteriaSqlData.append("and TO_CHAR(M.end_used,'DD/MM/YYYY') = :usedDateTo ");
            }

            Query query = em.createNativeQuery(criteriaSqlData.toString());
            query.setParameter("machineType",machineType);

            if(status != null && !status.equals("")){
                query.setParameter("status",status);
            }

            if(search != null && !search.equals("")){
                query.setParameter("search",search);
            }

            if(usedDateFrom != null){
                query.setParameter("usedDateFrom",usedDateFromStr);
            }

            if(usedDateTo != null){
                query.setParameter("usedDateTo",usedDateToStr);
            }

            listfromQuery = query.getResultList();

            if(listfromQuery != null){
                for(Object [] o :listfromQuery ){
                    Map<String,Object> mapResult = new HashMap();
                    mapResult.put("id",o[0]);
                    mapResult.put("version",o[1]);
                    mapResult.put("code",o[2]);
                    mapResult.put("name",o[3]);
                    mapResult.put("machineType",o[4]);
                    mapResult.put("detail",o[5]);
                    mapResult.put("status",o[6]);
                    mapResult.put("startUsed",o[7]);
                    mapResult.put("endUsed",o[8]);
                    result.add(mapResult);
                }
            }

            return result;

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


        public List<Map<String,Object>> findMachineByMachineType(String type,String ref){
            try{
                List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
                List<Object[]> listfromQuery = new ArrayList<Object[]>();
                StringBuilder criteriaSqlData = new StringBuilder();
                criteriaSqlData.append(" SELECT ID , CODE , NAME , MACHINE_TYPE ");
                criteriaSqlData.append(" , STATUS , MODEL_REF , SERIAL_NUMBER ");
                criteriaSqlData.append(" FROM MACHINE ");
                criteriaSqlData.append(" WHERE MACHINE_TYPE = :type AND STATUS = 1 ");
                if(ref != null){
                    criteriaSqlData.append(" AND MODEL_REF = :ref ");
                }
                criteriaSqlData.append(" ORDER BY SERIAL_NUMBER , CODE , NAME ");
                Query query = em.createNativeQuery(criteriaSqlData.toString());
                query.setParameter("type",type);
                if(ref != null ) query.setParameter("ref",ref);
                 listfromQuery = query.getResultList();

                if(listfromQuery != null){
                for(Object [] o :listfromQuery ){
                    Map<String,Object> mapResult = new HashMap();
                    mapResult.put("id",o[0]);
                    mapResult.put("code",o[1]);
                    mapResult.put("name",o[2]);
                    mapResult.put("machineType",o[3]);
                    mapResult.put("status",o[4]);
                    mapResult.put("modelRef",o[5]);
                    mapResult.put("serialNumber",o[6]);
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