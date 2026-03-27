package io.kristixlab.notion.api.http.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Unit tests for {@link ExchangeRecordingInterceptor#serviceNameFrom}. */
class ExchangeRecordingInterceptorTest {

  private static final String BASE = "https://api.notion.com/v1";
  private static final String UUID = "abc12345-1234-1234-1234-abcdef012345";

  @Test
  void get_collectionEndpoint() {
    assertEquals("pages_retrieve", name(BASE + "/pages", "GET"));
  }

  @Test
  void get_resourceById() {
    assertEquals("pages_retrieve", name(BASE + "/pages/" + UUID, "GET"));
  }

  @Test
  void get_nestedCollection() {
    assertEquals("blocks_children_retrieve", name(BASE + "/blocks/" + UUID + "/children", "GET"));
  }

  @Test
  void get_users_me() {
    assertEquals("users_me_retrieve", name(BASE + "/users/me", "GET"));
  }

  @Test
  void post_create() {
    assertEquals("pages_create", name(BASE + "/pages", "POST"));
  }

  @Test
  void post_queryWithId() {
    assertEquals("databases_query_create", name(BASE + "/databases/" + UUID + "/query", "POST"));
  }

  @Test
  void patch_update() {
    assertEquals("pages_update", name(BASE + "/pages/" + UUID, "PATCH"));
  }

  @Test
  void delete_resource() {
    assertEquals("blocks_delete", name(BASE + "/blocks/" + UUID, "DELETE"));
  }

  @Test
  void get_compactUuidStripped() {
    assertEquals("pages_retrieve", name(BASE + "/pages/abc123def456abc123def456abc123de", "GET"));
  }

  @Test
  void unknownMethod_noSuffix() {
    assertEquals("pages", name(BASE + "/pages", "PUT"));
  }

  @Test
  void malformedUrl_returnsUnknown() {
    assertEquals("unknown", name("not a url %%", "GET"));
  }

  private static String name(String url, String method) {
    return ExchangeRecordingInterceptor.serviceNameFrom(url, method);
  }
}
