package io.kristixlab.notion.api;

import lombok.Data;
import lombok.Setter;

import java.util.Base64;

@Data
public class NotionAuthSettings {

  /**
   * Access token for Notion API. Can be a token from a public integration obtained via exchange toekn call
   * or a token from a private integration.
   */
  private String accessToken;

  /**
   * Refresh token for Notion API. Only applicable for public integrations.
   */
  private String refreshToken;

  /**
   * This field stores the "Bearer accessToken" string that is used as an Http Header for Notion API authentication
   */
  @Setter(lombok.AccessLevel.NONE)
  private String tokenAuthHeader;


  /**
   * Client id for Notion public integration
   */
  private String clientId;

  /**
   * Client token for Notion public integration
   */
  private String clientSecret;

  /**
   * Redirect url for Notion public integration
   */
  private String redirectUri;

  /**
   * this field stores a base 64 encoded string of "Basic clientId:clientToken" that is used as an Http Header for
   * OAuth2 authorization code exchange
   */
  @Setter(lombok.AccessLevel.NONE)
  private String publicIntegrationBase64AuthHeader;

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
    this.tokenAuthHeader = null;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
    this.publicIntegrationBase64AuthHeader = null;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
    this.publicIntegrationBase64AuthHeader = null;
  }

  public String getTokenAuthHeader() {
    if (tokenAuthHeader == null && accessToken != null) {
      tokenAuthHeader = "Bearer " + accessToken;
    }
    return tokenAuthHeader;
  }

  public String getPublicIntegrationBase64AuthHeader() {
    if (publicIntegrationBase64AuthHeader == null && clientId != null && clientSecret != null) {
      String toEncode = clientId + ":" + clientSecret;
      publicIntegrationBase64AuthHeader = "Basic " + Base64.getEncoder().encodeToString(toEncode.getBytes());
    }
    return publicIntegrationBase64AuthHeader;
  }

}
