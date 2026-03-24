package io.kristixlab.notion.api.http;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.NotionAuthSettings;
import io.kristixlab.notion.api.http.client.HttpClient.*;
import io.kristixlab.notion.api.http.interceptor.NotionAuthInterceptor;
import org.junit.jupiter.api.*;

/** Unit tests for {@link NotionAuthInterceptor}. */
class NotionAuthInterceptorTest {

  private static final String VERSION = "2026-03-11";
  private static final String BASE = "https://api.notion.com/v1";

  // ------------------------------------------------------------------
  // Helpers
  // ------------------------------------------------------------------

  private static NotionAuthSettings tokenAuth(String accessToken) {
    NotionAuthSettings s = new NotionAuthSettings();
    s.setAccessToken(accessToken);
    return s;
  }

  private static NotionAuthSettings fullAuth(
      String accessToken, String clientId, String clientSecret) {
    NotionAuthSettings s = new NotionAuthSettings();
    s.setAccessToken(accessToken);
    s.setClientId(clientId);
    s.setClientSecret(clientSecret);
    return s;
  }

  private static HttpRequest get(String url) {
    return HttpRequest.builder().url(url).method(HttpMethod.GET).build();
  }

  private static HttpRequest post(String url) {
    return HttpRequest.builder().url(url).method(HttpMethod.POST).build();
  }

  // ------------------------------------------------------------------
  // Constructor validation
  // ------------------------------------------------------------------

  @Test
  @DisplayName("Rejects null authSettings")
  void nullAuthSettingsThrows() {
    assertThrows(NullPointerException.class, () -> new NotionAuthInterceptor(null, VERSION));
  }

  @Test
  @DisplayName("Rejects null notionVersion")
  void nullVersionThrows() {
    assertThrows(
        NullPointerException.class, () -> new NotionAuthInterceptor(tokenAuth("tok"), null));
  }

  // ------------------------------------------------------------------
  // Standard API calls — Bearer token
  // ------------------------------------------------------------------

  @Test
  @DisplayName("Adds Notion-Version, Accept, and Bearer Authorization for regular endpoints")
  void regularEndpointHeaders() {
    var interceptor = new NotionAuthInterceptor(tokenAuth("secret_abc123"), VERSION);

    HttpRequest result = interceptor.beforeSend(get(BASE + "/pages"));

    assertEquals(VERSION, result.headers().get("Notion-Version"));
    assertEquals("application/json", result.headers().get("Accept"));
    assertEquals("Bearer secret_abc123", result.headers().get("Authorization"));
  }

  @Test
  @DisplayName("Adds Bearer for database query")
  void databaseQueryEndpoint() {
    var interceptor = new NotionAuthInterceptor(tokenAuth("secret_xyz"), VERSION);

    HttpRequest result = interceptor.beforeSend(post(BASE + "/databases/abc-123/query"));

    assertEquals("Bearer secret_xyz", result.headers().get("Authorization"));
  }

  // ------------------------------------------------------------------
  // OAuth endpoints — Basic auth
  // ------------------------------------------------------------------

  @Test
  @DisplayName("/oauth/token uses Basic auth")
  void oauthTokenEndpoint() {
    var interceptor = new NotionAuthInterceptor(fullAuth("tok", "client1", "secret1"), VERSION);

    HttpRequest result = interceptor.beforeSend(post(BASE + "/oauth/token"));

    assertTrue(result.headers().get("Authorization").startsWith("Basic "));
  }

  @Test
  @DisplayName("/oauth/introspect uses Basic auth")
  void oauthIntrospectEndpoint() {
    var interceptor = new NotionAuthInterceptor(fullAuth("tok", "client1", "secret1"), VERSION);

    HttpRequest result = interceptor.beforeSend(post(BASE + "/oauth/introspect"));

    assertTrue(result.headers().get("Authorization").startsWith("Basic "));
  }

  @Test
  @DisplayName("/oauth/revoke uses Basic auth")
  void oauthRevokeEndpoint() {
    var interceptor = new NotionAuthInterceptor(fullAuth("tok", "client1", "secret1"), VERSION);

    HttpRequest result = interceptor.beforeSend(post(BASE + "/oauth/revoke"));

    assertTrue(result.headers().get("Authorization").startsWith("Basic "));
  }

  // ------------------------------------------------------------------
  // Pre-existing Authorization header — not overwritten
  // ------------------------------------------------------------------

  @Test
  @DisplayName("Does not overwrite an existing Authorization header")
  void existingAuthHeaderPreserved() {
    var interceptor = new NotionAuthInterceptor(tokenAuth("secret_abc"), VERSION);

    HttpRequest request =
        HttpRequest.builder()
            .url(BASE + "/pages")
            .method(HttpMethod.GET)
            .header("Authorization", "Bearer custom_token")
            .build();

    HttpRequest result = interceptor.beforeSend(request);

    assertEquals("Bearer custom_token", result.headers().get("Authorization"));
    // Other headers are still added
    assertEquals(VERSION, result.headers().get("Notion-Version"));
    assertEquals("application/json", result.headers().get("Accept"));
  }

  // ------------------------------------------------------------------
  // Missing credentials — fail-fast
  // ------------------------------------------------------------------

  @Test
  @DisplayName("Throws if Bearer token is missing for regular endpoint")
  void missingTokenThrows() {
    NotionAuthSettings noToken = new NotionAuthSettings();
    var interceptor = new NotionAuthInterceptor(noToken, VERSION);

    IllegalStateException ex =
        assertThrows(
            IllegalStateException.class, () -> interceptor.beforeSend(get(BASE + "/pages")));
    assertTrue(ex.getMessage().contains("Auth token"));
  }

  @Test
  @DisplayName("Throws if client credentials are missing for OAuth endpoint")
  void missingClientCredentialsThrows() {
    NotionAuthSettings noOauth = new NotionAuthSettings();
    noOauth.setAccessToken("tok"); // has token, but no client id/secret
    var interceptor = new NotionAuthInterceptor(noOauth, VERSION);

    IllegalStateException ex =
        assertThrows(
            IllegalStateException.class, () -> interceptor.beforeSend(post(BASE + "/oauth/token")));
    assertTrue(ex.getMessage().contains("Client ID"));
  }

  // ------------------------------------------------------------------
  // Immutability — original request is not modified
  // ------------------------------------------------------------------

  @Test
  @DisplayName("Original request is not mutated")
  void originalRequestUnchanged() {
    var interceptor = new NotionAuthInterceptor(tokenAuth("secret_abc"), VERSION);

    HttpRequest original = get(BASE + "/pages");
    HttpRequest modified = interceptor.beforeSend(original);

    assertNotSame(original, modified);
    assertFalse(original.headers().containsKey("Authorization"));
    assertTrue(modified.headers().containsKey("Authorization"));
  }
}
