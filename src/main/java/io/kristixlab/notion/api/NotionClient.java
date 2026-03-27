package io.kristixlab.notion.api;

import io.kristixlab.notion.api.endpoints.*;
import io.kristixlab.notion.api.endpoints.impl.*;
import io.kristixlab.notion.api.http.base.client.*;
import io.kristixlab.notion.api.http.base.config.ApiClientConfig;
import io.kristixlab.notion.api.http.base.interceptor.ExchangeRecordingInterceptor;
import io.kristixlab.notion.api.http.base.interceptor.HttpClientInterceptor;
import io.kristixlab.notion.api.http.base.interceptor.LoggingHttpInterceptor;
import io.kristixlab.notion.api.http.base.json.JacksonSerializer;
import io.kristixlab.notion.api.http.base.json.JsonSerializer;
import io.kristixlab.notion.api.http.error.NotionErrorResponseHandler;
import io.kristixlab.notion.api.http.interceptor.NotionAuthInterceptor;
import io.kristixlab.notion.api.http.interceptor.RateLimitHttpInterceptor;
import io.kristixlab.notion.api.http.interceptor.RateLimiter;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

public class NotionClient {

  private final AuthorizationEndpoint authorization;
  private final BlocksEndpoint blocks;
  private final CommentsEndpoint comments;
  private final DataSourcesEndpoint dataSources;
  private final DatabasesEndpoint databases;
  private final FileUploadsEndpoint fileUploads;
  private final PagesEndpoint pages;
  private final SearchEndpoint search;
  private final UsersEndpoint users;

  private NotionClient(ApiClient apiClient, NotionAuthSettings authSettings) {
    this.authorization = new AuthorizationEndpointImpl(authSettings, apiClient);
    this.blocks = new BlocksEndpointImpl(apiClient);
    this.comments = new CommentsEndpointImpl(apiClient);
    this.dataSources = new DataSourcesEndpointImpl(apiClient);
    this.databases = new DatabasesEndpointImpl(apiClient);
    this.fileUploads = new FileUploadsEndpointImpl(apiClient);
    this.pages = new PagesEndpointImpl(apiClient);
    this.search = new SearchEndpointImpl(apiClient);
    this.users = new UsersEndpointImpl(apiClient);
  }

  // ==================================================================
  // Endpoint accessors
  // ==================================================================

  public AuthorizationEndpoint authorization() {
    return authorization;
  }

  public BlocksEndpoint blocks() {
    return blocks;
  }

  public CommentsEndpoint comments() {
    return comments;
  }

  public DataSourcesEndpoint dataSources() {
    return dataSources;
  }

  public DatabasesEndpoint databases() {
    return databases;
  }

  public FileUploadsEndpoint fileUploads() {
    return fileUploads;
  }

  public PagesEndpoint pages() {
    return pages;
  }

  public SearchEndpoint search() {
    return search;
  }

  public UsersEndpoint users() {
    return users;
  }

