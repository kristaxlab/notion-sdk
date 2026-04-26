package io.kristaxlab.notion;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.auth.NotionAuthInterceptor;
import io.kristaxlab.notion.http.NotionHttpClient;
import io.kristaxlab.notion.http.NotionVersionInterceptor;
import io.kristaxlab.notion.http.base.client.HttpClient;
import io.kristaxlab.notion.http.base.interceptor.HttpClientInterceptor;
import io.kristaxlab.notion.http.base.json.JsonSerializer;
import io.kristaxlab.notion.http.base.request.ApiPath;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NotionClientBuilderTest {

  @Test
  @DisplayName("build() creates NotionClient with correct interceptors and config")
  void build_createsClientWithCorrectInterceptorsAndConfig() {
    String token = "test-token";
    String version = "2026-03-11";
    String baseUrl = "https://api.notion.com/v1";
    NotionClient client =
        new NotionClientBuilder().authToken(token).version(version).baseUrl(baseUrl).build();
    assertNotNull(client);
    List<HttpClientInterceptor> interceptors = extractInterceptors(client);
    assertTrue(
        interceptors.stream().anyMatch(NotionVersionInterceptor.class::isInstance),
        "NotionVersionInterceptor missing");
    assertTrue(
        interceptors.stream().anyMatch(NotionAuthInterceptor.class::isInstance),
        "NotionAuthInterceptor missing");
  }

  @Test
  @DisplayName("build() throws exception if authToken is missing")
  void build_throwsIfAuthTokenMissing() {
    NotionClientBuilder builder = new NotionClientBuilder();
    Exception ex = assertThrows(NullPointerException.class, builder::build);
    assertTrue(ex.getMessage().toLowerCase().contains("auth token"));
  }

  @Test
  @DisplayName("exchangeLogging enables ExchangeRecordingInterceptor")
  void exchangeLogging_enablesExchangeRecordingInterceptor() {
    NotionClient client =
        new NotionClientBuilder()
            .authToken("t")
            .exchangeLogging(Paths.get("/tmp/exchange-test"))
            .build();

    List<HttpClientInterceptor> interceptors = extractInterceptors(client);
    assertTrue(
        interceptors.stream()
            .anyMatch(i -> i.getClass().getSimpleName().contains("ExchangeRecordingInterceptor")),
        "ExchangeRecordingInterceptor missing");
  }

  @Test
  @DisplayName("build() uses custom rawHttpClient if provided")
  void build_usesCustomRawHttpClient() {
    var customHttpClient =
        new HttpClient() {
          @Override
          public HttpClient.HttpResponse send(HttpClient.HttpRequest request) {
            throw new RuntimeException("customRawHttpClient used");
          }
        };
    NotionClientBuilder builder =
        new NotionClientBuilder().authToken("token").rawHttpClient(customHttpClient);
    NotionClient client = builder.build();
    assertNotNull(client);
    // Try to call and expect our custom client to throw
    var httpClient = (NotionHttpClient) client.getHttpClient();
    Exception ex =
        assertThrows(
            RuntimeException.class,
            () -> httpClient.call("GET", ApiPath.from("/foo"), String.class));
    assertTrue(ex.getMessage().contains("customRawHttpClient used"));
  }

  @Test
  @DisplayName("build() uses custom JsonSerializer if provided")
  void build_usesCustomJsonSerializer() {
    var customSerializer =
        new JsonSerializer() {
          @Override
          public String toJson(Object o) {
            return "CUSTOM_JSON";
          }

          @Override
          public <T> T toObject(String json, Class<T> type) {
            return null;
          }
        };
    NotionClientBuilder builder =
        new NotionClientBuilder().authToken("token").jsonSerializer(customSerializer);
    NotionClient client = builder.build();
    assertNotNull(client);
    // Reflection to get serializer
    try {
      var httpClientField = NotionClient.class.getDeclaredField("httpClient");
      httpClientField.setAccessible(true);
      Object notionHttpClientImpl = httpClientField.get(client);
      var serializerField =
          notionHttpClientImpl.getClass().getSuperclass().getDeclaredField("serializer");
      serializerField.setAccessible(true);
      Object serializer = serializerField.get(notionHttpClientImpl);
      assertSame(customSerializer, serializer);
    } catch (Exception e) {
      fail("Reflection failed: " + e.getClass().getName() + " - " + e.getMessage());
    }
  }

  @Test
  @DisplayName("build() throws IllegalArgumentException if token is null or blank")
  void build_throwsIfTokenNullOrBlank() {
    NotionClientBuilder builderNull = new NotionClientBuilder();
    Exception exNull =
        assertThrows(IllegalArgumentException.class, () -> builderNull.authToken(null));
    assertTrue(exNull.getMessage().toLowerCase().contains("token"));
    NotionClientBuilder builderBlank = new NotionClientBuilder();
    Exception exBlank =
        assertThrows(IllegalArgumentException.class, () -> builderBlank.authToken("   "));
    assertTrue(exBlank.getMessage().toLowerCase().contains("token"));
  }

  /** Uses reflection to extract the list of interceptors from a NotionClient's pipeline. */
  private static List<HttpClientInterceptor> extractInterceptors(NotionClient client) {
    List<HttpClientInterceptor> interceptors = null;
    try {
      // Step 1: NotionClient -> httpClient (NotionHttpClientImpl)
      Field httpClientField = NotionClient.class.getDeclaredField("httpClient");
      httpClientField.setAccessible(true);
      Object notionHttpClientImpl = httpClientField.get(client);

      // Step 2: NotionHttpClientImpl (extends ApiClientImpl) -> httpClient
      // (ErrorHandlingHttpClient)
      Field apiClientImplHttpClientField =
          notionHttpClientImpl.getClass().getSuperclass().getDeclaredField("httpClient");
      apiClientImplHttpClientField.setAccessible(true);
      Object errorHandlingHttpClient = apiClientImplHttpClientField.get(notionHttpClientImpl);

      // Step 3: ErrorHandlingHttpClient -> delegate (InterceptingHttpClient)
      Field delegateField = errorHandlingHttpClient.getClass().getDeclaredField("delegate");
      delegateField.setAccessible(true);
      Object interceptingHttpClient = delegateField.get(errorHandlingHttpClient);

      // Step 4: InterceptingHttpClient -> interceptors
      Field interceptorsField = interceptingHttpClient.getClass().getDeclaredField("interceptors");
      interceptorsField.setAccessible(true);

      interceptors = (List<HttpClientInterceptor>) interceptorsField.get(interceptingHttpClient);

    } catch (Exception e) {
      fail("Reflection failed: " + e.getClass().getName() + " - " + e.getMessage());
    }
    return interceptors;
  }
}
