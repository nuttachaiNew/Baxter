package com.demo.spring.SpringBootOAuth2.service.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.spt.engine.util.ConstantVariableUtil;

@Service
public class MailUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailUtil.class);
   
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    // @Autowired
    // SimpleMailMessage simpleMailMessage;
    
    // @Autowired
    // JavaMailSenderImpl mailSender;


    public void reconfigureMailSenders(String host, int port, String userName, String password) {
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(userName);
        mailSender.setPassword(password);
        LOGGER.debug("reconfigureMailSenders : {}  {} {} {}",mailSender.getHost(),mailSender.getPort(),mailSender.getUsername(),mailSender.getPassword());

    }

    public void sendMail(String mailSubject, String emailBody, List<String> receivers){
        LOGGER.debug("-= Send mail =-");
        try {

             Properties properties = System.getProperties();
             properties.setProperty("mail.smtp.auth","");
             properties.setProperty("mail.smtp.host",ConstantVariableUtil.MAIL_SERVER);
             properties.setProperty("mail.smtp.port",ConstantVariableUtil.MAIL_PORT );
             Session session = Session.getDefaultInstance(properties);
             MimeMessage mimeMessage = new MimeMessage(session);
             mimeMessage.setContent(new String(emailBody.getBytes(),"UTF-8"), "text/html; charset=utf-8");
             MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
             helper.setFrom(mailSender.getUsername());
             helper.setTo(receivers.toArray(new String[receivers.size()]));
             helper.setSubject("[MMVNCRS] -" + mailSubject);
             Transport.send(mimeMessage);
             LOGGER.debug("-= Send mail complete =-");
            
        } catch (MessagingException | UnsupportedEncodingException  e) {
            LOGGER.error("-= Send mail =-Error : {}",e);
        } catch (Exception e ){
            LOGGER.error("-= Send mail =-Error : {}",e);
        }
    
    }




}