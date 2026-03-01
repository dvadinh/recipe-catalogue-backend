package com.dvaults.recipecatalogue.configs;

import com.dvaults.recipecatalogue.modules.auth.services.api.specification.AuthenticationService;
import com.dvaults.recipecatalogue.security.authentication.oauth2.DelegateOAuth2AuthorizationRequestResolver;
import com.dvaults.recipecatalogue.security.authentication.oauth2.JpaOAuth2AuthorizedClientRepository;
import com.dvaults.recipecatalogue.security.authentication.oauth2.JpaOAuth2AuthorizedClientService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import java.util.List;
import java.util.Map;

@Configuration
public class OAuth2AuthenticationConfigs {

  public static final String GOOGLE_REGISTRATION_ID = "google";
  public static final String GITHUB_REGISTRATION_ID = "github";
  public static final List<String> ALL_REGISTRATION_IDS = List.of(
      GOOGLE_REGISTRATION_ID,
      GITHUB_REGISTRATION_ID
  );

  public static final String SIGN_UP_AUTHORIZATION_REQUEST_URI = "/auth/oauth2/sign-up";
  public static final String SIGN_IN_AUTHORIZATION_REQUEST_URI = "/auth/oauth2/sign-in";
  public static final String LINK_AUTHORIZATION_REQUEST_URI = "/auth/oauth2/link";

  public static final String REDIRECT_URI = "/auth/oauth2/code";
  public static final String REDIRECT_MATCHER_URI_PATTERN = "/auth/oauth2/code/*";

  public static final Map<String, Object> GOOGLE_AUTHORIZATION_REQUEST_ADDITIONAL_PARAMETERS = Map.of(
      "access_type", "offline",
      "prompt", "consent"
  );

  public static final String STATUS_QUERY_PARAMETER_NAME = "status";
  public static final String ERROR_QUERY_PARAMETER_NAME = "error";
  public static final String TARGET_URL_QUERY_PARAMETER_NAME = "target_url";
  public static final String FAILURE_STATUS_QUERY_PARAMETER_VALUE = "oauth2_failure";
  public static final String SUCCESS_STATUS_QUERY_PARAMETER_VALUE = "oauth2_success";

  @Bean
  @ConfigurationProperties("oauth2.client.registration.google")
  public OAuth2ClientProperties.Registration googleOAuth2ClientPropertiesRegistration() {
    return new OAuth2ClientProperties.Registration();
  }

  @Bean
  @ConfigurationProperties("oauth2.client.provider.google")
  public OAuth2ClientProperties.Provider googleOAuth2ProviderPropertiesRegistration() {
    return new OAuth2ClientProperties.Provider();
  }

  @Bean
  public ClientRegistration googleOidcClientRegistration(
      OAuth2ClientProperties.Registration googleOAuth2ClientPropertiesRegistration,
      OAuth2ClientProperties.Provider googleOAuth2ProviderPropertiesRegistration
  ) {
    return ClientRegistrations.fromOidcIssuerLocation(googleOAuth2ProviderPropertiesRegistration.getIssuerUri())
        .registrationId(GOOGLE_REGISTRATION_ID)
        .clientId(googleOAuth2ClientPropertiesRegistration.getClientId())
        .clientSecret(googleOAuth2ClientPropertiesRegistration.getClientSecret())
        .scope(googleOAuth2ClientPropertiesRegistration.getScope())
        .redirectUri(googleOAuth2ClientPropertiesRegistration.getRedirectUri())
        .authorizationGrantType(new AuthorizationGrantType(googleOAuth2ClientPropertiesRegistration.getAuthorizationGrantType()))
        .build();
  }

  @Bean
  @ConfigurationProperties("oauth2.client.registration.github")
  public OAuth2ClientProperties.Registration githubOAuth2ClientPropertiesRegistration() {
    return new OAuth2ClientProperties.Registration();
  }

  @Bean
  @ConfigurationProperties("oauth2.client.provider.github")
  public OAuth2ClientProperties.Provider githubOAuth2ProviderPropertiesRegistration() {
    return new OAuth2ClientProperties.Provider();
  }

  @Bean
  public ClientRegistration githubOAuth2ClientRegistration(
      OAuth2ClientProperties.Registration githubOAuth2ClientPropertiesRegistration,
      OAuth2ClientProperties.Provider githubOAuth2ProviderPropertiesRegistration
  ) {
    return CommonOAuth2Provider.GITHUB.getBuilder(GITHUB_REGISTRATION_ID)
        .clientId(githubOAuth2ClientPropertiesRegistration.getClientId())
        .clientSecret(githubOAuth2ClientPropertiesRegistration.getClientSecret())
        .redirectUri(githubOAuth2ClientPropertiesRegistration.getRedirectUri())
        .issuerUri(githubOAuth2ProviderPropertiesRegistration.getIssuerUri())
        .build();
  }

  @Bean
  public ClientRegistrationRepository clientRegistrationRepository(
      ClientRegistration googleOidcClientRegistration,
      ClientRegistration githubOAuth2ClientRegistration
  ) {
    return new InMemoryClientRegistrationRepository(
        googleOidcClientRegistration,
        githubOAuth2ClientRegistration
    );
  }

  @Bean
  public OAuth2AuthorizedClientRepository jpaOAuth2AuthorizedClientRepository(
      JpaOAuth2AuthorizedClientService jpaOAuth2AuthorizedClientService,
      AuthenticationService authenticationService,
      RequestCache requestCache
  ) {
    return new JpaOAuth2AuthorizedClientRepository(
        jpaOAuth2AuthorizedClientService,
        authenticationService,
        requestCache
    );
  }

  @Bean
  public OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(
      ClientRegistrationRepository clientRegistrationRepository,
      OAuth2AuthorizedClientRepository jpaOAuth2AuthorizedClientRepository
  ) {

    DefaultOAuth2AuthorizedClientManager clientManager = new DefaultOAuth2AuthorizedClientManager(
        clientRegistrationRepository,
        jpaOAuth2AuthorizedClientRepository
    );
    clientManager.setAuthorizedClientProvider(
        OAuth2AuthorizedClientProviderBuilder.builder()
            .authorizationCode()
            .clientCredentials()
            .refreshToken()
            .clientCredentials()
            .build()
    );

    return clientManager;

  }

  @Bean
  public OAuth2AuthorizationRequestResolver oAuth2AuthorizationRequestResolver(
      ClientRegistrationRepository clientRegistrationRepository
  ) {
    return new DelegateOAuth2AuthorizationRequestResolver(clientRegistrationRepository);
  }

  @Bean
  public RequestCache requestCache() {
    return new HttpSessionRequestCache();
  }

}
