package io.kristixlab.notion.api;

import io.kristixlab.notion.api.model.authorization.*;

/**
 * API for handling Notion OAuth authorization flow. Provides methods to exchange authorization
 * codes for access tokens, refresh tokens, introspect tokens, and revoke tokens.
 */
public class AuthorizationApi {

  private final NotionApiTransport transport;
  private final NotionApiClient client;

  public AuthorizationApi(NotionApiClient client, NotionApiTransport transport) {
    this.client = client;
    this.transport = transport;
  }

  /**
   * Convenience method to exchange authorization code for token.
   * Redirect uri is taken from the Notion client auth settings.
   *
   * @param code The authorization code
   * @return TokenResponse containing the access token and workspace information
   * <p>
   * TODO will it work without redirect url?
   */
  public TokenResponse exchangeCodeForToken(String code) {
    return exchangeCodeForToken(TokenRequest.of(code, client.getAuthSettings().getRedirectUri()));
  }

  /**
   * Exchange an authorization code for an access token.
   *
   * @param code        The authorization code received from the OAuth authorization flow
   * @param redirectUri The redirect URI used in the initial authorization request
   * @return TokenResponse containing the access token and workspace information
   */
  public TokenResponse exchangeCodeForToken(String code, String redirectUri) {
    return exchangeCodeForToken(TokenRequest.of(code, redirectUri));
  }

  /**
   * Exchange an authorization code for an access token with a TokenRequest object.
   *
   * @param request The token request containing code and redirect URI
   * @return TokenResponse containing the access token and workspace information
   */
  public TokenResponse exchangeCodeForToken(TokenRequest request) {
    validateRequest(request);
    TokenResponse response = transport.call("POST", "/oauth/token", request, TokenResponse.class);
    // update client auth settings with new tokens
    client.getAuthSettings().setAccessToken(response.getAccessToken());
    client.getAuthSettings().setRefreshToken(response.getRefreshToken());
    return response;
  }

  /**
   * Refresh an access token using a refresh token stored in the client auth settings.
   *
   * @return TokenResponse containing the new access token and workspace information
   */
  public TokenResponse refreshToken() {
    return refreshToken(client.getAuthSettings().getRefreshToken());
  }

  /**
   * Refresh an access token using a refresh token.
   *
   * @param refreshToken The refresh token received from the initial OAuth token exchange
   * @return TokenResponse containing the new access token and workspace information
   */
  public TokenResponse refreshToken(String refreshToken) {
    RefreshTokenRequest request = RefreshTokenRequest.create(refreshToken);
    return refreshToken(request);
  }

  /**
   * Refresh an access token using a RefreshTokenRequest object.
   *
   * @param request The refresh token request containing the refresh token
   * @return TokenResponse containing the new access token and workspace information
   */
  public TokenResponse refreshToken(RefreshTokenRequest request) {
    validateRefreshTokenRequest(request);
    TokenResponse response = transport.call("POST", "/oauth/token", request, TokenResponse.class);
    // update client auth settings with new tokens
    client.getAuthSettings().setAccessToken(response.getAccessToken());
    client.getAuthSettings().setRefreshToken(response.getRefreshToken());
    return response;
  }

  /**
   * Introspect the Notion API access token stored in the Notion client auth settings.
   *
   * @return IntrospectTokenResponse containing token information
   */
  public IntrospectTokenResponse introspectAccessToken() {
    return introspectAccessToken(client.getAuthSettings().getAccessToken());
  }

  /**
   * Introspect the Notion API refresh token stored in the Notion client auth settings.
   *
   * @return IntrospectTokenResponse containing token information
   */
  public IntrospectTokenResponse introspectRefreshToken() {
    return introspectRefreshToken(client.getAuthSettings().getRefreshToken());
  }

  /**
   * Introspect a token to get information about its validity and properties.
   *
   * @param token The token to introspect (access token or refresh token)
   * @return IntrospectTokenResponse containing token information
   * TODO check if it will work without type hint
   */
  public IntrospectTokenResponse introspectToken(String token) {
    validateToken(token);

    IntrospectTokenRequest request = IntrospectTokenRequest.of(token);
    return transport.call("POST", "/oauth/introspect", request, IntrospectTokenResponse.class);
  }

  /**
   * Introspect an access token with type hint.
   *
   * @param accessToken The access token to introspect
   * @return IntrospectTokenResponse containing token information
   */
  public IntrospectTokenResponse introspectAccessToken(String accessToken) {
    validateToken(accessToken);

    IntrospectTokenRequest request = IntrospectTokenRequest.ofAccessToken(accessToken);
    return transport.call("POST", "/oauth/introspect", request, IntrospectTokenResponse.class);
  }

  /**
   * Introspect a refresh token with type hint.
   *
   * @param refreshToken The refresh token to introspect
   * @return IntrospectTokenResponse containing token information
   */
  public IntrospectTokenResponse introspectRefreshToken(String refreshToken) {
    validateToken(refreshToken);

    IntrospectTokenRequest request = IntrospectTokenRequest.ofRefreshToken(refreshToken);
    return transport.call("POST", "/oauth/introspect", request, IntrospectTokenResponse.class);
  }

  /**
   * Introspect a token using an IntrospectTokenRequest object.
   *
   * @param request The introspection request containing token and optional type hint
   * @return IntrospectTokenResponse containing token information
   */
  public IntrospectTokenResponse introspectToken(IntrospectTokenRequest request) {
    validateIntrospectTokenRequest(request);
    return transport.call("POST", "/oauth/introspect", request, IntrospectTokenResponse.class);
  }


  /**
   * Revoke the access token stored in the Notion client auth settings.
   *
   * @return RevokeTokenResponse indicating revocation status
   */
  public RevokeTokenResponse revokeToken() {
    return revokeToken(client.getAuthSettings().getAccessToken());
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
    return transport.call("POST", "/oauth/revoke", request, RevokeTokenResponse.class);
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
