package com.dvaults.recipecatalogue.modules.auth.mappers;

import com.dvaults.recipecatalogue.modules.auth.dtos.linkedoauth2account.responses.LinkedOAuth2AccountResponse;
import com.dvaults.recipecatalogue.modules.auth.models.LinkedOAuth2Account;
import com.dvaults.recipecatalogue.modules.auth.models.LinkedOAuth2AccountId;
import lombok.AccessLevel;
import lombok.Setter;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    builder = @Builder(disableBuilder = true),
    collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
    nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
@Setter(value = AccessLevel.PROTECTED, onMethod_ = @Autowired)
public abstract class LinkedOAuth2AccountMapper {

  private ClientRegistrationRepository clientRegistrationRepository;

  @Named("mapToClientRegistration")
  protected ClientRegistration mapToClientRegistration(LinkedOAuth2AccountId linkedOAuth2AccountId) {

    if (linkedOAuth2AccountId == null) {
      return null;
    }

    return clientRegistrationRepository.findByRegistrationId(linkedOAuth2AccountId.getClientRegistrationId());

  }

  @Named("mapToOAuth2AccessTokenTokenType")
  protected OAuth2AccessToken.TokenType mapToOAuth2AccessTokenTokenType(String accessTokenType) {

    if (!StringUtils.hasText(accessTokenType)) {
      return OAuth2AccessToken.TokenType.BEARER;
    }

    if ("bearer".equalsIgnoreCase(accessTokenType)) {
      return OAuth2AccessToken.TokenType.BEARER;
    }

    return new OAuth2AccessToken.TokenType(accessTokenType);

  }

  @Named("mapToAccessTokenScopeSet")
  protected Set<String> mapToAccessTokenScopeSet(String accessTokenScopes) {

    if (!StringUtils.hasText(accessTokenScopes)) {
      return Set.of();
    }

    return Arrays.stream(accessTokenScopes.trim()
            .split("\\s+"))
        .filter(StringUtils::hasText)
        .map(String::trim)
        .collect(Collectors.toSet());

  }

  @Named("mapToOAuth2AccessToken")
  protected OAuth2AccessToken mapToOAuth2AccessToken(LinkedOAuth2Account linkedOAuth2Account) {

    if (linkedOAuth2Account == null
        || !StringUtils.hasText(linkedOAuth2Account.getAccessTokenValue())
    ) {
      return null;
    }

    return new OAuth2AccessToken(
        mapToOAuth2AccessTokenTokenType(linkedOAuth2Account.getAccessTokenType()),
        linkedOAuth2Account.getAccessTokenValue(),
        linkedOAuth2Account.getAccessTokenIssuedAt(),
        linkedOAuth2Account.getAccessTokenExpiresAt(),
        mapToAccessTokenScopeSet(linkedOAuth2Account.getAccessTokenScopes())
    );

  }

  @Named("mapToOAuth2RefreshToken")
  protected OAuth2RefreshToken mapToOAuth2RefreshToken(LinkedOAuth2Account linkedOAuth2Account) {

    if (linkedOAuth2Account == null
        || !StringUtils.hasText(linkedOAuth2Account.getRefreshTokenValue())
    ) {
      return null;
    }

    return new OAuth2RefreshToken(
        linkedOAuth2Account.getRefreshTokenValue(),
        linkedOAuth2Account.getRefreshTokenIssuedAt(),
        linkedOAuth2Account.getRefreshTokenExpiresAt()
    );

  }

  @Named("toOAuth2AuthorizedClient")
  public OAuth2AuthorizedClient toOAuth2AuthorizedClient(LinkedOAuth2Account linkedOAuth2Account) {

    if (linkedOAuth2Account == null) {
      return null;
    }

    return new OAuth2AuthorizedClient(
        mapToClientRegistration(linkedOAuth2Account.getId()),
        linkedOAuth2Account.getId().getPrincipalName(),
        mapToOAuth2AccessToken(linkedOAuth2Account),
        mapToOAuth2RefreshToken(linkedOAuth2Account)
    );

  }

  @Named("mapToAccessTokenType")
  protected String mapToAccessTokenType(String accessTokenType) {

    if (!StringUtils.hasText(accessTokenType)) {
      return null;
    }

    if ("bearer".equalsIgnoreCase(accessTokenType)) {
      return "Bearer";
    }

    return accessTokenType.trim();

  }

