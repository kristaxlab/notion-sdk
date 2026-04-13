package io.kristaxlab.notion.http;

import io.kristaxlab.notion.http.base.client.HttpClient.HttpRequest;
import io.kristaxlab.notion.http.base.interceptor.HttpClientInterceptor;
import java.util.Objects;

/** Adds Notion-Version headers to every outgoing request. */
public class NotionVersionInterceptor implements HttpClientInterceptor {

  private final String notionVersion;

  /**
   * @param notionVersion Notion API version header value (e.g. {@code "2026-03-11"})
   */
  public NotionVersionInterceptor(String notionVersion) {
    this.notionVersion = Objects.requireNonNull(notionVersion, "notionVersion");
  }

  @Override
  public HttpRequest beforeSend(HttpRequest request) {
    return request.toBuilder().header("Notion-Version", notionVersion).build();
  }
}