  // ==================================================================
  // Static factories
  // ==================================================================

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
    NotionAuthSettings auth = new NotionAuthSettings();
    auth.setAccessToken(token);
    return builder().auth(auth).build();
  }

  /**
   * Returns a {@link Builder} pre-configured with Notion API defaults (version {@code
   * "2026-03-11"}, base URL {@code "https://api.notion.com/v1"}, 30 s OkHttp timeouts).
   *
   * <p>Only {@link Builder#auth(NotionAuthSettings)} is required before calling {@link
   * Builder#build()}.
   */
  public static Builder builder() {
    return new Builder();
  }

  // ==================================================================
  // Builder
  // ==================================================================

  /**
   * Fluent builder for {@link NotionClient}.
   *
   * <p>Wires the Notion-specific {@link HttpClient} pipeline internally and produces a fully
   * configured {@link NotionClient} with all endpoints ready.
   */
  public static final class Builder {

    private static final String DEFAULT_VERSION = "2026-03-11";
    private static final String DEFAULT_BASE_URL = "https://api.notion.com/v1";

    private NotionAuthSettings authSettings;
    private String version = DEFAULT_VERSION;
    private String baseUrl = DEFAULT_BASE_URL;

    /** Raw (un-intercepted) client. {@code null} → default OkHttp3Client with 30 s timeouts. */
    private HttpClient rawHttpClient;

    /** {@code null} → a default {@link RateLimiter} is created. */
    private RateLimiter rateLimiter;

    private JsonSerializer jsonSerializer;

    /**
     * When non-{@code null}, an {@link ExchangeRecordingInterceptor} writes files to this
     * directory.
     */
    private java.nio.file.Path rqRsCatchDir = null;

    private Builder() {}

    /**
     * <b>Required.</b> Sets the auth settings providing the Bearer access token for regular calls
     * and optional OAuth client credentials for token-exchange endpoints.
     */
    public Builder auth(NotionAuthSettings authSettings) {
      this.authSettings = authSettings;
      return this;
    }

    public Builder jsonSerializer(JsonSerializer jsonSerializer) {
      this.jsonSerializer = jsonSerializer;
      return this;
    }

    /**
     * Convenience overload: creates a {@link NotionAuthSettings} with the given access token.
     *
     * @param token the Notion integration token; must not be blank
     */
    public Builder auth(String token) {
      if (token == null || token.isBlank()) {
        throw new IllegalArgumentException("Token must not be null or blank");
      }
      NotionAuthSettings s = new NotionAuthSettings();
      s.setAccessToken(token);
      this.authSettings = s;
      return this;
    }

    /** Sets the {@code Notion-Version} header value. Defaults to {@value DEFAULT_VERSION}. */
    public Builder version(String version) {
      this.version = version;
      return this;
    }

    /**
     * Overrides the base URL. Defaults to {@value DEFAULT_BASE_URL}.
     *
     * <p>Useful for testing against a mock server.
     */
    public Builder baseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }

    /**
     * Provides a custom raw {@link HttpClient} as the innermost transport layer. Interceptors and
     * the error handler are still applied on top.
     *
     * <p>If not set, an {@link OkHttp3Client} with 30-second timeouts is created automatically.
     */
    public Builder rawHttpClient(HttpClient rawHttpClient) {
      this.rawHttpClient = rawHttpClient;
      return this;
    }

    /**
     * Provides a custom {@link RateLimiter}. Defaults to a new {@link RateLimiter} instance (3
     * requests per second).
     */
    public Builder rateLimiter(RateLimiter rateLimiter) {
      this.rateLimiter = rateLimiter;
      return this;
    }

    /**
     * Enables file-based exchange logging via {@link ExchangeRecordingInterceptor}.
     *
     * <p>Each HTTP exchange (request + response) is written as a pretty-printed JSON file into
     * {@code dir}. The directory is created automatically if it does not exist.
     *
     * <p>Pass {@code null} (or omit the call) to disable exchange logging.
     *
     * <p><b>Test pattern:</b>
     *
     * <pre>{@code
     * Path dir = Paths.get("exchanges", testClass, testMethod);
     * NotionClient.builder().auth(token).exchangeLogging(dir).build();
     * }</pre>
     *
     * @param dir target directory; {@code null} disables exchange logging
     */
    public Builder exchangeLogging(Path dir) {
      this.rqRsCatchDir = dir;
      return this;
    }

    /**
     * Builds the {@link NotionClient} with the configured settings.
     *
     * @throws NullPointerException if {@link #auth(NotionAuthSettings)} was not called
     */
    public NotionClient build() {
      Objects.requireNonNull(
          authSettings, "authSettings is required — call auth(NotionAuthSettings) or auth(String)");

      HttpClient raw = rawHttpClient != null ? rawHttpClient : defaultOkHttp3Client();
      RateLimiter limiter = rateLimiter != null ? rateLimiter : new RateLimiter();
      JsonSerializer serializer =
          jsonSerializer != null ? jsonSerializer : JacksonSerializer.withDefaults();
      String apiLabel = "Notion";

      List<HttpClientInterceptor> interceptors = new java.util.ArrayList<>();
      interceptors.add(new RateLimitHttpInterceptor(limiter, apiLabel));
      interceptors.add(new NotionAuthInterceptor(authSettings, version));
      if (rqRsCatchDir != null) {
        interceptors.add(
            new ExchangeRecordingInterceptor(rqRsCatchDir, JacksonSerializer.pretty()));
      }
      interceptors.add(new LoggingHttpInterceptor(apiLabel));

      HttpClient pipeline =
          new ErrorHandlingHttpClient(
              new InterceptingHttpClient(raw, interceptors),
              new NotionErrorResponseHandler(serializer));

      ApiClientConfig cfg = ApiClientConfig.builder().apiBaseUrl(baseUrl).build();

      ApiClientImpl apiClient = new ApiClientImpl(pipeline, cfg, serializer);
      return new NotionClient(apiClient, authSettings);
    }

    /** Creates an {@link OkHttp3Client} backed by an {@link OkHttpClient} with 30 s timeouts. */
    private static HttpClient defaultOkHttp3Client() {
      OkHttpClient ok =
          new OkHttpClient.Builder()
              .connectTimeout(30, TimeUnit.SECONDS)
              .readTimeout(30, TimeUnit.SECONDS)
              .writeTimeout(30, TimeUnit.SECONDS)
              .build();
      return new OkHttp3Client(ok);
    }
  }
}