  @Named("mapToAccessTokenScopes")
  protected String mapToAccessTokenScopes(Set<String> accessTokenScopeSet) {

    if (accessTokenScopeSet == null
        || accessTokenScopeSet.isEmpty()
    ) {
      return null;
    }

    return accessTokenScopeSet.stream()
        .filter(StringUtils::hasText)
        .map(String::trim)
        .collect(Collectors.joining(" "));

  }

  @Named("toLinkedOAuth2Account")
  public LinkedOAuth2Account toLinkedOAuth2Account(OAuth2AuthorizedClient oAuth2AuthorizedClient) {
    return toLinkedOAuth2Account(new LinkedOAuth2Account(), oAuth2AuthorizedClient);
  }

  @Named("toLinkedOAuth2Account")
  @Mapping(target = "id", source = "oAuth2AuthorizedClient")
  @Mapping(target = "accessTokenType", source = "oAuth2AuthorizedClient.accessToken.tokenType.value", qualifiedByName = "mapToAccessTokenType")
  @Mapping(target = "accessTokenValue", source = "oAuth2AuthorizedClient.accessToken.tokenValue")
  @Mapping(target = "accessTokenIssuedAt", source = "oAuth2AuthorizedClient.accessToken.issuedAt")
  @Mapping(target = "accessTokenExpiresAt", source = "oAuth2AuthorizedClient.accessToken.expiresAt")
  @Mapping(target = "accessTokenScopes", source = "oAuth2AuthorizedClient.accessToken.scopes", qualifiedByName = "mapToAccessTokenScopes")
  @Mapping(target = "refreshTokenValue", source = "oAuth2AuthorizedClient.refreshToken.tokenValue")
  @Mapping(target = "refreshTokenIssuedAt", source = "oAuth2AuthorizedClient.refreshToken.issuedAt")
  @Mapping(target = "refreshTokenExpiresAt", source = "oAuth2AuthorizedClient.refreshToken.expiresAt")
  public abstract LinkedOAuth2Account toLinkedOAuth2Account(
      @MappingTarget LinkedOAuth2Account linkedOAuth2Account,
      OAuth2AuthorizedClient oAuth2AuthorizedClient
  );

  @Named("updateLinkedOAuth2Account")
  @Mapping(target = "accessTokenType", source = "oAuth2AuthorizedClient.accessToken.tokenType.value", qualifiedByName = "mapToAccessTokenType")
  @Mapping(target = "accessTokenValue", source = "oAuth2AuthorizedClient.accessToken.tokenValue")
  @Mapping(target = "accessTokenIssuedAt", source = "oAuth2AuthorizedClient.accessToken.issuedAt")
  @Mapping(target = "accessTokenExpiresAt", source = "oAuth2AuthorizedClient.accessToken.expiresAt")
  @Mapping(target = "accessTokenScopes", source = "oAuth2AuthorizedClient.accessToken.scopes", qualifiedByName = "mapToAccessTokenScopes")
  @Mapping(target = "refreshTokenValue", source = "oAuth2AuthorizedClient.refreshToken.tokenValue")
  @Mapping(target = "refreshTokenIssuedAt", source = "oAuth2AuthorizedClient.refreshToken.issuedAt")
  @Mapping(target = "refreshTokenExpiresAt", source = "oAuth2AuthorizedClient.refreshToken.expiresAt")
  public abstract LinkedOAuth2Account updateLinkedOAuth2Account(
      @MappingTarget LinkedOAuth2Account linkedOAuth2Account,
      OAuth2AuthorizedClient oAuth2AuthorizedClient
  );

  @Named("toLinkedOAuth2AccountResponse")
  @Mapping(target = "accessTokenScopes", qualifiedByName = "mapToAccessTokenScopeSet")
  @Mapping(target = "provider", source = "id.clientRegistrationId")
  public abstract LinkedOAuth2AccountResponse toLinkedOAuth2AccountResponse(LinkedOAuth2Account linkedOAuth2Account);

  @Named("toLinkedOAuth2AccountResponseList")
  @IterableMapping(qualifiedByName = "toLinkedOAuth2AccountResponse")
  public abstract List<LinkedOAuth2AccountResponse> toLinkedOAuth2AccountResponseList(Set<LinkedOAuth2Account> linkedOAuth2AccountSet);

}
