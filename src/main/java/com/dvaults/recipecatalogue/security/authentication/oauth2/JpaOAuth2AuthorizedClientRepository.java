package com.dvaults.recipecatalogue.security.authentication.oauth2;

import com.dvaults.recipecatalogue.configs.JwtConfigs;
import com.dvaults.recipecatalogue.configs.OAuth2AuthenticationConfigs;
import com.dvaults.recipecatalogue.modules.auth.services.api.specification.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.web.util.WebUtils;

import java.util.Objects;

@RequiredArgsConstructor
public class JpaOAuth2AuthorizedClientRepository implements OAuth2AuthorizedClientRepository {

  private final JpaOAuth2AuthorizedClientService authorizedClientService;
  private final AuthenticationService authenticationService;

  private final RequestCache requestCache;

  @Override
  public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(
      String clientRegistrationId,
      Authentication principal,
      HttpServletRequest request
  ) {
    return authorizedClientService.loadAuthorizedClient(clientRegistrationId, principal.getName());
  }

  @Override
  public void saveAuthorizedClient(
      OAuth2AuthorizedClient authorizedClient,
      Authentication principal,
      HttpServletRequest request,
      HttpServletResponse response
  ) {

    if (request.getRequestURI().startsWith(OAuth2AuthenticationConfigs.REDIRECT_URI)) {

      DefaultSavedRequest defaultSavedRequest = (DefaultSavedRequest) requestCache.getRequest(request, response);
      String savedRequestUri = defaultSavedRequest.getRequestURI();

      if (savedRequestUri.startsWith(OAuth2AuthenticationConfigs.SIGN_UP_AUTHORIZATION_REQUEST_URI)) {
        authorizedClientService.saveSigningUpAuthorizedClient(authorizedClient, principal);

      } else if (savedRequestUri.startsWith(OAuth2AuthenticationConfigs.SIGN_IN_AUTHORIZATION_REQUEST_URI)) {
        authorizedClientService.saveSigningInAuthorizedClient(authorizedClient, principal);

      } else if (savedRequestUri.startsWith(OAuth2AuthenticationConfigs.LINK_AUTHORIZATION_REQUEST_URI)) {
        authorizedClientService.linkAuthorizedClient(
            authorizedClient,
            principal,
            authenticationService.parseJwtAccessToken(Objects.requireNonNull(WebUtils.getCookie(request, JwtConfigs.ACCESS_TOKEN_COOKIE_NAME))
                .getValue())
        );

      } else {
        authorizedClientService.saveAuthorizedClient(authorizedClient, principal);
      }

    } else {
      authorizedClientService.saveAuthorizedClient(authorizedClient, principal);
    }

  }

  @Override
  public void removeAuthorizedClient(
      String clientRegistrationId,
      Authentication principal,
      HttpServletRequest request,
      HttpServletResponse response
  ) {
    authorizedClientService.removeAuthorizedClient(clientRegistrationId, principal.getName());
  }

}
