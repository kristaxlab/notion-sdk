package io.kristixlab.notion.api;


/*
TODO upgrade
update error message with additional data
test if parent of database can be workspace (root page), UI allows?

Visit https://notion.so/profile/integrations to create and manage public and private Notion integrations
 */

import lombok.Getter;

public class NotionApiClient {

  private static final String VERSION = "2025-09-03";
  private static final String BASE_URL = "https://api.notion.com/v1/";

  @Getter
  private final NotionAuthSettings authSettings = new NotionAuthSettings();

  /**
   * Transport layer for making API calls
   */
  private NotionApiTransport transport;

  /**
   * Api classes
   */
  private AuthorizationApi authorizationApi;
  private BlocksApi blocksApi;
  private PagesApi pagesApi;
  private DatabasesApi databasesApi;
  private CommentsApi commentsApi;
  private FileUploadsApi fileUploadsApi;
  private SearchApi searchApi;
  private UsersApi usersApi;

  /**
   * Default constructor. Initializes the client using environment variables.
   * <ul>
   *   <li>NOTION_AUTH_TOKEN - for private integration token
   *   <li>NOTION_CLIENT_ID - for public integration client id
   *   <li>NOTION_CLIENT_TOKEN - for public integration client token
   *   <li>NOTION_REDIRECT_URI - optional, for public integration redirect uri
   * </ul>
   * If NOTION_AUTH_TOKEN is set, it takes precedence over client id and token.
   * If neither is set, an IllegalArgumentException is thrown.
   */
  public NotionApiClient() {
  }

  /**
   * Constructor using a direct access token.
   *
   * @param token The access token for the Notion API.
   *              Must not be null or empty.
   *              This token is used for simple token-based authentication with the Notion API.
   *              When this field is set, it takes precedence over OAuth credentials.
   * @throws IllegalArgumentException if token is null or empty
   */
  public NotionApiClient(String token) {
    initWithAuthToken(token);
  }

  /**
   * Constructor using public integration credentials.
   *
   * @param clientId    The client ID for the Notion public integration.
   *                    Must not be null or empty.
   * @param clientToken The client token for the Notion public integration.
   *                    Must not be null or empty.
   * @param redirectUri The redirect URI for the Notion public integration.
   *                    Can be null or empty if not applicable.
   * @throws IllegalArgumentException if clientId or clientToken is null or empty
   */
  public NotionApiClient(String clientId, String clientToken, String redirectUri) {
    initAsPublicIntegration(clientId, clientToken, redirectUri, null, null);
  }

  /**
   * Constructor using public integration credentials.
   *
   * @param clientId     The client ID for the Notion public integration.
   *                     Must not be null or empty.
   * @param clientToken  The client token for the Notion public integration.
   *                     Must not be null or empty.
   * @param redirectUri  The redirect URI for the Notion public integration.
   *                     Can be null or empty if not applicable.
   * @param accessToken  The access token for the Notion API.
   * @param refreshToken The refresh token for the Notion API.
   * @throws IllegalArgumentException if clientId or clientToken is null or empty
   */
  public NotionApiClient(String clientId, String clientToken, String redirectUri, String accessToken, String refreshToken) {
    initAsPublicIntegration(clientId, clientToken, redirectUri, accessToken, refreshToken);
  }

  private void initWithAuthToken(String token) {
    if (token == null || token.isEmpty()) {
      throw new IllegalArgumentException("Token cannot be null or empty");
    }
    authSettings.setAccessToken(token);
    transport = new NotionApiTransport(this.getAuthSettings());
    initApis(transport);
  }

  private void initAsPublicIntegration(String clientId, String clientToken, String redirectUri, String accessToken, String refreshToken) {
    if (clientId == null || clientId.isEmpty()) {
      throw new IllegalArgumentException("Client id cannot be null or empty");
    }
    if (clientToken == null || clientToken.isEmpty()) {
      throw new IllegalArgumentException("Client token cannot be null or empty");
    }
    authSettings.setClientId(clientId);
    authSettings.setClientSecret(clientToken);
    authSettings.setRedirectUri(redirectUri);
    authSettings.setAccessToken(accessToken);
    authSettings.setRefreshToken(refreshToken);
    transport = new NotionApiTransport(this.getAuthSettings());
    initApis(transport);
  }

  private void initApis(NotionApiTransport transport) {
    this.authorizationApi = new AuthorizationApi(this.getAuthSettings(), transport);
    this.blocksApi = new BlocksApi(transport);
    this.pagesApi = new PagesApi(transport);
    this.databasesApi = new DatabasesApi(transport);
    this.commentsApi = new CommentsApi(transport);
    this.fileUploadsApi = new FileUploadsApi(transport);
    this.searchApi = new SearchApi(transport);
    this.usersApi = new UsersApi(transport);
  }

  public AuthorizationApi authorization() {
    return authorizationApi;
  }

  public BlocksApi blocks() {
    return blocksApi;
  }

  public PagesApi pages() {
    return pagesApi;
  }

  public DatabasesApi databases() {
    return databasesApi;
  }

  public CommentsApi comments() {
    return commentsApi;
  }

  public FileUploadsApi fileUploads() {
    return fileUploadsApi;
  }

  public SearchApi search() {
    return searchApi;
  }

  public UsersApi users() {
    return usersApi;
  }

  public String getVersion() {
    return VERSION;
  }

  public String getBaseUrl() {
    return BASE_URL;
  }

  private NotionApiTransport getTransport() {
    return transport;
  }

  public void shutdown() {
    try {
      transport.shutdown();
    } catch (Exception e) {
      // calm shutdown
    }
  }

}
