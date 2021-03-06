package com.demo.spring.SpringBootOAuth2.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import  java.sql.*;
import oracle.jdbc.pool.OracleDataSource;

// @Configuration
// @EnableTransactionManagement
// @EnableJpaRepositories(basePackages = "com.demo.spring.SpringBootOAuth2.repository")
public class DatasourceConfig {

    
    // @Bean
    // DataSource dataSource() throws SQLException {
 
    //     OracleDataSource dataSource = new OracleDataSource();
    //     dataSource.setUser("DEMO");
    //     dataSource.setPassword("demo");
    //     dataSource.setURL("jdbc:oracle:thin:@//localhost:9721/xe");
    //     dataSource.setImplicitCachingEnabled(true);
    //     dataSource.setFastConnectionFailoverEnabled(true);
    //     return dataSource;
    // }

    // @Bean
    // public DataSource datasource() throws PropertyVetoException {
    //     EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
    //     EmbeddedDatabase dataSource = builder
    //             .setType(EmbeddedDatabaseType.H2)
    //             .addScript("sql-scripts/schema.sql")
    //             .addScript("sql-scripts/data.sql")
    //             .build();

    //     return dataSource;
    // }

    // @Bean
    // public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("datasource") DataSource ds) throws PropertyVetoException{
    //     LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
    //     entityManagerFactory.setDataSource(ds);
    //     entityManagerFactory.setPackagesToScan(new String[]{"com.demo.spring.SpringBootOAuth2.domain"});
    //     JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    //     entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
    //     return entityManagerFactory;
    // }

    // @Bean
    // public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
    //     JpaTransactionManager transactionManager = new JpaTransactionManager();
    //     transactionManager.setEntityManagerFactory(entityManagerFactory);
    //     return transactionManager;
    // }
}