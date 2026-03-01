package com.dvaults.recipecatalogue.security.authentication.oauth2;

import com.dvaults.recipecatalogue.common.jwt.UserJwt;
import com.dvaults.recipecatalogue.modules.auth.services.api.specification.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

  private final AuthenticationService authenticationService;

  @Override
  @SuppressWarnings("unchecked")
  public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(
      String clientRegistrationId,
      String principalName
  ) {
    return (T) authenticationService.loadOAuth2AuthorizedClient(clientRegistrationId, principalName);
  }

  public void saveSigningUpAuthorizedClient(
      OAuth2AuthorizedClient oAuth2AuthorizedClient,
      Authentication oAuth2Principal
  ) {
    authenticationService.saveSigningUpOAuth2AuthorizedClient(oAuth2AuthorizedClient, oAuth2Principal);
  }

  public void saveSigningInAuthorizedClient(
      OAuth2AuthorizedClient oAuth2AuthorizedClient,
      Authentication oAuth2Principal
  ) {
    authenticationService.saveSigningInOAuth2AuthorizedClient(oAuth2AuthorizedClient, oAuth2Principal);
  }

  public void linkAuthorizedClient(
      OAuth2AuthorizedClient oAuth2AuthorizedClient,
      Authentication oAuth2Principal,
      UserJwt principalJwt
  ) {
    authenticationService.linkOAuth2AuthorizedClient(oAuth2AuthorizedClient, oAuth2Principal, principalJwt);
  }

  @Override
  public void saveAuthorizedClient(
      OAuth2AuthorizedClient oAuth2AuthorizedClient,
      Authentication oAuth2Principal
  ) {
    authenticationService.saveOAuth2AuthorizedClient(oAuth2AuthorizedClient, oAuth2Principal);
  }

  @Override
  public void removeAuthorizedClient(
      String clientRegistrationId,
      String principalName
  ) {
    authenticationService.removeOAuth2AuthorizedClient(clientRegistrationId, principalName);
  }

}
