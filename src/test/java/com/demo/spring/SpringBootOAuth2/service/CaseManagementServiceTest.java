package com.demo.spring.SpringBootOAuth2.service;

import com.demo.spring.SpringBootOAuth2.AbstractApplicationTests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.junit.Assert;
import com.demo.spring.SpringBootOAuth2.domain.app.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
public class CaseManagementServiceTest  {
    private static final Logger LOGGER = LoggerFactory.getLogger(CaseManagementServiceTest.class);
    private static SimpleDateFormat FORMAT_DOCUMENT_NUMBER = new SimpleDateFormat("YYMM");

    @Autowired
    CaseManagementService caseManagementService;

    public void finished() throws Exception {
        LOGGER.info("-= Finished Tests =-");
    }

    @Test
    public void generateCaseNumberTest_case1(){
    	String 	caseNumber = caseManagementService.generateCaseNumber("CR");
        Assert.assertTrue(caseNumber.length() == 12);
    }

    @Test
    public void generateCaseNumberTest_case2(){
    	String 	caseNumber = caseManagementService.generateCaseNumber("CR");
        Assert.assertTrue(caseNumber.substring(0,2).equalsIgnoreCase("CR") );
    }

    @Test
    public void autoGenerateMachineByTypeAndStatusEqActiveTest(){
		Long id  = caseManagementService.autoGenerateMachineByTypeAndStatusEqActive("CR","MC1","MC10003xx1A");
        Assert.assertTrue(id!=null);
    }

    @Test 
    public void findByCaseNumberTest(){
        CaseManagement caseData = caseManagementService.findByCaseNumber("CR0818/01/02");
        Assert.assertTrue(caseData!=null);

    }

    @Test
	public void   findCaseByCriteria(){
		List<Map<String,Object>> results = caseManagementService.findCaseByCriteria("08-2018","%" ,null , null ,0 ,10,null);
        Assert.assertTrue(results.size()!=0);
	}

}
