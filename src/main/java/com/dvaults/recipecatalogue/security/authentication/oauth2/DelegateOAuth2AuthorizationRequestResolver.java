package com.dvaults.recipecatalogue.security.authentication.oauth2;

import com.dvaults.recipecatalogue.configs.OAuth2AuthenticationConfigs;
import com.dvaults.recipecatalogue.modules.auth.errors.AuthenticationErrorDictionary;
import com.dvaults.recipecatalogue.security.authentication.errors.exceptions.OAuth2SavedRequestAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

public class DelegateOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

  private final DefaultOAuth2AuthorizationRequestResolver delegateSignupRequestResolver;
  private final DefaultOAuth2AuthorizationRequestResolver delegateLoginRequestResolver;
  private final DefaultOAuth2AuthorizationRequestResolver delegateLinkRequestResolver;

  public DelegateOAuth2AuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
    this.delegateSignupRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
        clientRegistrationRepository,
        OAuth2AuthenticationConfigs.SIGN_UP_AUTHORIZATION_REQUEST_URI
    );
    this.delegateLoginRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
        clientRegistrationRepository,
        OAuth2AuthenticationConfigs.SIGN_IN_AUTHORIZATION_REQUEST_URI
    );
    this.delegateLinkRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
        clientRegistrationRepository,
        OAuth2AuthenticationConfigs.LINK_AUTHORIZATION_REQUEST_URI
    );
  }

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
    return request.getRequestURI().startsWith(OAuth2AuthenticationConfigs.REDIRECT_URI)
        ? null
        : intercept(getResolver(request).resolve(request));
  }

  @Override
  public OAuth2AuthorizationRequest resolve(
      HttpServletRequest request,
      String clientRegistrationId
  ) {
    return request.getRequestURI().startsWith(OAuth2AuthenticationConfigs.REDIRECT_URI)
        ? null
        : intercept(getResolver(request).resolve(request, clientRegistrationId));
  }

  private OAuth2AuthorizationRequest intercept(OAuth2AuthorizationRequest authorizationRequest) {

    if (authorizationRequest == null) {
      return null;
    }

    String clientRegistrationId = authorizationRequest.getAttribute(OAuth2ParameterNames.REGISTRATION_ID);
    if (clientRegistrationId == null) {
      return null;
    }

    OAuth2AuthorizationRequest.Builder oAuth2AuthorizationRequestBuilder = OAuth2AuthorizationRequest.from(authorizationRequest);

    if (clientRegistrationId.equals(OAuth2AuthenticationConfigs.GOOGLE_REGISTRATION_ID)) {
      oAuth2AuthorizationRequestBuilder.additionalParameters(OAuth2AuthenticationConfigs.GOOGLE_AUTHORIZATION_REQUEST_ADDITIONAL_PARAMETERS);
    }

    return oAuth2AuthorizationRequestBuilder.build();

  }

  private OAuth2AuthorizationRequestResolver getResolver(HttpServletRequest request) {

    String requestUri = request.getRequestURI();

    if (requestUri.startsWith(OAuth2AuthenticationConfigs.SIGN_UP_AUTHORIZATION_REQUEST_URI)) {
      return delegateSignupRequestResolver;
    } else if (requestUri.startsWith(OAuth2AuthenticationConfigs.SIGN_IN_AUTHORIZATION_REQUEST_URI)) {
      return delegateLoginRequestResolver;
    } else if (requestUri.startsWith(OAuth2AuthenticationConfigs.LINK_AUTHORIZATION_REQUEST_URI)) {
      return delegateLinkRequestResolver;
    }

    throw new OAuth2SavedRequestAuthenticationException(AuthenticationErrorDictionary.INVALID_OAUTH2_AUTHENTICATION_004);

  }

}
