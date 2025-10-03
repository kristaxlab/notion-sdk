package io.kristixlab.notion.api.model.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Request object for introspecting a token to get information about its validity and properties.
 */
@Data
public class IntrospectTokenRequest {

  /**
   * The token to introspect (access token or refresh token).
   */
  @JsonProperty("token")
  private String token;

  /**
   * Optional hint about the type of token being introspected. Can be "access_token" or
   * "refresh_token".
   */
  @JsonProperty("token_type_hint")
  private String tokenTypeHint;

  /**
   * Creates an introspection request for an access token.
   */
  public static IntrospectTokenRequest ofAccessToken(String accessToken) {
    IntrospectTokenRequest request = new IntrospectTokenRequest();
    request.setToken(accessToken);
    request.setTokenTypeHint("access_token");
    return request;
  }

  /**
   * Creates an introspection request for a refresh token.
   */
  public static IntrospectTokenRequest ofRefreshToken(String refreshToken) {
    IntrospectTokenRequest request = new IntrospectTokenRequest();
    request.setToken(refreshToken);
    request.setTokenTypeHint("refresh_token");
    return request;
  }

  /**
   * Creates a basic introspection request without token type hint.
   */
  public static IntrospectTokenRequest of(String token) {
    IntrospectTokenRequest request = new IntrospectTokenRequest();
    request.setToken(token);
    return request;
  }
}
