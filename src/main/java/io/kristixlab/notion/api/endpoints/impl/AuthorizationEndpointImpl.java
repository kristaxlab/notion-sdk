package io.kristixlab.notion.api.endpoints.impl;

import io.kristixlab.notion.api.NotionAuthSettings;
import io.kristixlab.notion.api.endpoints.AuthorizationEndpoint;
import io.kristixlab.notion.api.http.NotionHttpTransport;
import io.kristixlab.notion.api.http.transport.rq.URLInfo;
import io.kristixlab.notion.api.model.authorization.*;
import io.kristixlab.notion.api.util.NotionAuthUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * API for handling Notion OAuth authorization flow. Provides methods to exchange authorization
 * codes for access tokens, refresh tokens, introspect tokens, and revoke tokens.
 */
public class AuthorizationEndpointImpl implements AuthorizationEndpoint {

  private final NotionHttpTransport transport;
  private final NotionAuthSettings authSettings;

  public AuthorizationEndpointImpl(NotionAuthSettings authSettings, NotionHttpTransport transport) {
    this.authSettings = authSettings;
    this.transport = transport;
  }

  /**
   * Convenience method to exchange authorization code for token. Redirect uri is taken from the
   * Notion client auth settings.
   *
   * @param code The authorization code
   * @return TokenResponse containing the access token and workspace information
   *     <p>
   */
  public TokenResponse createToken(String code) {
    TokenRequest request = TokenRequest.of(code, null);
    return createToken(request);
  }

  /**
   * Exchange an authorization code for an access token.
   *
   * @param code The authorization code received from the OAuth authorization flow
   * @param redirectUri The redirect URI used in the initial authorization request
   * @return TokenResponse containing the access token and workspace information
   */
  public TokenResponse createToken(String code, String redirectUri) {
    TokenRequest request = TokenRequest.of(code, redirectUri);
    return createToken(request);
  }

  /**
   * Exchange an authorization code for an access token with a TokenRequest object.
   *
   * @param request The token request containing code and redirect URI
   * @return TokenResponse containing the access token and workspace information
   */
  public TokenResponse createToken(TokenRequest request) {
    validateRequest(request);
    URLInfo urlInfo = URLInfo.build("/oauth/token");

    TokenResponse response = transport.call("POST", urlInfo, request, TokenResponse.class);

    authSettings.setAccessToken(response.getAccessToken());
    authSettings.setRefreshToken(response.getRefreshToken());
    return response;
  }

  /**
   * Exchange an authorization code for an access token with a TokenRequest object and client
   * credentials.
   *
   * @param request The token request containing code and redirect URI
   * @param clientId The OAuth client ID
   * @param clientSecret The OAuth client secret
   * @return TokenResponse containing the access token and workspace information
   */
  public TokenResponse createToken(TokenRequest request, String clientId, String clientSecret) {
    validateRequest(request);
    URLInfo urlInfo = URLInfo.build("/oauth/token");
    Map<String, String> headers = authHeader(clientId, clientSecret);
    return transport.call("POST", urlInfo, headers, request, TokenResponse.class);
  }

  /**
   * Refresh an access token using a refresh token stored in the client auth settings.
   *
   * @return TokenResponse containing the new access token and workspace information
   */
  public TokenResponse refreshToken() {
    TokenResponse response = refreshToken(authSettings.getRefreshToken());
    authSettings.setAccessToken(response.getAccessToken());
    authSettings.setRefreshToken(response.getRefreshToken());
    return response;
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
    URLInfo urlInfo = URLInfo.build("/oauth/token");
    return transport.call("POST", urlInfo, request, TokenResponse.class);
  }

  /**
   * Refresh an access token using a refresh token with client credentials.
   *
   * @param refreshToken The refresh token received from the initial OAuth token exchange
   * @param clientId The OAuth client ID
   * @param clientSecret The OAuth client secret
   * @return TokenResponse containing the new access token and workspace information
   */
  public TokenResponse refreshToken(String refreshToken, String clientId, String clientSecret) {
    RefreshTokenRequest request = RefreshTokenRequest.create(refreshToken);
    return refreshToken(request, clientId, clientSecret);
  }

