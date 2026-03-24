package io.kristixlab.notion.api.http.client;

import io.kristixlab.notion.api.http.config.ApiClientConfig;
import io.kristixlab.notion.api.http.request.ApiPath;
import io.kristixlab.notion.api.http.request.ApiPathUtil;
import io.kristixlab.notion.api.http.request.HttpBodyFactory;
import io.kristixlab.notion.api.json.JsonConverter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.Objects;

/**
 * Standard implementation of {@link ApiClient} that sits on top of a fully-composed {@link
 * HttpClient} pipeline.
 *
 * <p>Responsibilities:
 *
 * <ol>
 *   <li>URL building — uses {@link ApiPathUtil} to merge base URL + {@link ApiPath} path/query
 *       params
 *   <li>Body serialization — delegates to {@link HttpBodyFactory}
 *   <li>Request dispatch — delegates to the injected {@link HttpClient}
 *   <li>Response deserialization — converts the raw response body to a typed object via {@link
 *       JsonConverter}
 * </ol>
 *
 * <p>All cross-cutting concerns (authentication, rate-limiting, logging, error mapping) must be
 * handled by the {@link HttpClient} pipeline passed in at construction time. This class contains no
 * Notion-specific logic.
 *
 * <p>Construct directly for tests or custom pipelines:
 *
 * <pre>{@code
 * HttpClient pipeline = new ErrorHandlingHttpClient(
 *     new InterceptingHttpClient(new OkHttp3Client(), interceptors),
 *     errorHandler);
 *
 * ApiClient client = new ApiClientImpl(pipeline, "https://api.notion.com/v1");
 * }</pre>
 *
 * <p>For production use, prefer {@link io.kristixlab.notion.api.NotionClient#builder()} which wires
 * the full Notion pipeline automatically.
 *
 * @see ApiClient
 * @see io.kristixlab.notion.api.NotionClient
 */
public class ApiClientImpl implements ApiClient {

  private final HttpClient httpClient;
  private final String baseUrl;
  private final ApiClientConfig config;

  /**
   * Constructs an {@code ApiClientImpl} with default transport config ({@code
   * jsonFailOnUnknownProperties=false}).
   *
   * @param httpClient the fully-composed HTTP pipeline to delegate to
   * @param baseUrl the base URL prepended to all relative paths (e.g. {@code
   *     "https://api.notion.com/v1"})
   */
  public ApiClientImpl(HttpClient httpClient, String baseUrl) {
    this(httpClient, baseUrl, null);
  }

  /**
   * @param httpClient the fully-composed HTTP pipeline to delegate to
   * @param baseUrl the base URL prepended to all relative paths
   * @param config transport configuration controlling JSON strictness; {@code null} uses sensible
   *     defaults
   */
  public ApiClientImpl(HttpClient httpClient, String baseUrl, ApiClientConfig config) {
    this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
    this.baseUrl = normalizeBaseUrl(Objects.requireNonNull(baseUrl, "baseUrl"));
    this.config = config != null ? config : defaultConfig();
  }

  // ==================================================================
  // ApiClient implementation
  // ==================================================================

  @Override
  public <T> T call(String method, ApiPath apiPath, Class<T> responseType) {
    return call(method, apiPath, null, null, responseType);
  }

  @Override
  public <T> T call(String method, ApiPath apiPath, Object body, Class<T> responseType) {
    return call(method, apiPath, null, body, responseType);
  }

  @Override
  public <T> T call(
      String method,
      ApiPath apiPath,
      Map<String, String> extraHeaders,
      Object body,
      Class<T> responseType) {

    String url = ApiPathUtil.toUrlString(baseUrl, apiPath);
    HttpClient.Body requestBody = HttpBodyFactory.create(body);

    HttpClient.HttpRequest.Builder builder =
        HttpClient.HttpRequest.builder().url(url).method(toHttpMethod(method)).body(requestBody);

    if (HttpBodyFactory.requiresJsonContentType(body)) {
      builder.header("Content-Type", "application/json");
    }

    if (extraHeaders != null) {
      extraHeaders.forEach(builder::header);
    }

    HttpClient.HttpResponse response = send(builder.build());
    return deserialize(response.bodyAsString(), responseType);
  }

  // ==================================================================
  // Internals
  // ==================================================================

  private HttpClient.HttpResponse send(HttpClient.HttpRequest request) {
    try {
      return httpClient.send(request);
    } catch (IOException e) {
      throw new UncheckedIOException("Network error calling " + request.url(), e);
    }
  }

  private <T> T deserialize(String body, Class<T> responseType) {
    if (responseType == String.class) {
      return responseType.cast(body);
    }
    return JsonConverter.getInstance()
        .toObject(body, responseType, config.isJsonFailOnUnknownProperties());
  }

  private static HttpClient.HttpMethod toHttpMethod(String method) {
    try {
      return HttpClient.HttpMethod.valueOf(method.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Unsupported HTTP method: " + method, e);
    }
  }

  /** Strips a trailing slash (except for bare protocol strings like {@code https://}). */
  private static String normalizeBaseUrl(String baseUrl) {
    if (baseUrl.endsWith("/") && !baseUrl.equals("https://") && !baseUrl.equals("http://")) {
      return baseUrl.substring(0, baseUrl.length() - 1);
    }
    return baseUrl;
  }

  private static ApiClientConfig defaultConfig() {
    return ApiClientConfig.builder().jsonFailOnUnknownProperties(false).build();
  }
}
