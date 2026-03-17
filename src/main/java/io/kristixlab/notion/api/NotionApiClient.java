package io.kristixlab.notion.api;

/*
TODO upgrade
update error message with additional data
test if parent of database can be workspace (root page), UI allows?

Visit https://notion.so/profile/integrations to create and manage public and private Notion integrations
 */

import io.kristixlab.notion.api.endpoints.*;
import io.kristixlab.notion.api.endpoints.impl.*;
import io.kristixlab.notion.api.http.NotionHttpTransport;
import io.kristixlab.notion.api.http.transport.HttpTransport;
import io.kristixlab.notion.api.http.transport.log.ExchangeLogger;
import lombok.Getter;

public class NotionApiClient {

  private static final String VERSION = "2026-03-11";
  private static final String BASE_URL = "https://api.notion.com/v1/";

  @Getter private final NotionAuthSettings authSettings = new NotionAuthSettings();

  /** Transport layer for making API calls */
  private HttpTransport transport;

  /** Api classes */
  private AuthorizationEndpointImpl authorizationApi;

  private BlocksEndpointImpl blocksEndpointImpl;
  private PagesEndpointImpl pagesApi;
  private DatabasesEndpointImpl databasesApi;
  private DataSourcesEndpointImpl dataSourcesApi;
  private CommentsEndpointImpl commentsApi;
  private FileUploadsEndpointImpl fileUploadsApi;
  private SearchEndpointImpl searchApi;
  private UsersEndpointImpl usersApi;

  /**
   * Default constructor. Initializes the client using environment variables.
   *
   * <ul>
   *   <li>NOTION_AUTH_TOKEN - for private integration token
   *   <li>NOTION_CLIENT_ID - for public integration client id
   *   <li>NOTION_CLIENT_TOKEN - for public integration client token
   *   <li>NOTION_REDIRECT_URI - optional, for public integration redirect uri
   * </ul>
   *
   * If NOTION_AUTH_TOKEN is set, it takes precedence over client id and token. If neither is set,
   * an IllegalArgumentException is thrown.
   */
  public NotionApiClient() {}

  /**
   * Constructor using a direct access token.
   *
   * @param token The access token for the Notion API. Must not be null or empty. This token is used
   *     for simple token-based authentication with the Notion API. When this field is set, it takes
   *     precedence over OAuth credentials.
   * @throws IllegalArgumentException if token is null or empty
   */
  public NotionApiClient(String token) {
    initWithAuthToken(token);
    transport = new NotionHttpTransport(this.getAuthSettings());
    initApis(transport);
  }

  private void initWithAuthToken(String token) {
    if (token == null || token.isEmpty()) {
      throw new IllegalArgumentException("Token cannot be null or empty");
    }
    authSettings.setAccessToken(token);
  }

  private void initAsPublicIntegration(
      String clientId,
      String clientToken,
      String redirectUri,
      String accessToken,
      String refreshToken) {
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
  }

  private void initApis(HttpTransport transport) {
    this.authorizationApi = new AuthorizationEndpointImpl(this.getAuthSettings(), transport);
    this.blocksEndpointImpl = new BlocksEndpointImpl(transport);
    this.pagesApi = new PagesEndpointImpl(transport);
    this.databasesApi = new DatabasesEndpointImpl(transport);
    this.dataSourcesApi = new DataSourcesEndpointImpl(transport);
    this.commentsApi = new CommentsEndpointImpl(transport);
    this.fileUploadsApi = new FileUploadsEndpointImpl(transport);
    this.searchApi = new SearchEndpointImpl(transport);
    this.usersApi = new UsersEndpointImpl(transport);
  }

  public AuthorizationEndpoint authorization() {
    return authorizationApi;
  }

  public BlocksEndpoint blocks() {
    return blocksEndpointImpl;
  }

  public PagesEndpoint pages() {
    return pagesApi;
  }

  public DatabasesEndpoint databases() {
    return databasesApi;
  }

  public DataSourcesEndpoint dataSources() {
    return dataSourcesApi;
  }

  public CommentsEndpoint comments() {
    return commentsApi;
  }

  public FileUploadsEndpoint fileUploads() {
    return fileUploadsApi;
  }

  public SearchEndpoint search() {
    return searchApi;
  }

  public UsersEndpoint users() {
    return usersApi;
  }

  public String getVersion() {
    return VERSION;
  }

  public String getBaseUrl() {
    return BASE_URL;
  }

  private HttpTransport getTransport() {
    return transport;
  }

