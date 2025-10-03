package io.kristixlab.notion.api.model.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Request object for exchanging an authorization code for an access token.
 */
@Data
public class TokenRequest {

  /**
   * The authorization code received from the OAuth authorization flow.
   */
  @JsonProperty("code")
  private String code;

  /**
   * Must be "authorization_code" for the authorization code grant type.
   */
  @JsonProperty("grant_type")
  private String grantType = "authorization_code";

  /**
   * The redirect URI used in the initial authorization request.
   */
  @JsonProperty("redirect_uri")
  private String redirectUri;

  /**
   * Creates a token request with the authorization code and redirect URI.
   */
  public static TokenRequest of(String code, String redirectUri) {
    TokenRequest request = new TokenRequest();
    request.setCode(code);
    request.setRedirectUri(redirectUri);
    return request;
  }
}
