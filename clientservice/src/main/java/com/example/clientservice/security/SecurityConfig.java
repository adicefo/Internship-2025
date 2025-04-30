package com.example.clientservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.client.web.*;
import org.springframework.security.oauth2.client.endpoint.*;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.web.*;
import org.springframework.security.oauth2.client.endpoint.*;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.web.*;
import org.springframework.security.oauth2.client.endpoint.*;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.web.*;
import org.springframework.security.oauth2.client.endpoint.*;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.web.*;
import org.springframework.security.oauth2.client.endpoint.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
        return http.build();
    }


}
