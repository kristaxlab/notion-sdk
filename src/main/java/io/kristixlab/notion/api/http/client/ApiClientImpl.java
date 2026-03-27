package io.kristixlab.notion.api.http.client;

import io.kristixlab.notion.api.http.config.ApiClientConfig;
import io.kristixlab.notion.api.http.request.ApiPath;
import io.kristixlab.notion.api.json.JsonSerializer;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.Objects;

/**
 * Standard {@link ApiClient} implementation backed by a fully-composed {@link HttpClient} pipeline.
 *
 * <p>For production use, prefer {@link io.kristixlab.notion.api.NotionClient#builder()} which wires
 * the full pipeline automatically.
 */
public class ApiClientImpl implements ApiClient {

  private static final String APPLICATION_JSON = "application/json";
  private final HttpClient httpClient;
  private final String baseUrl;
  private final JsonSerializer serializer;

  public ApiClientImpl(HttpClient httpClient, ApiClientConfig config, JsonSerializer serializer) {
    this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
    this.baseUrl = normalizeBaseUrl(config.get(ApiClientConfig.API_BASE_URL).get());
    this.serializer = Objects.requireNonNull(serializer, "serializer");
  }

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

    String url = apiPath.resolve(baseUrl);
    HttpClient.Body httpBody = serializeBody(body);

    HttpClient.HttpRequest.Builder builder =
        HttpClient.HttpRequest.builder().url(url).method(toHttpMethod(method)).body(httpBody);

    if (body != null && !(body instanceof HttpClient.Body)) {
      builder.header("Content-Type", APPLICATION_JSON);
    }

    if (extraHeaders != null) {
      extraHeaders.forEach(builder::header);
    }

    HttpClient.HttpResponse response = send(builder.build());
    return deserialize(response.bodyAsString(), responseType);
  }

  /** Sends the request through the pipeline. */
  private HttpClient.HttpResponse send(HttpClient.HttpRequest request) {
    try {
      return httpClient.send(request);
    } catch (IOException e) {
      throw new UncheckedIOException("Network error calling " + request.url(), e);
    }
  }

  /** Serializes the body object into an HttpClient.Body if necessary */
  private HttpClient.Body serializeBody(Object body) {
    if (body == null) return null;
    if (body instanceof HttpClient.Body b) return b;
    return new HttpClient.StringBody(serializer.toJson(body), APPLICATION_JSON);
  }

  /** Deserializes the response body string into the target type. */
  private <T> T deserialize(String body, Class<T> responseType) {
    if (responseType == String.class) {
      return responseType.cast(body);
    }
    return serializer.toObject(body, responseType);
  }

  private static HttpClient.HttpMethod toHttpMethod(String method) {
    try {
      return HttpClient.HttpMethod.valueOf(method.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Unsupported HTTP method: " + method, e);
    }
  }

  /** Strips a trailing slash unless the URL is a bare protocol root. */
  private static String normalizeBaseUrl(String baseUrl) {
    if (baseUrl == null || baseUrl.isBlank()) {
      return "";
    }
    if (baseUrl.endsWith("/") && !baseUrl.equals("https://") && !baseUrl.equals("http://")) {
      return baseUrl.substring(0, baseUrl.length() - 1);
    }
    return baseUrl;
  }
}
