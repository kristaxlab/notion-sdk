package io.kristaxlab.notion.http.base.request;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import lombok.Getter;

/**
 * Immutable API path with optional path and query parameters. Created via {@link #from(String)} or
 * the fluent {@link Builder}.
 */
public final class ApiPath {

  @Getter private final String url;

  /** Unmodifiable map of path parameters. */
  @Getter private final Map<String, String> pathParams;

  /** Unmodifiable map of query parameters. */
  @Getter private final Map<String, List<String>> queryParams;

  private ApiPath(
      String url, Map<String, String> pathParams, Map<String, List<String>> queryParams) {
    this.url = url;
    this.pathParams = Map.copyOf(pathParams);
    this.queryParams = deepCopyQueryParams(queryParams);
  }

  /**
   * Creates an empty builder.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Creates a builder preconfigured with the provided URL/path template.
   *
   * @param url relative path or absolute URL
   * @return a new builder with {@code url} set
   */
  public static Builder builder(String url) {
    return new Builder().url(url);
  }

  /**
   * Creates an {@link ApiPath} from the provided URL/path with no params.
   *
   * @param url relative path or absolute URL
   * @return an immutable API path
   */
  public static ApiPath from(String url) {
    return new Builder().url(url).build();
  }

  /**
   * Resolves to a full URL assuming the path is already absolute.
   *
   * @return resolved URL
   */
  public String resolve() {
    return resolve("");
  }

  /**
   * Resolves against the given base URL, substitutes path parameters, and appends query parameters.
   * If this path is already absolute, {@code baseUrl} is ignored.
   *
   * @param baseUrl base URL used when this instance contains a relative path
   * @return resolved URL with encoded path/query parameters
   */
  public String resolve(String baseUrl) {
    String resolved = buildBase(baseUrl, url);
    resolved = applyPathParams(resolved, pathParams);
    return appendQueryParams(resolved, queryParams);
  }

  private static String buildBase(String baseUrl, String path) {
    if (path == null || path.isEmpty()) {
      return baseUrl != null ? baseUrl : "";
    }
    if (path.startsWith("http://") || path.startsWith("https://")) {
      return path;
    }
    String base = baseUrl != null ? baseUrl : "";
    String sep = path.startsWith("/") ? "" : "/";
    return base + sep + path;
  }

  private static String applyPathParams(String url, Map<String, String> pathParams) {
    if (pathParams.isEmpty()) {
      return url;
    }
    for (Map.Entry<String, String> entry : pathParams.entrySet()) {
      String encoded =
          URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8).replace("+", "%20");
      url = url.replace("{" + entry.getKey() + "}", encoded);
    }
    return url;
  }

  private static String appendQueryParams(String url, Map<String, List<String>> queryParams) {
    if (queryParams.isEmpty()) {
      return url;
    }
    StringBuilder sb = new StringBuilder(url);
    boolean hasQuery = url.contains("?");
    for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
      List<String> values = entry.getValue();
      if (values == null) continue;
      String encodedKey =
          URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8).replace("+", "%20");
      for (String value : values) {
        if (value == null) continue;
        sb.append(hasQuery ? '&' : '?');
        sb.append(encodedKey);
        sb.append('=');
        sb.append(URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20"));
        hasQuery = true;
      }
    }
    return sb.toString();
  }

  private static Map<String, List<String>> deepCopyQueryParams(Map<String, List<String>> source) {
    Map<String, List<String>> copy = new LinkedHashMap<>(source.size());
    source.forEach((key, values) -> copy.put(key, List.copyOf(values)));
    return Collections.unmodifiableMap(copy);
  }

  /** Mutable builder used to create immutable {@link ApiPath} instances. */
  public static class Builder {

    private String url;
    private final Map<String, String> pathParams = new LinkedHashMap<>();
    private final Map<String, List<String>> queryParams = new LinkedHashMap<>();

    /**
     * Builds an immutable {@link ApiPath}. The builder can be reused after this call.
     *
     * @return immutable API path snapshot
     */
    public ApiPath build() {
      return new ApiPath(url, pathParams, queryParams);
    }

    /**
     * Sets the relative path or absolute URL.
     *
     * @param url relative path or absolute URL
     * @return this builder
     */
    public Builder url(String url) {
      this.url = url;
      return this;
    }

    /**
     * Adds or replaces a path parameter.
     *
     * @param key path placeholder name without braces
     * @param value path parameter value
     * @return this builder
     */
    public Builder pathParam(String key, String value) {
      pathParams.put(key, value);
      return this;
    }

    /**
     * Appends a single query parameter value.
     *
     * @param key query parameter name
     * @param value query parameter value
     * @return this builder
     */
    public Builder queryParam(String key, String value) {
      return queryParam(key, List.of(value));
    }

    /**
     * Appends multiple query parameter values under the same key.
     *
     * @param key query parameter name
     * @param values query parameter values to append
     * @return this builder
     */
    public Builder queryParam(String key, List<String> values) {
      queryParams.merge(
          key,
          new ArrayList<>(values),
          (existing, incoming) -> {
            existing.addAll(incoming);
            return existing;
          });
      return this;
    }
  }
}
