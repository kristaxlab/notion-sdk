package io.kristixlab.notion.api;

import io.kristixlab.notion.NotionClient;
import io.kristixlab.notion.api.exchange.NotionApiTransport;
import io.kristixlab.notion.api.model.authorization.IntrospectTokenRequest;
import io.kristixlab.notion.api.model.authorization.IntrospectTokenResponse;
import io.kristixlab.notion.api.model.authorization.RefreshTokenRequest;
import io.kristixlab.notion.api.model.authorization.RevokeTokenRequest;
import io.kristixlab.notion.api.model.authorization.RevokeTokenResponse;
import io.kristixlab.notion.api.model.authorization.TokenRequest;
import io.kristixlab.notion.api.model.authorization.TokenResponse;

/**
 * API for handling Notion OAuth authorization flow. Provides methods to exchange authorization
 * codes for access tokens, refresh tokens, introspect tokens, and revoke tokens.
 */
public class AuthorizationApi {

  private final NotionApiTransport transport;
  private final NotionClient client;

  public AuthorizationApi(NotionClient client, NotionApiTransport transport) {
    this.client = client;
    this.transport = transport;
  }

  /**
   * Exchange an authorization code for an access token.
   *
   * @param code The authorization code received from the OAuth authorization flow
   * @param redirectUri The redirect URI used in the initial authorization request
   * @return TokenResponse containing the access token and workspace information
   */
  public TokenResponse exchangeCodeForToken(String code, String redirectUri) {
    validateCode(code);
    if (redirectUri == null) {
      redirectUri = client.getRedirectUri();
    }

    TokenRequest request = TokenRequest.create(code, redirectUri);
    return transport.call("POST", "/oauth/token", null, null, request, TokenResponse.class);
  }

  /**
   * Exchange an authorization code for an access token with a TokenRequest object.
   *
   * @param request The token request containing code and redirect URI
   * @return TokenResponse containing the access token and workspace information
   */
  public TokenResponse exchangeCodeForToken(TokenRequest request) {
    validateRequest(request);
    return transport.call("POST", "/oauth/token", null, null, request, TokenResponse.class);
  }

  /**
   * Convenience method to exchange authorization code for token. Uses the same redirect URI pattern
   * that's commonly used.
   *
   * @param code The authorization code
   * @return TokenResponse containing the access token and workspace information
   */
  public TokenResponse exchangeCodeForToken(String code) {
    return exchangeCodeForToken(code, null);
  }

  /**
   * Refresh an access token using a refresh token.
   *
   * @param refreshToken The refresh token received from the initial OAuth token exchange
   * @return TokenResponse containing the new access token and workspace information
   */
  public TokenResponse refreshToken(String refreshToken) {
    validateRefreshToken(refreshToken);

    RefreshTokenRequest request = RefreshTokenRequest.create(refreshToken);
    return transport.call("POST", "/oauth/token", null, null, request, TokenResponse.class);
  }

  /**
   * Refresh an access token using a RefreshTokenRequest object.
   *
   * @param request The refresh token request containing the refresh token
   * @return TokenResponse containing the new access token and workspace information
   */
  public TokenResponse refreshToken(RefreshTokenRequest request) {
    validateRefreshTokenRequest(request);
    return transport.call("POST", "/oauth/token", null, null, request, TokenResponse.class);
  }

  /**
   * Introspect a token to get information about its validity and properties.
   *
   * @param token The token to introspect (access token or refresh token)
   * @return IntrospectTokenResponse containing token information
   */
  public IntrospectTokenResponse introspectToken(String token) {
    validateToken(token);

    IntrospectTokenRequest request = IntrospectTokenRequest.create(token);
    return transport.call(
            "POST", "/oauth/introspect", null, null, request, IntrospectTokenResponse.class);
  }

  /**
   * Introspect an access token with type hint.
   *
   * @param accessToken The access token to introspect
   * @return IntrospectTokenResponse containing token information
   */
  public IntrospectTokenResponse introspectAccessToken(String accessToken) {
    validateToken(accessToken);

    IntrospectTokenRequest request = IntrospectTokenRequest.forAccessToken(accessToken);
    return transport.call(
            "POST", "/oauth/introspect", null, null, request, IntrospectTokenResponse.class);
  }

  /**
   * Introspect a refresh token with type hint.
   *
   * @param refreshToken The refresh token to introspect
   * @return IntrospectTokenResponse containing token information
   */
  public IntrospectTokenResponse introspectRefreshToken(String refreshToken) {
    validateToken(refreshToken);

    IntrospectTokenRequest request = IntrospectTokenRequest.forRefreshToken(refreshToken);
    return transport.call(
            "POST", "/oauth/introspect", null, null, request, IntrospectTokenResponse.class);
  }

  /**
   * Introspect a token using an IntrospectTokenRequest object.
   *
   * @param request The introspection request containing token and optional type hint
   * @return IntrospectTokenResponse containing token information
   */
  public IntrospectTokenResponse introspectToken(IntrospectTokenRequest request) {
    validateIntrospectTokenRequest(request);
    return transport.call(
            "POST", "/oauth/introspect", null, null, request, IntrospectTokenResponse.class);
  }

  /**
   * Revoke a token (access token or refresh token).
   *
   * @param token The token to revoke (access token or refresh token)
   * @return RevokeTokenResponse indicating revocation status
   */
  public RevokeTokenResponse revokeToken(String token) {
    validateToken(token);

    RevokeTokenRequest request = RevokeTokenRequest.create(token);
    return transport.call("POST", "/oauth/revoke", null, null, request, RevokeTokenResponse.class);
  }

  private void validateRequest(TokenRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Token request cannot be null");
    }
    validateCode(request.getCode());
  }

  private void validateCode(String code) {
    if (code == null || code.trim().isEmpty()) {
      throw new IllegalArgumentException("Authorization code cannot be null or empty");
    }
  }

  private void validateRefreshToken(String refreshToken) {
    if (refreshToken == null || refreshToken.trim().isEmpty()) {
      throw new IllegalArgumentException("Refresh token cannot be null or empty");
    }
  }

  private void validateRefreshTokenRequest(RefreshTokenRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Refresh token request cannot be null");
    }
    validateRefreshToken(request.getRefreshToken());
  }

  private void validateToken(String token) {
    if (token == null || token.trim().isEmpty()) {
      throw new IllegalArgumentException("Token cannot be null or empty");
    }
  }

  private void validateIntrospectTokenRequest(IntrospectTokenRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Introspect token request cannot be null");
    }
    validateToken(request.getToken());
  }

  private void validateRevokeTokenRequest(RevokeTokenRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Revoke token request cannot be null");
    }
    validateToken(request.getToken());
  }
}
