package io.kristixlab.notion;

import io.kristixlab.notion.api.*;
import io.kristixlab.notion.api.exchange.NotionApiTransport;
import java.util.Base64;

/*
TODO upgrade
data_sources endpoint
database endpoint - create / update / query / retrieve / list databases (deprecated)
search endpoint?
parent
update error message with additional data
test if parent of database can be workspace (root page), UI allows?
 */
public class NotionClient {
  private static final String DEFAULT_VERSION = "2025-09-03"; /*"2022-06-28";*/
  private static final String BASE_URL = "https://api.notion.com/v1/";
  private String token;
  private String version;
  private String tokenAuthHeader;

  private String clientId;
  private String clientToken;
  private String redirectUri;
  private String base64AuthHeader;

  private NotionApiTransport transport;

  private AuthorizationApi authorizationApi;
  private BlocksApi blocksApi;
  private PagesApi pagesApi;
  private DatabasesApi databasesApi;
  private CommentsApi commentsApi;
  private FileUploadsApi fileUploadsApi;
  private SearchApi searchApi;
  private UsersApi usersApi;

  public NotionClient(String token) {
    this(token, DEFAULT_VERSION);
  }

  public NotionClient(String clientId, String clientToken, String redirectUri) {
    if (clientId == null || clientId.isEmpty()) {
      throw new IllegalArgumentException("Client id cannot be null or empty");
    }
    if (clientToken == null || clientToken.isEmpty()) {
      throw new IllegalArgumentException("Client token cannot be null or empty");
    }
    this.base64AuthHeader =
        "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientToken).getBytes());
    this.clientId = clientId;
    this.clientToken = clientToken;
    this.redirectUri = redirectUri;

    transport = new NotionApiTransport(this, "Notion API");
    initApis(transport);
  }

  public NotionClient(String token, String version) {
    if (token == null || token.isEmpty()) {
      throw new IllegalArgumentException("Token cannot be null or empty");
    }
    this.token = token;
    this.version = (version == null || version.isEmpty()) ? DEFAULT_VERSION : version;
    this.tokenAuthHeader = "Bearer " + token;

    transport = new NotionApiTransport(this, "Notion API");
    initApis(transport);
  }

  private void initApis(NotionApiTransport transport) {
    this.authorizationApi = new AuthorizationApi(this, transport);
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

  public String getClientId() {
    return clientId;
  }

  public String getClientToken() {
    return clientToken;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public String getBase64AuthHeader() {
    return base64AuthHeader;
  }

  public String getTokenAuthHeader() {
    return tokenAuthHeader;
  }

  public String getToken() {
    return token;
  }

  public String getVersion() {
    return version;
  }

  public NotionApiTransport getTransport() {
    return transport;
  }

  public String getBaseUrl() {
    return BASE_URL;
  }
}
