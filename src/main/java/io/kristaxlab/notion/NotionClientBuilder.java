package io.kristaxlab.notion;

import io.kristaxlab.notion.auth.FixedTokenProvider;
import io.kristaxlab.notion.auth.NotionAuthInterceptor;
import io.kristaxlab.notion.http.NotionHttpClient;
import io.kristaxlab.notion.http.NotionHttpClientImpl;
import io.kristaxlab.notion.http.NotionVersionInterceptor;
import io.kristaxlab.notion.http.base.client.*;
import io.kristaxlab.notion.http.base.client.ErrorHandlingHttpClient;
import io.kristaxlab.notion.http.base.client.HttpClient;
import io.kristaxlab.notion.http.base.client.InterceptingHttpClient;
import io.kristaxlab.notion.http.base.client.OkHttp3Client;
import io.kristaxlab.notion.http.base.config.ApiClientConfig;
import io.kristaxlab.notion.http.base.interceptor.ExchangeRecordingInterceptor;
import io.kristaxlab.notion.http.base.interceptor.HttpClientInterceptor;
import io.kristaxlab.notion.http.base.interceptor.LoggingHttpInterceptor;
import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import io.kristaxlab.notion.http.base.json.JsonSerializer;
import io.kristaxlab.notion.http.error.NotionErrorResponseHandler;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

/**
 * Configures auth, API version, base URL, optional custom HTTP transport, JSON serializer, and
 * exchange logging before {@link #build()}.
 */
public class NotionClientBuilder {

  private static final String DEFAULT_VERSION = "2026-03-11";
  private static final String DEFAULT_BASE_URL = "https://api.notion.com/v1";

  private String authToken;
  private String version = DEFAULT_VERSION;
  private String baseUrl = DEFAULT_BASE_URL;

  private HttpClient rawHttpClient;

  private JsonSerializer jsonSerializer;

  /**
   * When non-{@code null}, an {@link ExchangeRecordingInterceptor} writes files with requests to
   * and responses from Notion API to this directory.
   */
  private Path exchangeRecordsPath = null;

  public NotionClientBuilder() {}

  public NotionClientBuilder jsonSerializer(JsonSerializer jsonSerializer) {
    this.jsonSerializer = jsonSerializer;
    return this;
  }

  public NotionClientBuilder authToken(String token) {
    if (token == null || token.isBlank()) {
      throw new IllegalArgumentException("Token must not be null or blank");
    }
    this.authToken = token;
    return this;
  }

  /** Sets the {@code Notion-Version} header value. Defaults to {@value DEFAULT_VERSION}. */
  public NotionClientBuilder version(String version) {
    this.version = version;
    return this;
  }

  /**
   * Overrides the base URL. Defaults to {@value DEFAULT_BASE_URL}.
   *
   * <p>Useful for testing against a mock server.
   */
  public NotionClientBuilder baseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
    return this;
  }

  /**
   * Provides a custom raw {@link HttpClient} as the innermost transport layer. Interceptors and the
   * error handler are still applied on top.
   *
   * <p>If not set, an {@link OkHttp3Client} with 30-second timeouts is created automatically.
   */
  public NotionClientBuilder rawHttpClient(HttpClient rawHttpClient) {
    this.rawHttpClient = rawHttpClient;
    return this;
  }

  /**
   * Enables file-based exchange logging via {@link ExchangeRecordingInterceptor}.
   *
   * <p>Each HTTP exchange (request + response) is written as a pretty-printed JSON file into {@code
   * dir}. The directory is created automatically if it does not exist.
   *
   * <p>Pass {@code null} (or omit the call) to disable exchange logging.
   *
   * <p><b>Test pattern:</b>
   *
   * <pre>{@code
   * Path dir = Paths.get("exchanges", testClass, testMethod);
   * NotionClient.builder().authToken(token).exchangeLogging(dir).build();
   * }</pre>
   *
   * @param dir target directory; {@code null} disables exchange logging
   */
  public NotionClientBuilder exchangeLogging(Path dir) {
    this.exchangeRecordsPath = dir;
    return this;
  }

  public NotionClient build() {
    Objects.requireNonNull(authToken, "auth token is required");

    HttpClient raw = rawHttpClient != null ? rawHttpClient : defaultOkHttp3Client();
    JsonSerializer serializer =
        jsonSerializer != null ? jsonSerializer : JacksonSerializer.withDefaults();

    List<HttpClientInterceptor> interceptors = buildInterceptors();

    HttpClient pipeline =
        new ErrorHandlingHttpClient(
            new InterceptingHttpClient(raw, interceptors),
            new NotionErrorResponseHandler(serializer));

    ApiClientConfig cfg = ApiClientConfig.builder().apiBaseUrl(baseUrl).build();

    NotionHttpClient httpClient = new NotionHttpClientImpl(pipeline, cfg, serializer);
    return new NotionClient(httpClient);
  }

  private List<HttpClientInterceptor> buildInterceptors() {
    List<HttpClientInterceptor> interceptors = new java.util.ArrayList<>();
    interceptors.add(new NotionVersionInterceptor(version));
    interceptors.add(new NotionAuthInterceptor(new FixedTokenProvider(authToken)));
    if (exchangeRecordsPath != null) {
      interceptors.add(
          new ExchangeRecordingInterceptor(exchangeRecordsPath, JacksonSerializer.pretty()));
    }
    interceptors.add(new LoggingHttpInterceptor("Notion"));
    return interceptors;
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
