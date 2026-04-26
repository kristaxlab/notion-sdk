package io.kristaxlab.notion.auth;

import io.kristaxlab.notion.http.base.client.HttpClient.HttpRequest;
import io.kristaxlab.notion.http.base.interceptor.HttpClientInterceptor;
import java.util.Objects;

/**
 * Adds Notion-specific authentication and API headers to every outgoing request.
 *
 * <p>Adds {@code Notion-Version}, {@code Accept: application/json}, and {@code Authorization}
 * (Bearer for regular calls, Basic for OAuth endpoints). Existing {@code Authorization} headers are
 * never overwritten.
 */
public class NotionAuthInterceptor implements HttpClientInterceptor {

  private final AuthTokenProvider authTokenProvider;

  /**
   * @param authTokenProvider auth token provider
   */
  public NotionAuthInterceptor(AuthTokenProvider authTokenProvider) {
    this.authTokenProvider = Objects.requireNonNull(authTokenProvider, "authTokenProvider");
  }

  @Override
  public HttpRequest beforeSend(HttpRequest request) {
    if (!request.headers().containsKey("Authorization")) {
      return request.toBuilder()
          .header("Authorization", "Bearer " + authTokenProvider.getAuthToken())
          .build();
    }

    return request;
  }
}
