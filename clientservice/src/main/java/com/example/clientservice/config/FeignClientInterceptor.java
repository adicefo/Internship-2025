package com.example.clientservice.config;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Component;
@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private final OAuth2AuthorizedClientManager clientManager;

    public FeignClientInterceptor(OAuth2AuthorizedClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        OAuth2AuthorizedClient client = clientManager.authorize(OAuth2AuthorizeRequest.withClientRegistrationId("keycloak").principal("client-service").build());

        if (client != null) {
            String token = client.getAccessToken().getTokenValue();
            requestTemplate.header("Authorization", "Bearer " + token);
        }
    }
}
