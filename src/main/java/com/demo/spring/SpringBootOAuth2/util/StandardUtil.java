package com.demo.spring.SpringBootOAuth2.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
// encode
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Base64;

@Component
public class StandardUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(StandardUtil.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    // for encrypt data
    public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: "
                    + Base64.encodeBase64String(encrypted));

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
    // for decryptData
    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    // for get current date return timestamp
    public static Timestamp getCurrentDate() {
        Timestamp today = null;
        try {
            Date nowDate = Calendar.getInstance().getTime();
            today = new java.sql.Timestamp(nowDate.getTime());
        } catch (Exception e) {
            LOGGER.error("error msg : {} ", e);
            throw new RuntimeException(e);
        }
        return today;
    }

    // param string date format dd-MM-yyyy
    public static Timestamp getTimeStamp(String stringDate) {
        Timestamp today = null;
        try {
            today = getDateWithRemoveTime(DATE_FORMAT.parse(stringDate));
        } catch (Exception e) {
            LOGGER.error("error msg : {} ", e);
            throw new RuntimeException(e);
        }
        return today;
    }

    public static Locale getSystemLocale() {
        LOGGER.info("getSystemLocale  Locale.US");
        return Locale.US;
    }
    // param string date format dd-MM-yyyy
    public static Timestamp getTimeStampGetMaxTime(String stringDate) {
        Timestamp today = null;
        try {
            today = getTimeMax(DATE_FORMAT.parse(stringDate));
        } catch (Exception e) {
            LOGGER.error("error msg : {} ", e);
            throw new RuntimeException(e);
        }

        return today;
    }


    public static Timestamp getTimeMax(Date date) {
        LOGGER.info("getDateWithRemoveTime : {} ", date);
        Timestamp maxTimeDate = null;
        try {
            SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy", StandardUtil.getSystemLocale());
            maxTimeDate = Timestamp.valueOf(newformat.format(date) + " " + "23:59:59.999");
            LOGGER.debug("getDateWithRemoveTime return : {}", maxTimeDate);
        } catch (Exception e) {
            LOGGER.error("error msg : {} ", e);
            throw new RuntimeException(e);
        }

        return maxTimeDate;
    }
    
    // param string date format dd-MM-yyyy
    public static Timestamp getDateWithRemoveTime(Date date) {
        LOGGER.info("getDateWithRemoveTime : {} ", date);
        Timestamp maxTimeDate = null;
        try {
            SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy", StandardUtil.getSystemLocale());
            maxTimeDate = Timestamp.valueOf(newformat.format(date) + " " + "00:00:00.000");
            LOGGER.debug("getDateWithRemoveTime return : {}", maxTimeDate);
        } catch (Exception e) {
            LOGGER.error("error msg : {} ", e);
            throw new RuntimeException(e);
        }
        return maxTimeDate;
    }
        
}

