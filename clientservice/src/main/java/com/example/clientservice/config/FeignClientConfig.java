
package com.example.clientservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
@Primary
@Profile("!noauth")
public class FeignClientConfig {

    @Autowired
    private OAuth2AuthorizedClientService clientService;
    
    @Bean
    @Primary
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("Current auth: " + (authentication != null ? authentication.getClass().getName() : "null"));
                
                if (authentication instanceof OAuth2AuthenticationToken) {
                    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                    OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                            oauthToken.getAuthorizedClientRegistrationId(),
                            oauthToken.getName());
                            
                    if (client != null && client.getAccessToken() != null) {
                        String tokenValue = client.getAccessToken().getTokenValue();
                        System.out.println("Adding token: Bearer " + tokenValue.substring(0, Math.min(10, tokenValue.length())) + "...");
                        template.header("Authorization", "Bearer " + tokenValue);
                    } else {
                        System.out.println("No OAuth2 client or token available");
                    }
                } else {
                    System.out.println("Not using OAuth2 token - using anonymous authentication");
                }
            }
        };
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}