package io.kristixlab.notion.api;

import io.kristixlab.notion.api.endpoints.BlocksEndpoint;
import io.kristixlab.notion.api.endpoints.FileUploadsEndpoint;
import io.kristixlab.notion.api.endpoints.PagesEndpoint;
import io.kristixlab.notion.api.endpoints.UsersEndpoint;
import io.kristixlab.notion.api.endpoints.impl.BlocksEndpointImpl;
import io.kristixlab.notion.api.endpoints.impl.FileUploadsEndpointImpl;
import io.kristixlab.notion.api.endpoints.impl.PagesEndpointImpl;
import io.kristixlab.notion.api.endpoints.impl.UsersEndpointImpl;
import io.kristixlab.notion.api.http.NotionHttpClient;
import io.kristixlab.notion.api.http.base.client.*;

public class NotionClient {

  private final NotionHttpClient httpClient;

  private UsersEndpoint usersEndpoint;
  private BlocksEndpoint blocksEndpoint;
  private PagesEndpoint pagesEndpoint;
  private FileUploadsEndpoint fileUploadsEndpoint;

  NotionClient(NotionHttpClient httpClient) {
    this.httpClient = httpClient;
    this.usersEndpoint = new UsersEndpointImpl(httpClient);
    this.blocksEndpoint = new BlocksEndpointImpl(httpClient);
    this.pagesEndpoint = new PagesEndpointImpl(httpClient);
    this.fileUploadsEndpoint = new FileUploadsEndpointImpl(httpClient);
  }

  public NotionHttpClient getHttpClient() {
    return httpClient;
  }

  public UsersEndpoint users() {
    return usersEndpoint;
  }

  public BlocksEndpoint blocks() {
    return blocksEndpoint;
  }

  public PagesEndpoint pages() {
    return pagesEndpoint;
  }

  public FileUploadsEndpoint fileUploads() {
    return fileUploadsEndpoint;
  }

  /**
   * Creates a {@link NotionClient} for a private integration token with all Notion defaults.
   *
   * <p>Equivalent to:
   *
   * <pre>{@code
   * NotionAuthSettings auth = new NotionAuthSettings();
   * auth.setAccessToken(token);
   * NotionClient.builder().auth(auth).build();
   * }</pre>
   *
   * @param token the Notion integration token (e.g. {@code "secret_xxx"}); must not be blank
   * @return a fully-wired {@link NotionClient}
   */
  public static NotionClient forToken(String token) {
    if (token == null || token.isBlank()) {
      throw new IllegalArgumentException("Token must not be null or blank");
    }
    return builder().authToken(token).build();
  }

  /**
   * Returns a {@link NotionClientBuilder} pre-configured with Notion API defaults (version {@code
   * "2026-03-11"}, base URL {@code "https://api.notion.com/v1"}, 30 s OkHttp timeouts).
   */
  public static NotionClientBuilder builder() {
    return new NotionClientBuilder();
  }
}
