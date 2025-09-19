package io.kristixlab.notion.api.exchange;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration class for Notion API authentication settings.
 *
 * <p>This class provides two authentication modes:
 *
 * <ul>
 *   <li><strong>Token-based authentication</strong> - Using a direct access token for simple API
 *       access
 *   <li><strong>OAuth authentication</strong> - Using client credentials for OAuth flow
 *       authentication
 * </ul>
 *
 * Check https://notion.so/profile/integrations to find settings for your Notion integrations.
 *
 * @author KristaxLab
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
public class NotionAuthSettings {

  /**
   * The access token for direct API authentication.
   *
   * <p>This token is used for simple token-based authentication with the Notion API. When this
   * field is set, it takes precedence over OAuth credentials.
   */
  private String authToken;

  /**
   * The client ID for OAuth authentication.
   *
   * <p>This is part of the OAuth credentials provided by Notion when registering an application for
   * OAuth-based authentication.
   */
  private String clientId;

  /**
   * The client secret for OAuth authentication.
   *
   * <p>This is the secret key provided by Notion for OAuth authentication. This should be kept
   * secure and never exposed in client-side code.
   */
  private String clientSecret;

  /**
   * The redirect URI for OAuth authentication.
   *
   * <p>This is the URI where users will be redirected after authorizing your application. Must
   * match the URI registered with Notion.
   */
  private String redirectUri;

  /**
   * Creates a new NotionAuthSettings instance for token-based authentication.
   *
   * <p>Use this method to initialize client  you have a direct access token and want to authenticate using simple
   * token-based authentication.
   *
   * @param accessToken the access token for API authentication
   * @return a NotionAuthSettings instance configured for token-based authentication
   * @throws IllegalArgumentException if accessToken is null or empty
   */
  public static NotionAuthSettings ofAuthToken(String accessToken) {
    NotionAuthSettings settings = new NotionAuthSettings();
    settings.setAuthToken(accessToken);
    return settings;
  }

  /**
   * Creates a new NotionAuthSettings instance for token-based authentication.
   *
   * <p>Use this constructor when you have a direct access token and want to authenticate using
   * simple token-based authentication.
   *
   * @param authToken the access token for API authentication
   * @throws IllegalArgumentException if accessToken is null or empty
   */
  public NotionAuthSettings(String authToken) {
    this.authToken = authToken;
  }

  /**
   * Creates a new NotionAuthSettings instance for OAuth authentication.
   *
   * <p>Use this constructor when you want to implement OAuth flow authentication and have
   * registered your application with Notion to obtain OAuth credentials.
   *
   * @param clientId the client ID provided by Notion
   * @param clientSecret the client secret provided by Notion
   * @param redirectUri the redirect URI registered with your Notion application
   * @throws IllegalArgumentException if any parameter is null or empty
   */
  public NotionAuthSettings(String clientId, String clientSecret, String redirectUri) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.redirectUri = redirectUri;
  }
}