  /**
   * Refresh an access token using a refresh token with client credentials.
   *
   * @param request The refresh token received from the initial OAuth token exchange
   * @param clientId The OAuth client ID
   * @param clientSecret The OAuth client secret
   * @return TokenResponse containing the new access token and workspace information
   */
  public TokenResponse refreshToken(
      RefreshTokenRequest request, String clientId, String clientSecret) {
    validateRefreshTokenRequest(request);
    URLInfo urlInfo = URLInfo.build("/oauth/token");
    Map<String, String> headers = authHeader(clientId, clientSecret);
    return transport.call("POST", urlInfo, headers, request, TokenResponse.class);
  }

  /**
   * Introspect the Notion API access token stored in the Notion client auth settings.
   *
   * @return IntrospectTokenResponse containing token information
   */
  public IntrospectTokenResponse introspectAccessToken() {
    IntrospectTokenRequest request = IntrospectTokenRequest.of(authSettings.getAccessToken());
    return introspectToken(request);
  }

  /**
   * Introspect the Notion API refresh token stored in the Notion client auth settings.
   *
   * @return IntrospectTokenResponse containing token information
   */
  public IntrospectTokenResponse introspectRefreshToken() {
    IntrospectTokenRequest request = IntrospectTokenRequest.of(authSettings.getRefreshToken());
    return introspectToken(request);
  }

  /**
   * Introspect a token to get information about its validity and properties.
   *
   * @param token The token to introspect (access token or refresh token)
   * @return IntrospectTokenResponse containing token information TODO check if it will work without
   *     type hint
   */
  public IntrospectTokenResponse introspectToken(String token) {
    IntrospectTokenRequest request = IntrospectTokenRequest.of(token);
    return introspectToken(request);
  }

  /**
   * Introspect a token using an IntrospectTokenRequest object.
   *
   * @param request The introspection request containing token and optional type hint
   * @return IntrospectTokenResponse containing token information
   */
  public IntrospectTokenResponse introspectToken(IntrospectTokenRequest request) {
    validateIntrospectTokenRequest(request);
    URLInfo urlInfo = URLInfo.build("/oauth/introspect");
    return transport.call("POST", urlInfo, request, IntrospectTokenResponse.class);
  }

  /**
   * Introspect a token to get information about its validity and properties with client
   * credentials.
   *
   * @param token The token to introspect (access token or refresh token)
   * @param clientId The OAuth client ID
   * @param clientSecret The OAuth client secret
   * @return IntrospectTokenResponse containing token information
   */
  public IntrospectTokenResponse introspectToken(
      String token, String clientId, String clientSecret) {
    IntrospectTokenRequest request = IntrospectTokenRequest.of(token);
    return introspectToken(request, clientId, clientSecret);
  }

  /**
   * Introspect a token using an IntrospectTokenRequest object with client credentials.
   *
   * @param request The introspection request containing token and optional type hint
   * @param clientId The OAuth client ID
   * @param clientSecret The OAuth client secret
   * @return IntrospectTokenResponse containing token information
   */
  public IntrospectTokenResponse introspectToken(
      IntrospectTokenRequest request, String clientId, String clientSecret) {
    validateIntrospectTokenRequest(request);
    URLInfo urlInfo = URLInfo.build("/oauth/introspect");
    Map<String, String> headers = authHeader(clientId, clientSecret);
    return transport.call("POST", urlInfo, headers, request, IntrospectTokenResponse.class);
  }

  /**
   * Revoke the access token stored in the Notion client auth settings.
   *
   * @return RevokeTokenResponse indicating revocation status
   */
  public RevokeTokenResponse revokeToken() {
    return revokeToken(authSettings.getAccessToken());
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
    URLInfo urlInfo = URLInfo.builder("/oauth/revoke").build();
    return transport.call("POST", urlInfo, request, RevokeTokenResponse.class);
  }

  public RevokeTokenResponse revokeToken(String token, String clientId, String clientSecret) {
    validateToken(token);
    RevokeTokenRequest request = RevokeTokenRequest.create(token);
    URLInfo urlInfo = URLInfo.build("/oauth/revoke");
    Map<String, String> headers = authHeader(clientId, clientSecret);
    return transport.call("POST", urlInfo, headers, request, RevokeTokenResponse.class);
  }

  private Map<String, String> authHeader(String clientId, String clientSecret) {
    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", NotionAuthUtil.basic(clientId, clientSecret));
    return headers;
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
