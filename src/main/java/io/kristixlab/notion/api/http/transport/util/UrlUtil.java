package io.kristixlab.notion.api.http.transport.util;

import io.kristixlab.notion.api.http.transport.rq.URLInfo;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import okhttp3.*;

/** Utility class for building HTTP requests with OkHttp3 library */
public class UrlUtil {

  // Constants
  private static final String HTTP_PREFIX = "http://";
  private static final String HTTPS_PREFIX = "https://";

  private UrlUtil() {
    // Utility class - prevent instantiation
  }

  /**
   * Builds URL string with query and path params applied.
   *
   * @param baseUrl will be added as prefix to urlInfo if urlInfo url does not start with {@code
   *     http://} or {@code https://}
   * @param urlInfo object containing info about url + query + path params
   * @return URL string enriched by provided query and path param values
   */
  public static String toUrlString(String baseUrl, URLInfo urlInfo) {
    String processedUrl = buildBaseUrl(baseUrl, urlInfo.getUrl());
    processedUrl = applyPathParameters(processedUrl, urlInfo.getPathParams());

    HttpUrl httpUrl = parseUrl(processedUrl);
    HttpUrl.Builder urlBuilder = httpUrl.newBuilder();

    addQueryParameters(urlBuilder, urlInfo.getQueryParams());

    return urlBuilder.build().toString();
  }

  /** Builds the base URL by combining baseUrl with the provided url if needed. */
  private static String buildBaseUrl(String baseUrl, String url) {
    if (url == null || url.isEmpty()) {
      return baseUrl;
    }

    if (isAbsoluteUrl(url)) {
      return url;
    }

    String separator = url.startsWith("/") ? "" : "/";
    return baseUrl + separator + url;
  }

  /** Checks if the URL is absolute (starts with {@code http://} or {@code https://}). */
  private static boolean isAbsoluteUrl(String url) {
    return url.startsWith(HTTP_PREFIX) || url.startsWith(HTTPS_PREFIX);
  }

  /** Applies path parameters to the URL by replacing {param} placeholders. */
  private static String applyPathParameters(String url, Map<String, String> pathParams) {
    if (pathParams == null || pathParams.isEmpty()) {
      return url;
    }

    for (Map.Entry<String, String> entry : pathParams.entrySet()) {
      String encodedValue = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
      url = url.replace("{" + entry.getKey() + "}", encodedValue);
    }
    return url;
  }

  /** Parses the URL string and validates it. */
  private static HttpUrl parseUrl(String url) {
    HttpUrl httpUrl = HttpUrl.parse(url);
    if (httpUrl == null) {
      throw new IllegalArgumentException("Invalid URL: " + url);
    }
    return httpUrl;
  }

  /** Adds query parameters to the URL builder. */
  private static void addQueryParameters(
      HttpUrl.Builder urlBuilder, Map<String, List<String>> queryParams) {
    if (queryParams == null || queryParams.isEmpty()) {
      return;
    }

    queryParams.forEach((key, values) -> addQueryParameterValues(urlBuilder, key, values));
  }

  /** Adds all non-null values for a single query parameter key. */
  private static void addQueryParameterValues(
      HttpUrl.Builder urlBuilder, String key, List<String> values) {
    if (values == null) {
      return;
    }

    values.stream()
        .filter(java.util.Objects::nonNull)
        .forEach(value -> urlBuilder.addQueryParameter(key, value));
  }
}
