package io.kristixlab.notion.api.http.interceptor;

import io.kristixlab.notion.api.NotionAuthSettings;
import io.kristixlab.notion.api.http.client.HttpClient.HttpRequest;
import java.net.URI;
import java.util.Objects;
import java.util.Set;

/**
 * Interceptor that adds Notion-specific authentication and API headers to every outgoing request.
 *
 * <p>Headers added:
 *
 * <ul>
 *   <li>{@code Notion-Version} — the API version string (e.g. {@code "2026-03-11"})
 *   <li>{@code Accept: application/json}
 *   <li>{@code Authorization} — either a Bearer token (for regular API calls) or a Basic credential
 *       (for OAuth token/introspect/revoke endpoints), determined by inspecting the request URL
 * </ul>
 *
 * <p>If the request already contains an {@code Authorization} header (set by the caller), this
 * interceptor will <b>not</b> overwrite it.
 *
 * <p>Usage:
 *
 * <pre>{@code
 * HttpClient client = new InterceptingHttpClient(new OkHttp3Client(), List.of(
 *     new RateLimitHttpInterceptor(rateLimiter, "Notion"),
 *     new NotionAuthInterceptor(authSettings, "2026-03-11"),
 *     new LoggingHttpInterceptor("Notion")
 * ));
 * }</pre>
 *
 * @see HttpClientInterceptor
 */
public class NotionAuthInterceptor implements HttpClientInterceptor {

  /** OAuth endpoints that require Basic authentication (client-id:client-secret). */
  private static final Set<String> OAUTH_PATHS =
      Set.of("/oauth/token", "/oauth/introspect", "/oauth/revoke");

  private final NotionAuthSettings authSettings;
  private final String notionVersion;

  /**
   * @param authSettings the authentication settings containing tokens / client credentials
   * @param notionVersion the Notion API version header value (e.g. {@code "2026-03-11"})
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

  /**
   * Selects the correct {@code Authorization} header value based on the request URL.
   *
   * <p>OAuth endpoints ({@code /oauth/token}, {@code /oauth/introspect}, {@code /oauth/revoke})
   * require HTTP Basic authentication using the integration's client ID and client secret. All
   * other endpoints use the Bearer access token.
   *
   * @param url the full request URL
   * @return the {@code Authorization} header value
   * @throws IllegalStateException if the required credentials are not configured
   */
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

  /**
   * Checks whether the URL targets one of the OAuth endpoints that require Basic authentication.
   *
   * @param url the full request URL (e.g. {@code "https://api.notion.com/v1/oauth/token"})
   * @return {@code true} if the URL path matches an OAuth endpoint
   */
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
