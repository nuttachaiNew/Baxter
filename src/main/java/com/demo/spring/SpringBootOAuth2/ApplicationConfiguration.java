package com.demo.spring.SpringBootOAuth2;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.demo.spring.SpringBootOAuth2")
public class ApplicationConfiguration extends WebMvcConfigurerAdapter{
	
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(true).
                ignoreAcceptHeader(true).
                useJaf(false).mediaType("json", MediaType.APPLICATION_JSON);
    }


    // @Bean
    // public WebMvcConfigurer corsConfigurer() {
    //     return new WebMvcConfigurerAdapter() {
    //         @Override
    //         public void addCorsMappings(CorsRegistry registry) {
    //             registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET","POST","PUT", "DELETE");
    //         }
    //     };
    // }

    // @Bean
    // public FilterRegistrationBean corsFilter() {
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     CorsConfiguration config = new CorsConfiguration();
    //     config.setAllowCredentials(true);
    //     config.addAllowedOrigin("*");
    //     config.addAllowedHeader("*");
    //     config.addAllowedMethod("OPTIONS");
    //     config.addAllowedMethod("HEAD");
    //     config.addAllowedMethod("GET");
    //     config.addAllowedMethod("PUT");
    //     config.addAllowedMethod("POST");
    //     config.addAllowedMethod("DELETE");
    //     config.addAllowedMethod("PATCH");
    //     config.addAllowedMethod("*");
    //     source.registerCorsConfiguration("/**", config);
    //     FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CorsFilter(source));
    //     registrationBean.setOrder(0);
    //     CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
    //     characterEncodingFilter.setForceEncoding(true);
    //     characterEncodingFilter.setEncoding("UTF-8");
    //     registrationBean.setFilter(characterEncodingFilter);
    //     return registrationBean;

    // }
  
}