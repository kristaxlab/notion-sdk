package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.authorization.*;

/**
 * Interface defining operations for Notion Authentication/Authorization.
 *
 * @see <a href="https://developers.notion.com/reference/authentication">Notion Authentication
 *     Endpoint</a>
 */
public interface AuthorizationEndpoint {

  TokenResponse createToken(String code);

  TokenResponse createToken(String code, String redirectUri);

  TokenResponse createToken(TokenRequest request);

  TokenResponse createToken(TokenRequest request, String clientId, String clientSecret);

  IntrospectTokenResponse introspectAccessToken();

  IntrospectTokenResponse introspectRefreshToken();

  IntrospectTokenResponse introspectToken(String token);

  IntrospectTokenResponse introspectToken(IntrospectTokenRequest request);

  IntrospectTokenResponse introspectToken(String token, String clientId, String clientSecret);

  IntrospectTokenResponse introspectToken(
      IntrospectTokenRequest request, String clientId, String clientSecret);

  RevokeTokenResponse revokeToken();

  RevokeTokenResponse revokeToken(String token);

  RevokeTokenResponse revokeToken(String token, String clientId, String clientSecret);

  TokenResponse refreshToken();

  TokenResponse refreshToken(String refreshToken);

  TokenResponse refreshToken(RefreshTokenRequest request);

  TokenResponse refreshToken(String refreshToken, String clientId, String clientSecret);

  TokenResponse refreshToken(RefreshTokenRequest request, String clientId, String clientSecret);
}