  /**
   * Returns a new {@link Builder} for creating a {@link NotionApiClient} with custom settings.
   *
   * <p>Usage examples:
   *
   * <pre>{@code
   * // Private integration
   * NotionApiClient client = NotionApiClient.builder()
   *     .authToken("secret_xxx")
   *     .create();
   *
   * // Public integration with custom HTTP transport
   * NotionApiClient client = NotionApiClient.builder()
   *     .clientId("id")
   *     .clientToken("token")
   *     .redirectUri("https://myapp.com/callback")
   *     .httpClient(myTransport)
   *     .create();
   * }</pre>
   *
   * @return a new {@link Builder} instance
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder for {@link NotionApiClient}.
   *
   * <p>Supports both private token and public integration (OAuth) authentication, with optional
   * injection of a custom {@link HttpTransport} for transport-level control (e.g. mocking in tests,
   * custom interceptors). If no transport is provided, a default {@link NotionHttpTransport} is
   * used.
   *
   * <p>Call {@link #build()} as the terminal method to produce the fully initialized client.
   */
  public static final class Builder {

    private String authToken;
    private String clientId;
    private String clientToken;
    private String redirectUri;
    private String accessToken;
    private String refreshToken;
    private HttpTransport httpClient;

    private ExchangeLogger customExchangeLogger;

    private Builder() {}

    /**
     * Sets the private integration access token.
     *
     * @param authToken the Notion integration token; must not be null or empty when {@link
     *     #build()} is called without OAuth credentials
     */
    public Builder authToken(String authToken) {
      this.authToken = authToken;
      return this;
    }

    /**
     * Sets the OAuth client ID for a public integration.
     *
     * @param clientId the OAuth client ID; must not be null or empty
     */
    public Builder clientId(String clientId) {
      this.clientId = clientId;
      return this;
    }

    /**
     * Sets the OAuth client token (secret) for a public integration.
     *
     * @param clientToken the OAuth client secret; must not be null or empty
     */
    public Builder clientToken(String clientToken) {
      this.clientToken = clientToken;
      return this;
    }

    /**
     * Sets the redirect URI for a public integration.
     *
     * @param redirectUri the redirect URI; may be null
     */
    public Builder redirectUri(String redirectUri) {
      this.redirectUri = redirectUri;
      return this;
    }

    /**
     * Sets a pre-obtained OAuth access token.
     *
     * @param accessToken the access token; may be null
     */
    public Builder accessToken(String accessToken) {
      this.accessToken = accessToken;
      return this;
    }

    /**
     * Sets a pre-obtained OAuth refresh token.
     *
     * @param refreshToken the refresh token; may be null
     */
    public Builder refreshToken(String refreshToken) {
      this.refreshToken = refreshToken;
      return this;
    }

    /**
     * Sets a custom {@link HttpTransport} to use as the underlying HTTP transport.
     *
     * <p>Use this to inject a mock transport in unit tests or a custom implementation with specific
     * logging, retry, or proxy behavior. If not set, a default {@link NotionHttpTransport} is
     * created automatically.
     *
     * @param httpClient the transport instance; if null, the default transport is used
     */
    public Builder httpClient(HttpTransport httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    /**
     * Sets a custom {@link ExchangeLogger} for logging API exchanges.
     *
     * <p>This logger will be used by the default {@link NotionHttpTransport} if no custom transport
     * is provided. If a custom transport is set, it is the responsibility of that transport to use
     * the provided logger.
     *
     * @param exchangeLogger the logger instance; may be null
     */
    public Builder exchangeLogger(ExchangeLogger exchangeLogger) {
      this.customExchangeLogger = exchangeLogger;
      return this;
    }

    /**
     * Creates a fully initialized {@link NotionApiClient} based on the current configuration.
     *
     * <p>Either {@code authToken} or both {@code clientId} and {@code clientToken} must be set. If
     * no {@code httpClient} is configured, a default {@link NotionHttpTransport} is created.
     *
     * @return a configured {@link NotionApiClient}
     * @throws IllegalArgumentException if neither auth token nor OAuth credentials are provided
     */
    public NotionApiClient build() {
      NotionApiClient client = new NotionApiClient();
      if (authToken != null && !authToken.isEmpty()) {
        client.initWithAuthToken(authToken);
      } else if (clientId != null && clientToken != null) {
        client.initAsPublicIntegration(
            clientId, clientToken, redirectUri, accessToken, refreshToken);
      } else {
        throw new IllegalArgumentException(
            "Either authToken or clientId/clientToken must be provided");
      }
      client.transport = resolveTransport(client.getAuthSettings());
      client.initApis(client.transport);
      return client;
    }

    private HttpTransport resolveTransport(NotionAuthSettings authSettings) {
      // TODO should I force custom httpclient to include Notion specific AUth settings?
      if (httpClient != null) {
        return httpClient;
      }
      return new NotionHttpTransport(authSettings, customExchangeLogger);
    }
  }
}
