package io.kristixlab.notion.api.model.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Request object for revoking a token (access token or refresh token).
 */
@Data
public class RevokeTokenRequest {

  /**
   * The token to revoke (access token or refresh token).
   */
  @JsonProperty("token")
  private String token;

  /**
   * Creates a basic revoke request without token type hint.
   */
  public static RevokeTokenRequest create(String token) {
    RevokeTokenRequest request = new RevokeTokenRequest();
    request.setToken(token);
    return request;
  }
}
