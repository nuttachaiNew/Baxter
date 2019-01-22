package com.demo.spring.SpringBootOAuth2.util;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class AbstractReportJasperPDF {

	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractReportJasperPDF.class);

	public static JasperPrint exportReport(String jasperFileName,List list,Map<String,Object> params){

		JasperPrint jasperPrint = null;
        InputStream jasperStream = null;

        try{
            LOGGER.info("AbstractReportJasperPDF");
            //JasperReport jasperReport = JasperCompileManager.compileReport("/home/aphirat_n/Documents/SCG/"+jasperFileName);
            //JasperReport jasperReport = (JasperReport) JRLoader.loadObject("src/main/resourczes/jasperreports/"+jasperFileName);

            Class cls = Class.forName("com.demo.spring.SpringBootOAuth2.util.AbstractReportJasperPDF");
            ClassLoader cLoader = cls.getClassLoader();
            jasperStream = cLoader.getResourceAsStream("jasperReport/"+jasperFileName);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

            JRDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(list);
            LOGGER.info("JRDataSource : {}",beanCollectionDataSource);
            jasperPrint = JasperFillManager.fillReport(jasperReport, params, beanCollectionDataSource);
            return jasperPrint;

        }catch (Exception e) {
            LOGGER.error("Error : {}", e);
            e.printStackTrace();
            return jasperPrint;
        }finally{
            IOUtils.closeQuietly(jasperStream);
        }
    }

}
