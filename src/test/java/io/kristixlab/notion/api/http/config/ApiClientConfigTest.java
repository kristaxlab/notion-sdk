package io.kristixlab.notion.api.http.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class ApiClientConfigTest {

  @Test
  void defaults_hasNoEntries() {
    ApiClientConfig config = ApiClientConfig.defaults();

    assertEquals(Optional.empty(), config.get(ApiClientConfig.JSON_FAIL_ON_UNKNOWN));
  }

  @Test
  void defaults_getOrDefault_returnsFallback() {
    ApiClientConfig config = ApiClientConfig.defaults();

    assertFalse(config.getOrDefault(ApiClientConfig.JSON_FAIL_ON_UNKNOWN, false));
    assertTrue(config.getOrDefault(ApiClientConfig.JSON_FAIL_ON_UNKNOWN, true));
  }

  @Test
  void jsonFailOnUnknownProperties_convenience_storesValue() {
    ApiClientConfig config = ApiClientConfig.builder().jsonFailOnUnknownProperties(true).build();

    assertEquals(Optional.of(true), config.get(ApiClientConfig.JSON_FAIL_ON_UNKNOWN));
    assertTrue(config.getOrDefault(ApiClientConfig.JSON_FAIL_ON_UNKNOWN, false));
  }

  @Test
  void jsonFailOnUnknownProperties_false_storesValue() {
    ApiClientConfig config = ApiClientConfig.builder().jsonFailOnUnknownProperties(false).build();

    assertEquals(Optional.of(false), config.get(ApiClientConfig.JSON_FAIL_ON_UNKNOWN));
  }

  @Test
  void set_customStringKey_storedAndRetrieved() {
    ConfigKey<String> userAgent = ConfigKey.of("userAgent");
    ApiClientConfig config = ApiClientConfig.builder().set(userAgent, "MyApp/1.0").build();

    assertEquals(Optional.of("MyApp/1.0"), config.get(userAgent));
    assertEquals("MyApp/1.0", config.getOrDefault(userAgent, "default"));
  }

  @Test
  void set_customIntKey_storedAndRetrieved() {
    ConfigKey<Integer> retries = ConfigKey.of("maxRetries");
    ApiClientConfig config = ApiClientConfig.builder().set(retries, 3).build();

    assertEquals(Optional.of(3), config.get(retries));
  }

  @Test
  void set_multipleCustomKeys_independentlyRetrieved() {
    ConfigKey<String> userAgent = ConfigKey.of("userAgent");
    ConfigKey<Integer> retries = ConfigKey.of("maxRetries");

    ApiClientConfig config =
        ApiClientConfig.builder()
            .jsonFailOnUnknownProperties(true)
            .set(userAgent, "MyApp/1.0")
            .set(retries, 5)
            .build();

    assertTrue(config.getOrDefault(ApiClientConfig.JSON_FAIL_ON_UNKNOWN, false));
    assertEquals("MyApp/1.0", config.getOrDefault(userAgent, ""));
    assertEquals(5, config.getOrDefault(retries, 0));
  }

  @Test
  void get_unknownKey_returnsEmpty() {
    ConfigKey<String> absent = ConfigKey.of("absent");
    ApiClientConfig config = ApiClientConfig.defaults();

    assertEquals(Optional.empty(), config.get(absent));
  }

  @Test
  void get_sameNameDifferentInstance_notFound() {
    ConfigKey<String> keyA = ConfigKey.of("name");
    ConfigKey<String> keyB = ConfigKey.of("name");

    ApiClientConfig config = ApiClientConfig.builder().set(keyA, "value").build();

    assertEquals(Optional.of("value"), config.get(keyA));
    assertEquals(Optional.empty(), config.get(keyB));
  }

  @Test
  void set_nullKey_throws() {
    assertThrows(NullPointerException.class, () -> ApiClientConfig.builder().set(null, "v"));
  }

  @Test
  void set_nullValue_throws() {
    ConfigKey<String> key = ConfigKey.of("k");
    assertThrows(NullPointerException.class, () -> ApiClientConfig.builder().set(key, null));
  }

  @Test
  void build_producesImmutableConfig() {
    ConfigKey<String> key = ConfigKey.of("k");
    ApiClientConfig.Builder builder = ApiClientConfig.builder().set(key, "original");
    ApiClientConfig config = builder.build();

    ConfigKey<String> key2 = ConfigKey.of("k2");
    builder.set(key2, "added-after");

    assertEquals(Optional.empty(), config.get(key2));
  }
}

