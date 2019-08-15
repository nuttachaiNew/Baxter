package com.demo.spring.SpringBootOAuth2.service.impl;

import com.demo.spring.SpringBootOAuth2.domain.app.Machine;
import com.demo.spring.SpringBootOAuth2.domain.app.User;
import com.demo.spring.SpringBootOAuth2.repository.MachineRepository;
import com.demo.spring.SpringBootOAuth2.repository.MachineRepositoryCustom;
import com.demo.spring.SpringBootOAuth2.service.MachineService;
import com.demo.spring.SpringBootOAuth2.util.StandardUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Service
public class MachineServiceImpl implements MachineService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private MachineRepositoryCustom machineRepositoryCustom;

    JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            return json == null ? null : new Date(json.getAsLong());
        }
    };

    protected Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapter(Date.class, deser).create();

    @Override
    @Transactional
    public Map<String,String> saveMachine(String json,String user) {
        Map<String,String> result = new HashMap<>();
        try{
            LOGGER.info("===============Save Machine===============");
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);
            Machine machine = mapper.readValue(jsonObject.toString(),Machine.class);
            machine.setCreatedDate(StandardUtil.getCurrentDate());
            machineRepository.saveAndFlush(machine);
            result.put("code",machine.getCode());
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }
    }


       @Override
    @Transactional
    public Map<String,String> insertList(String json,String user) {
        Map<String,String> result = new HashMap<>();
        try{
            LOGGER.info("===============Save Machine===============");
            // ObjectMapper mapper = new ObjectMapper(json);
            JSONObject jsonObject = new JSONObject(json);
            ObjectMapper mapper = new ObjectMapper();

            List<Map<String,Object>> machineList = new JSONDeserializer<List<Map<String,Object>>>().deserialize(jsonObject.get("machineL").toString());
            for(Map<String,Object> machineObject : machineList){
               // Machine machine = mapper.readValue(machineObject.toString(),Machine.class);
                Machine machine = new Machine();
                machine.setCode(machineObject.get("code").toString());
                machine.setName(machineObject.get("name").toString());
                machine.setSerialNumber(machineObject.get("serialNumber") ==null?"": machineObject.get("serialNumber").toString());
                machine.setModelRef(machineObject.get("modelRef").toString());
                machine.setMachineType(machineObject.get("machineType").toString());
                machine.setCreatedBy(machineObject.get("createdBy").toString());
                machine.setStatus(1);
                machine.setCreatedDate(StandardUtil.getCurrentDate());
                machineRepository.saveAndFlush(machine);
            }
     
            
            // 
            // machine.setCreatedDate(StandardUtil.getCurrentDate());
            // machineRepository.saveAndFlush(machine);
            result.put("status","success");
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public Map<String, String> updateMachine(String json,String user) {
        Map<String,String> result = new HashMap<>();
        try{
            LOGGER.info("===============Update Machine===============");
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(json);
            Machine machineNew = mapper.readValue(jsonObject.toString(),Machine.class);
            Machine machineOld = machineRepository.findOne(machineNew.getId());
            machineNew = mapper.readerForUpdating(machineOld).readValue(gson.toJson(machineNew));

            if(machineOld != null && machineNew != null){
//                if( (machineNew.getVersion() != null && machineOld.getVersion() != null ) &&
//                        ( machineNew.getVersion().equals(machineOld.getVersion()) )
//                        ){
                    machineNew.setUpdatedDate(StandardUtil.getCurrentDate());
                    machineRepository.saveAndFlush(machineNew);
//                }else{
//                    throw new RuntimeException("Version machine is not match");
//                }
            }else{
                throw new RuntimeException("Machine is null");
            }
            result.put("code",machineNew.getCode());
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Machine findById(Long id) {

        try{
            LOGGER.info("===============Machine findById===============");
            LOGGER.info("id : {}",id);
            return machineRepository.findOne(id);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Machine> findAllMachines() {

        try{
            LOGGER.info("===============Machine findAllMachines===============");
            return machineRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String,Object>> findMachineByCriteria(JSONObject jsonObject) {
        return machineRepositoryCustom.findMachineByCriteria(jsonObject);
    }

    @Override
    @Transactional
    public void inActiveMachine(JSONObject jsonObject,String user) {

        try{
            LOGGER.info("===============Machine deleteMachine===============");

            JSONArray jsonArray = jsonObject.getJSONArray("id");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    Long id = Long.valueOf(jsonArray.get(i).toString());
                    Machine machine = machineRepository.findOne(id);
                    if(machine != null){
                        machine.setStatus(0);
                        machine.setUpdatedDate(StandardUtil.getCurrentDate());
                        machineRepository.saveAndFlush(machine);
                    }
                }
            }


        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Map<String,Object>> findMachineByMachineType(String type,String ref) {
        return machineRepositoryCustom.findMachineByMachineType(type,ref);
    }


    @Override
    @Transactional
    public Map<String,String> deleteMachine(JSONObject jsonObject) {

        Map<String,String> result = new HashMap<>();
        try{

            LOGGER.info("===============Machine deleteMachine===============");
            JSONArray jsonArray = jsonObject.getJSONArray("id");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    Long id = Long.valueOf(jsonArray.get(i).toString());
                    Machine machine = machineRepository.findOne(id);
                    if(machine != null){
                        machineRepository.delete(machine);
                    }
                }
            }
            result.put("msg","delete "+String.valueOf(jsonArray.length())+" success");
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Exception : {}",e);
            throw new RuntimeException(e);
        }

    }

}
