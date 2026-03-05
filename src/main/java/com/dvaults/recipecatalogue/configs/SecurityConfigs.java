package com.dvaults.recipecatalogue.configs;

import com.dvaults.recipecatalogue.modules.auth.services.api.specification.AuthenticationService;
import com.dvaults.recipecatalogue.security.authentication.basic.BasicAuthenticationTokenConverter;
import com.dvaults.recipecatalogue.security.authentication.basic.PostBasicAuthenticationFilter;
import com.dvaults.recipecatalogue.security.authentication.basic.SignInBasicAuthenticationProvider;
import com.dvaults.recipecatalogue.security.authentication.basic.SignUpBasicAuthenticationProvider;
import com.dvaults.recipecatalogue.security.authentication.errors.handlers.JsonAuthenticationEntryPoint;
import com.dvaults.recipecatalogue.security.authentication.oauth2.SavedRequestOAuth2AuthenticationFailureHandler;
import com.dvaults.recipecatalogue.security.authentication.oauth2.SavedRequestOAuth2AuthenticationSuccessHandler;
import com.dvaults.recipecatalogue.security.authentication.oauth2.SavedRequestOAuth2Filter;
import com.dvaults.recipecatalogue.security.authentication.rest.JwtCookieAuthenticationFilter;
import com.dvaults.recipecatalogue.security.authorization.errors.handlers.JsonAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfigs {

  @Bean
  @Order(1)
  public SecurityFilterChain basicAuthenticationFilterChain(
      HttpSecurity http,
      SignUpBasicAuthenticationProvider signupBasicAuthenticationProvider,
      SignInBasicAuthenticationProvider signinBasicAuthenticationProvider,
      AuthenticationService authenticationService,
      AccessDeniedHandler jsonAccessDeniedHandler
  ) throws Exception {

    AuthenticationManager authenticationManager = new ProviderManager(
        signupBasicAuthenticationProvider,
        signinBasicAuthenticationProvider
    );

    http.securityMatcher(BasicAuthenticationConfigs.SIGN_UP_URI, BasicAuthenticationConfigs.SIGN_IN_URI)
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .anonymous(AbstractHttpConfigurer::disable)
        .sessionManagement(sessionManagerConfigurer ->
            sessionManagerConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationManager(authenticationManager)
        .addFilterAt(
            new PostBasicAuthenticationFilter(
                authenticationManager,
                new JsonAuthenticationEntryPoint(),
                new BasicAuthenticationTokenConverter(),
                authenticationService),
            BasicAuthenticationFilter.class)
        .exceptionHandling(exceptionHandlingConfigurer ->
            exceptionHandlingConfigurer.accessDeniedHandler(jsonAccessDeniedHandler))
        .authorizeHttpRequests(authorizeHttpRequestsCustomizer ->
            authorizeHttpRequestsCustomizer.anyRequest().authenticated());

    return http.build();

  }

  @Bean
  @Order(2)
  public SecurityFilterChain oAuth2AuthenticationFilterChain(
      HttpSecurity http,
      RequestCache requestCache,
      OAuth2AuthorizationRequestResolver delegateOAuth2AuthorizationRequestResolver,
      OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
      OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository,
      AuthenticationService authenticationService
  ) throws Exception {

    OAuth2AuthorizationRequestRedirectFilter oAuth2AuthorizationRequestRedirectFilter =
        new OAuth2AuthorizationRequestRedirectFilter(delegateOAuth2AuthorizationRequestResolver);
    oAuth2AuthorizationRequestRedirectFilter.setRequestCache(requestCache);
    oAuth2AuthorizationRequestRedirectFilter.setAuthenticationFailureHandler(
        new SavedRequestOAuth2AuthenticationFailureHandler(requestCache)
    );

    http.securityMatcher(Stream.of(OAuth2AuthenticationConfigs.SIGN_UP_AUTHORIZATION_REQUEST_URI,
                OAuth2AuthenticationConfigs.SIGN_IN_AUTHORIZATION_REQUEST_URI,
                OAuth2AuthenticationConfigs.LINK_AUTHORIZATION_REQUEST_URI,
                OAuth2AuthenticationConfigs.REDIRECT_URI)
            .flatMap(baseUri -> Stream.of(OAuth2AuthenticationConfigs.GOOGLE_REGISTRATION_ID,
                    OAuth2AuthenticationConfigs.GITHUB_REGISTRATION_ID)
                .map(registrationId -> baseUri + "/" + registrationId))
            .toArray(String[]::new))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .anonymous(AbstractHttpConfigurer::disable)
        .sessionManagement(sessionManagerConfigurer ->
            sessionManagerConfigurer.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation().migrateSession())
        .addFilterBefore(
            new SavedRequestOAuth2Filter(
                authenticationService,
                requestCache,
                new JsonAuthenticationEntryPoint()),
            OAuth2AuthorizationRequestRedirectFilter.class)
        .addFilterAt(oAuth2AuthorizationRequestRedirectFilter, OAuth2AuthorizationRequestRedirectFilter.class)
        .oauth2Login(oauth2LoginConfigurer ->
            oauth2LoginConfigurer.successHandler(
                    new SavedRequestOAuth2AuthenticationSuccessHandler(
                        requestCache,
                        authenticationService))
                .failureHandler(new SavedRequestOAuth2AuthenticationFailureHandler(requestCache))
                .redirectionEndpoint(redirectionEndpointCustomizer ->
                    redirectionEndpointCustomizer.baseUri(OAuth2AuthenticationConfigs.REDIRECT_MATCHER_URI_PATTERN))
                .authorizedClientService(oAuth2AuthorizedClientService)
                .authorizedClientRepository(oAuth2AuthorizedClientRepository))
        .authorizeHttpRequests(authorizeHttpRequestsCustomizer ->
            authorizeHttpRequestsCustomizer.requestMatchers(OAuth2AuthenticationConfigs.REDIRECT_MATCHER_URI_PATTERN).permitAll()
                .anyRequest().authenticated());

    return http.build();

  }

  @Bean
  @Order(3)
  public SecurityFilterChain restApiSecurityFilterChain(
      HttpSecurity http,
      AccessDeniedHandler jsonAccessDeniedHandler,
      AuthenticationService authenticationService
  ) throws Exception {

    SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    http.csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .anonymous(AbstractHttpConfigurer::disable)
        .securityContext(securityContextConfigurer ->
            securityContextConfigurer.securityContextRepository(securityContextRepository))
        .sessionManagement(sessionManagerConfigurer ->
            sessionManagerConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(exceptionHandlingConfigurer ->
            exceptionHandlingConfigurer.accessDeniedHandler(jsonAccessDeniedHandler))
        .addFilterBefore(
            new JwtCookieAuthenticationFilter(securityContextRepository, authenticationService),
            AuthorizationFilter.class)
        .authorizeHttpRequests(authorizeHttpRequestsCustomizer ->
            authorizeHttpRequestsCustomizer.requestMatchers("/auth/sign-out", JwtConfigs.REFRESH_TOKEN_URI).permitAll()
                .anyRequest().authenticated());

    return http.build();

  }

  @Bean
  public AccessDeniedHandler jsonAccessDeniedHandler() {
    return new JsonAccessDeniedHandler();
  }

  @Bean
  public GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults("");
  }

  @Bean
  public CorsConfiguration corsConfiguration() {

    CorsConfiguration corsConfiguration = new CorsConfiguration();

    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.setAllowedOriginPatterns(List.of(
        "http://localhost:*",
        "http://127.0.0.1:*",
        "https://staging.recipecatalogue.com",
        "https://recipecatalogue.com"
    ));
    corsConfiguration.setAllowedMethods(List.of(
        "GET",
        "POST",
        "PUT",
        "DELETE",
        "PATCH",
        "OPTIONS"
    ));
    corsConfiguration.setAllowedHeaders(List.of(
        "Authorization",
        "Cache-Control",
        "Content-Type"
    ));
    corsConfiguration.addExposedHeader("*");

    return corsConfiguration;

  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource(CorsConfiguration corsConfiguration) {

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);

    return source;

  }

}
