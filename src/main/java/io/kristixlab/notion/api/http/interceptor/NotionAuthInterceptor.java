package io.kristixlab.notion.api.http.interceptor;

import io.kristixlab.notion.api.http.base.client.HttpClient.HttpRequest;
import io.kristixlab.notion.api.http.base.interceptor.HttpClientInterceptor;
import java.util.Objects;

/**
 * Adds Notion-specific authentication and API headers to every outgoing request.
 *
 * <p>Adds {@code Notion-Version}, {@code Accept: application/json}, and {@code Authorization}
 * (Bearer for regular calls, Basic for OAuth endpoints). Existing {@code Authorization} headers are
 * never overwritten.
 */
public class NotionAuthInterceptor implements HttpClientInterceptor {

  private final String authBearerHeader;
  private final String notionVersion;

  /**
   * @param authBearerHeader auth token
   * @param notionVersion Notion API version header value (e.g. {@code "2026-03-11"})
   */
  public NotionAuthInterceptor(String authBearerHeader, String notionVersion) {
    this.authBearerHeader = Objects.requireNonNull(authBearerHeader, "authBearerHeader");
    this.notionVersion = Objects.requireNonNull(notionVersion, "notionVersion");
  }

  @Override
  public HttpRequest beforeSend(HttpRequest request) {
    HttpRequest.Builder builder = request.toBuilder().header("Notion-Version", notionVersion);

    if (!request.headers().containsKey("Authorization")) {
      builder.header("Authorization", authBearerHeader);
    }

    return builder.build();
  }
}
