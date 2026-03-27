package io.kristixlab.notion.api.http.interceptor;

import io.kristixlab.notion.api.NotionAuthSettings;
import io.kristixlab.notion.api.http.client.HttpClient.HttpRequest;
import java.net.URI;
import java.util.Objects;
import java.util.Set;

/**
 * Adds Notion-specific authentication and API headers to every outgoing request.
 *
 * <p>Adds {@code Notion-Version}, {@code Accept: application/json}, and {@code Authorization}
 * (Bearer for regular calls, Basic for OAuth endpoints). Existing {@code Authorization} headers are
 * never overwritten.
 */
public class NotionAuthInterceptor implements HttpClientInterceptor {

  /** OAuth endpoints that require Basic authentication (client-id:client-secret). */
  private static final Set<String> OAUTH_PATHS =
      Set.of("/oauth/token", "/oauth/introspect", "/oauth/revoke");

  private final NotionAuthSettings authSettings;
  private final String notionVersion;

  /**
   * @param authSettings tokens and/or client credentials
   * @param notionVersion Notion API version header value (e.g. {@code "2026-03-11"})
   */
  public NotionAuthInterceptor(NotionAuthSettings authSettings, String notionVersion) {
    this.authSettings = Objects.requireNonNull(authSettings, "authSettings");
    this.notionVersion = Objects.requireNonNull(notionVersion, "notionVersion");
  }

  @Override
  public HttpRequest beforeSend(HttpRequest request) {
    HttpRequest.Builder builder =
        request.toBuilder()
            .header("Notion-Version", notionVersion)
            .header("Accept", "application/json");

    if (!request.headers().containsKey("Authorization")) {
      builder.header("Authorization", resolveAuthHeader(request.url()));
    }

    return builder.build();
  }

  /** Selects Bearer (regular) or Basic (OAuth) auth based on the request URL path. */
  private String resolveAuthHeader(String url) {
    if (isOAuthEndpoint(url)) {
      String basicHeader = authSettings.getOauthBasicHeader();
      if (basicHeader == null) {
        throw new IllegalStateException(
            "Client ID and Client Secret must be set for OAuth token exchange");
      }
      return basicHeader;
    }

    String tokenHeader = authSettings.getTokenAuthHeader();
    if (tokenHeader == null) {
      throw new IllegalStateException("Auth token for Notion API is missing");
    }
    return tokenHeader;
  }

  /** Returns {@code true} if the URL path matches an OAuth endpoint requiring Basic auth. */
  private static boolean isOAuthEndpoint(String url) {
    String path;
    try {
      path = URI.create(url).getPath();
    } catch (Exception e) {
      return false;
    }
    return OAUTH_PATHS.contains(path);
  }
}
