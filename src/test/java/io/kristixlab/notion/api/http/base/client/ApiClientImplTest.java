package io.kristixlab.notion.api.http.base.client;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.base.config.ApiClientConfig;
import io.kristixlab.notion.api.http.base.json.JsonSerializer;
import io.kristixlab.notion.api.http.base.request.ApiPath;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApiClientImplTest {

  @Test
  @DisplayName("call() serializes body, sets headers, and deserializes response")
  void call_serializesBody_setsHeaders_andDeserializesResponse() {
    // Arrange
    String baseUrl = "https://api.example.com";
    String resolvedUrl = baseUrl + "/v1/resource";
    String method = "POST";
    String requestBody = "{\"foo\":\"bar\"}";
    String responseBody = "{\"result\":42}";
    Map<String, String> extraHeaders = Map.of("X-Test", "true");
    Object body = new Object();
    class Response {
      int result;
    }

    // Fake HttpClient that records the request and returns a fixed response
    HttpClientMock httpClient = mockHttpClient(responseBody);

    ApiClientConfig config = ApiClientConfig.builder().apiBaseUrl(baseUrl).build();
    ApiPath apiPath = ApiPath.from("/v1/resource");
    ApiClientImpl client =
        new ApiClientImpl(httpClient, config, mockSerializer(requestBody, new Response()));

    // Act
    Response result = client.call(method, apiPath, extraHeaders, body, Response.class);

    // Assert
    assertNotNull(result);
    HttpClient.HttpRequest sent = httpClient.lastRequest;
    assertEquals(resolvedUrl, sent.url());
    assertEquals(HttpClient.HttpMethod.POST, sent.method());
    assertEquals("application/json", sent.headers().get("Content-Type"));
    assertEquals("true", sent.headers().get("X-Test"));
    assertTrue(sent.body() instanceof HttpClient.StringBody);
    assertEquals(requestBody, ((HttpClient.StringBody) sent.body()).content());
  }

  @DisplayName("call() resolves baseUrl correctly: empty, ends with /, no trailing slash")
  void call_resolvesBaseUrl_variants() {
    String requestPath = "/v1/test";
    String baseUrl = "https://api.example.com";
    String baseUrlWithSlash = "https://api.example.com/";

    HttpClientMock httpClient = mockHttpClient("{}");

    ApiClientImpl simpleBaseUrlClient =
        new ApiClientImpl(httpClient, config(baseUrl), mockSerializer());
    simpleBaseUrlClient.call("GET", ApiPath.from(requestPath), String.class);
    assertEquals(baseUrl + requestPath, httpClient.lastRequest.url());

    ApiClientImpl emptyBaseUrlClient = new ApiClientImpl(httpClient, config(""), mockSerializer());
    emptyBaseUrlClient.call("GET", ApiPath.from(requestPath), String.class);
    assertEquals(requestPath, httpClient.lastRequest.url());

    ApiClientImpl baseUrlWithSlashClient =
        new ApiClientImpl(httpClient, config(baseUrlWithSlash), mockSerializer());
    baseUrlWithSlashClient.call("GET", ApiPath.from(requestPath), String.class);
    assertEquals(baseUrl + requestPath, httpClient.lastRequest.url());
  }

  private ApiClientConfig config(String baseUrl) {
    return ApiClientConfig.builder().apiBaseUrl(baseUrl).build();
  }

  private HttpClientMock mockHttpClient(String responseBody) {
    HttpClient.HttpResponse fakeResponse =
        new HttpClient.HttpResponse(200, Map.of(), responseBody.getBytes());
    return new HttpClientMock(fakeResponse);
  }

  private JsonSerializer mockSerializer() {
    return mockSerializer("{}", new Object());
  }

  private JsonSerializer mockSerializer(String requestBody, Object responseBody) {
    return new JsonSerializer() {
      @Override
      public String toJson(Object o) {
        return requestBody;
      }

      @Override
      @SuppressWarnings("unchecked")
      public <T> T toObject(String json, Class<T> type) {
        return (T) responseBody;
      }
    };
  }
}
