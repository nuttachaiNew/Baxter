package com.demo.spring.SpringBootOAuth2.service.impl;

import com.demo.spring.SpringBootOAuth2.domain.User;
import com.demo.spring.SpringBootOAuth2.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        LOGGER.error("------------------------------loadUserByUsername--------------------------------");
        try{
            User user = userRepository.findByUsername(s);
            LOGGER.error("user : {}",user.getUsername());

            if(user == null) {
                throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
            }
            LOGGER.error("--2.1--B : {}",user.getBranch().getId());
            LOGGER.error("--2.2--R : {}",user.getRole().getId());

            List<GrantedAuthority> authorities = new ArrayList<>();
            // user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
            //});

            LOGGER.error("--3--");

            UserDetails userDetails = new org.springframework.security.core.userdetails.
                    User(user.getUsername(), user.getPassword(), authorities);

            LOGGER.error("--4--");

            return userDetails;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }
}