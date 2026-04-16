package io.kristaxlab.notion;

import io.kristaxlab.notion.endpoints.BlocksEndpoint;
import io.kristaxlab.notion.endpoints.FileUploadsEndpoint;
import io.kristaxlab.notion.endpoints.PagesEndpoint;
import io.kristaxlab.notion.endpoints.UsersEndpoint;
import io.kristaxlab.notion.endpoints.impl.BlocksEndpointImpl;
import io.kristaxlab.notion.endpoints.impl.FileUploadsEndpointImpl;
import io.kristaxlab.notion.endpoints.impl.PagesEndpointImpl;
import io.kristaxlab.notion.endpoints.impl.UsersEndpointImpl;
import io.kristaxlab.notion.http.NotionHttpClient;
import io.kristaxlab.notion.http.base.client.*;

/**
 * Entry point for the Notion REST API: {@link #users()}, {@link #blocks()}, {@link #pages()},
 * {@link #fileUploads()}. Use {@link #builder()} or {@link #forToken(String)} to construct an
 * instance.
 */
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

  /** Low-level HTTP client used by this instance (same pipeline as the endpoint accessors). */
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
   * Creates a client with a private integration token and library defaults (API version, base URL,
   * HTTP client).
   *
   * <p>Equivalent to {@code NotionClient.builder().authToken(token).build()}.
   *
   * @param token the Notion integration token (e.g. {@code "ntn_xxx"}); must not be blank
   * @return a fully configured {@link NotionClient}
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
