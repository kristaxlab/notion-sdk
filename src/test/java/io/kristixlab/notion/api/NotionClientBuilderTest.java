package io.kristixlab.notion.api;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.client.HttpClient;
import java.util.Map;
import org.junit.jupiter.api.*;

/** Unit tests for {@link NotionClient.Builder} and {@link NotionClient#forToken}. */
class NotionClientBuilderTest {

  // ------------------------------------------------------------------
  // Helpers
  // ------------------------------------------------------------------

  private static NotionAuthSettings authWithToken(String token) {
    NotionAuthSettings s = new NotionAuthSettings();
    s.setAccessToken(token);
    return s;
  }

  // ------------------------------------------------------------------
  // Validation
  // ------------------------------------------------------------------

  @Test
  @DisplayName("build() without auth() throws NullPointerException")
  void buildWithoutAuthThrows() {
    NullPointerException ex =
        assertThrows(NullPointerException.class, () -> NotionClient.builder().build());
    assertTrue(ex.getMessage().contains("authSettings"));
  }

  @Test
  @DisplayName("build() with null auth(NotionAuthSettings) throws NullPointerException")
  void buildWithNullAuthSettingsThrows() {
    assertThrows(
        NullPointerException.class,
        () -> NotionClient.builder().auth((NotionAuthSettings) null).build());
  }

  @Test
  @DisplayName("auth(String) with blank token throws IllegalArgumentException")
  void authWithBlankTokenThrows() {
    assertThrows(IllegalArgumentException.class, () -> NotionClient.builder().auth("  ").build());
  }

  @Test
  @DisplayName("forToken() with blank token throws IllegalArgumentException")
  void forTokenWithBlankThrows() {
    assertThrows(IllegalArgumentException.class, () -> NotionClient.forToken(" "));
  }

  // ------------------------------------------------------------------
  // Successful construction
  // ------------------------------------------------------------------

  @Test
  @DisplayName("builder().auth(authSettings).build() returns a non-null NotionClient")
  void builderWithAuthSettingsSucceeds() {
    NotionClient client = NotionClient.builder().auth(authWithToken("secret_test")).build();
    assertNotNull(client);
  }

  @Test
  @DisplayName("builder().auth(String).build() returns a non-null NotionClient")
  void builderWithAuthStringSucceeds() {
    NotionClient client = NotionClient.builder().auth("secret_test").build();
    assertNotNull(client);
  }

  @Test
  @DisplayName("forToken(token) returns a non-null NotionClient")
  void forTokenSucceeds() {
    NotionClient client = NotionClient.forToken("secret_test");
    assertNotNull(client);
  }

  @Test
  @DisplayName("NotionClient exposes all endpoint accessors")
  void allEndpointAccessorsNonNull() {
    NotionClient client = NotionClient.forToken("secret_test");
    assertAll(
        () -> assertNotNull(client.users()),
        () -> assertNotNull(client.pages()),
        () -> assertNotNull(client.blocks()),
        () -> assertNotNull(client.databases()),
        () -> assertNotNull(client.dataSources()),
        () -> assertNotNull(client.comments()),
        () -> assertNotNull(client.fileUploads()),
        () -> assertNotNull(client.search()),
        () -> assertNotNull(client.authorization()));
  }

  // ------------------------------------------------------------------
  // Pipeline is wired — auth header injected on every request
  // ------------------------------------------------------------------

  @Test
  @DisplayName("Built client injects Authorization header")
  void builtClientInjectsAuthHeader() {
    HttpClient.HttpRequest[] captured = new HttpClient.HttpRequest[1];
    HttpClient recorder =
        req -> {
          captured[0] = req;
          return new HttpClient.HttpResponse(200, Map.of(), "{}".getBytes());
        };

    NotionClient client =
        NotionClient.builder().auth(authWithToken("secret_abc123")).rawHttpClient(recorder).build();

    client.users().me();

    assertNotNull(captured[0], "Request should have been sent");
    assertEquals("Bearer secret_abc123", captured[0].headers().get("Authorization"));
  }

  @Test
  @DisplayName("Built client injects custom Notion-Version header")
  void builtClientInjectsVersionHeader() {
    HttpClient.HttpRequest[] captured = new HttpClient.HttpRequest[1];
    HttpClient recorder =
        req -> {
          captured[0] = req;
          return new HttpClient.HttpResponse(200, Map.of(), "{}".getBytes());
        };

    NotionClient client =
        NotionClient.builder()
            .auth(authWithToken("tok"))
            .version("2025-01-01")
            .rawHttpClient(recorder)
            .build();

    client.users().me();

    assertEquals("2025-01-01", captured[0].headers().get("Notion-Version"));
  }

  @Test
  @DisplayName("Built client uses custom base URL")
  void builtClientUsesCustomBaseUrl() {
    HttpClient.HttpRequest[] captured = new HttpClient.HttpRequest[1];
    HttpClient recorder =
        req -> {
          captured[0] = req;
          return new HttpClient.HttpResponse(200, Map.of(), "{}".getBytes());
        };

    NotionClient client =
        NotionClient.builder()
            .auth(authWithToken("tok"))
            .baseUrl("https://mock.notion.test/v1")
            .rawHttpClient(recorder)
            .build();

    client.users().me();

    assertTrue(
        captured[0].url().startsWith("https://mock.notion.test/v1"),
        "Expected custom base URL, got: " + captured[0].url());
  }
}
