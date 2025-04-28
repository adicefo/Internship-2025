package com.example.clientservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.core.authority.AuthorityUtils;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class CustomClientConfiguration {

  private static final String CLIENT_PRINCIPAL_IMPLICIT = "oauth2FeignClient";

  @Bean
  @Primary
  @ConditionalOnProperty(prefix = "spring.security.oauth2.client.registration.internshipApiSecuritySchemeImplicit", name = "enabled", havingValue = "true")
  public RequestInterceptor implicitOAuth2RequestInterceptor(final OAuth2AuthorizedClientManager implicitAuthorizedClientManager) {
     return new OAuth2RequestInterceptor(OAuth2AuthorizeRequest.withClientRegistrationId("internshipApiSecuritySchemeImplicit")
            .principal(new AnonymousAuthenticationToken(CLIENT_PRINCIPAL_IMPLICIT, CLIENT_PRINCIPAL_IMPLICIT, AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")))
            .build(), implicitAuthorizedClientManager);
  }

  @Bean
  @Primary
  @ConditionalOnProperty(prefix = "spring.security.oauth2.client.registration.internshipApiSecuritySchemeImplicit", name = "enabled", havingValue = "true")
  public OAuth2AuthorizedClientManager implicitAuthorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
        OAuth2AuthorizedClientService authorizedClientService) {
    return new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
  }
  
  public static class OAuth2RequestInterceptor implements RequestInterceptor {

    private final OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;
    private final OAuth2AuthorizeRequest oAuth2AuthorizeRequest;

    public OAuth2RequestInterceptor(OAuth2AuthorizeRequest oAuth2AuthorizeRequest, OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {
      this.oAuth2AuthorizeRequest = oAuth2AuthorizeRequest;
      this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
    }

    @Override
    public void apply(final RequestTemplate template) {
      // Changed to exactly use "Bearer " + jwt format
      String jwt = getAccessToken().getTokenValue();
      template.header("Authorization", "Bearer " + jwt);
    }

    public OAuth2AccessToken getAccessToken() {
      final OAuth2AuthorizedClient authorizedClient = oAuth2AuthorizedClientManager.authorize(oAuth2AuthorizeRequest);
      if (authorizedClient == null) {
        throw new OAuth2AuthenticationException("Client failed to authenticate");
      }
      return authorizedClient.getAccessToken();
    }
  }
}