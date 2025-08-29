package io.kristixlab.notion.api.model.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/** Request object for refreshing an access token using a refresh token. */
@Data
public class RefreshTokenRequest {

  /** The refresh token received from the initial OAuth token exchange. */
  @JsonProperty("refresh_token")
  private String refreshToken;

  /** Must be "refresh_token" for the refresh token grant type. */
  @JsonProperty("grant_type")
  private String grantType = "refresh_token";

  /** Creates a refresh token request with the refresh token. */
  public static RefreshTokenRequest create(String refreshToken) {
    RefreshTokenRequest request = new RefreshTokenRequest();
    request.setRefreshToken(refreshToken);
    return request;
  }
}
