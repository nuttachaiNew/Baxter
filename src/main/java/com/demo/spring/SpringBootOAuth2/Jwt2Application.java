package com.demo.spring.SpringBootOAuth2;


import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jndi.JndiObjectFactoryBean;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.naming.NamingException;
import javax.sql.DataSource;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
/****************************************************************
 * Remark @SpringBootApplication when need to deployment on jboss
 * ***************************************************************
 * Remark //@Configuration,//@ComponentScan,//@EnableAutoConfiguration when run standalone
 * ***************************************************************
 * */
//@ComponentScan
//@EnableAutoConfiguration
// //@Configuration
@SpringBootApplication

public class Jwt2Application extends SpringBootServletInitializer {
	@Value("${spring.datasource.url}")
	private String DB_URL = "";
	@Value("${spring.datasource.username}")
	private String DB_USER = "";
	@Value("${spring.datasource.password}")
	private String DB_PASS = "";
	@Value("${spring.datasource.driver-class-name}")
	private String DB_DRIVER = "";

	public static void main(String[] args) {
		SpringApplication.run(Jwt2Application.class, args);
	}

	@Bean
	public TomcatEmbeddedServletContainerFactory tomcatFactory() {
		return new TomcatEmbeddedServletContainerFactory() {

			@Override
			protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(
					Tomcat tomcat) {
				tomcat.enableNaming();
				return super.getTomcatEmbeddedServletContainer(tomcat);
			}

			@Override
			protected void postProcessContext(Context context) {
				ContextResource resource = new ContextResource();
				resource.setName("jdbc/CRSDS");
				resource.setType(DataSource.class.getName());
				resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
				resource.setProperty("driverClassName", DB_DRIVER);
				resource.setProperty("url", DB_URL);
				resource.setProperty("username", DB_USER);
				resource.setProperty("password", DB_PASS);
				context.getNamingResources().addResource(resource);
			}
		};
	}

	@Bean(destroyMethod = "")
	public DataSource jndiDataSource() throws IllegalArgumentException, NamingException {
		JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
		bean.setJndiName("java:comp/env/jdbc/CRSDS");
		bean.setProxyInterface(DataSource.class);
		bean.setLookupOnStartup(false);
		bean.afterPropertiesSet();
		return (DataSource) bean.getObject();
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dsItems) {
		try {
			return new JdbcTemplate(this.jndiDataSource());
		} catch (NamingException e) {
			return new JdbcTemplate(dsItems);
		}
	}

}
