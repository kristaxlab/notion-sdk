package io.kristixlab.notion.api.exchange.transport;

import io.kristixlab.notion.api.util.JsonConverter;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ApiTransport {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiTransport.class);
  private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

  private String baseUrl = "https://api.example.com"; // Default base URL, can be overridden
  private final OkHttpClient client;
  private final String apiName;

  public ApiTransport() {
    this.client = createHttpClient();
    if (baseUrl == null) {
      baseUrl = "";
    }
    this.apiName = "API Transport";
  }

  public ApiTransport(String baseUrl, String apiName) {
    this.client = createHttpClient();
    if (baseUrl == null) {
      baseUrl = "";
    } else if (baseUrl.endsWith("/") && !baseUrl.equals("https://") && !baseUrl.equals("http://")) {
      baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
    }
    this.baseUrl = baseUrl;
    this.apiName = apiName == null ? "API Transport" : apiName;
  }

  private OkHttpClient createHttpClient() {
    return new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
  }

  public <RS> RS call(String method, String url, Object body, Class<RS> responseType) {
    return call(method, url, null, null, body, responseType);
  }

  public <RS> RS call(
          String method,
          String url,
          Map<String, String[]> queryParams,
          Map<String, String> pathParams,
          Object body,
          Class<RS> responseType) {

    String logBlueprint = UUID.randomUUID().toString();

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("{} - Calling {}", logBlueprint, getApiName());
      LOGGER.debug("{} - Building request...", logBlueprint);
    }

    Request request = buildRequest(method, url, queryParams, pathParams, body);

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("{} - Request method: {}, url: {}", logBlueprint, method, request.url());
      if (request.body() != null) {
        LOGGER.debug("{} - Request body: \n{}", logBlueprint, JsonConverter.getInstance().toJson(body));
      }
    }

    return executeRequest(request, responseType, logBlueprint);
  }

  /**
   * Special method for multipart/form-data requests (like file uploads).
   */
  public <RS> RS callMultipart(
          String method,
          String url,
          String contentType,
          Map<String, String> pathParams,
          byte[] fileContent,
          String partNumber,
          Class<RS> responseType) {

    String logBlueprint = UUID.randomUUID().toString();
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("{} - Calling {} with multipart data", logBlueprint, getApiName());
      LOGGER.debug("{} - Building multipart request...", logBlueprint);
    }

    Request request =
            buildMultipartRequest(method, url, contentType, pathParams, fileContent, partNumber);

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("{} - Request method: {}, url: {}", logBlueprint, method, request.url());
      LOGGER.debug(
              "{} - Multipart request with file size: {} bytes", logBlueprint,
              fileContent != null ? fileContent.length : 0);
    }

    return executeRequest(request, responseType, logBlueprint);
  }

  private <RS> RS executeRequest(Request request, Class<RS> responseType, String logBlueprint) {
    Call call = client.newCall(request);
    try (Response response = call.execute()) {
      return handleResponse(response, responseType, logBlueprint);
    } catch (IOException e) {
      LOGGER.error("{} - Network error calling {} API", logBlueprint, getApiName(), e);
      throw new ApiTransportException("Network error: " + e.getMessage(), e);
    }
  }

  protected <RS> RS handleResponse(Response response, Class<RS> responseType, String logBlueprint)
          throws IOException, ApiExchangeException {
    final int statusCode = response.code();
    final ResponseBody responseBody = response.body();

    final String responseBodyString = responseBody.string();

    if (response.isSuccessful()) {
      LOGGER.debug("{} - Response from {} with code: {}", logBlueprint, getApiName(), statusCode);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("{} - Response body: \n{}", logBlueprint, responseBodyString);
      }
      return JsonConverter.getInstance().toObject(responseBodyString, responseType);
    } else {
      LOGGER.error("{} - Response from {} with error: {}", logBlueprint, getApiName(), statusCode);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("{} - Error response body: \n{}", logBlueprint, responseBodyString);
      }
      throw new ApiExchangeException(getApiName(), statusCode, responseBodyString);
    }
  }

  private Request buildRequest(
          String method,
          String url,
          Map<String, String[]> queryParams,
          Map<String, String> pathParams,
          Object body) {
    String urlWithQueryParams = buildURL(url, queryParams, pathParams);

    Request.Builder requestBuilder = new Request.Builder().url(urlWithQueryParams);

    if (body != null) {
      String jsonContent = JsonConverter.getInstance().toJson(body);
      RequestBody requestBody = RequestBody.create(jsonContent, JSON_MEDIA_TYPE);
      requestBuilder.method(method, requestBody);
    } else {
      // Handle methods that don't allow body (GET, HEAD) vs those that do
      if ("GET".equalsIgnoreCase(method) || "HEAD".equalsIgnoreCase(method)) {
        requestBuilder.method(method, null);
      } else {
        // For POST, PUT, PATCH without body, use empty body
        RequestBody emptyBody = RequestBody.create("", JSON_MEDIA_TYPE);
        requestBuilder.method(method, emptyBody);
      }
    }

    return afterBuildRequest(url, requestBuilder).build();
  }

  /**
   * Special method to build requests with multipart/form-data content type (e.g., for file
   * uploads).
   */
  private Request buildMultipartRequest(
          String method,
          String url,
          String contentType,
          Map<String, String> pathParams,
          byte[] fileContent,
          String partNumber) {

    String urlWithQueryParams = buildURL(url, null, pathParams);

    Request.Builder requestBuilder = new Request.Builder().url(urlWithQueryParams);

    // Multipart body builder
    MultipartBody.Builder multipartBuilder =
            new MultipartBody.Builder().setType(MultipartBody.FORM);

    // Add file content as a part
    if (fileContent != null) {
      RequestBody fileBody = RequestBody.create(fileContent, MediaType.parse(contentType));
      multipartBuilder.addFormDataPart("file", "upload_" + System.currentTimeMillis(), fileBody);
    }

    // Add additional part for part number if provided
    if (partNumber != null) {
      RequestBody partNumberBody = RequestBody.create(partNumber, JSON_MEDIA_TYPE);
      multipartBuilder.addFormDataPart("partNumber", null, partNumberBody);
    }

    requestBuilder.method(method, multipartBuilder.build());

    return afterBuildRequest(url, requestBuilder).build();
  }

  protected Request.Builder afterBuildRequest(String url, Request.Builder requestBuilder) {
    return requestBuilder;
  }

  private String buildURL(
          String url, Map<String, String[]> queryParams, Map<String, String> pathParams) {
    String processedUrl = null;
    if (url != null && !url.isEmpty()) {
      if (url.startsWith("http://") || url.startsWith("https://")) {
        processedUrl = url;
      } else {
        processedUrl = baseUrl + (url.startsWith("/") ? "" : "/") + url;
      }
    }
    if (pathParams != null && !pathParams.isEmpty()) {
      for (Map.Entry<String, String> entry : pathParams.entrySet()) {
        String value = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
        processedUrl = processedUrl.replace("{" + entry.getKey() + "}", value);
      }
    }

    HttpUrl httpUrl = HttpUrl.parse(processedUrl);
    if (httpUrl == null) {
      throw new IllegalArgumentException("Invalid URL: " + processedUrl);
    }

    HttpUrl.Builder urlBuilder = httpUrl.newBuilder();

    // Add query parameters
    if (queryParams != null && !queryParams.isEmpty()) {
      queryParams.forEach(
              (key, values) -> {
                if (values != null) {
                  for (String value : values) {
                    if (value != null) {
                      urlBuilder.addQueryParameter(key, value);
                    }
                  }
                }
              });
    }

    return urlBuilder.build().toString();
  }

  protected Logger getLogger() {
    return LOGGER;
  }

  protected String getBaseUrl() {
    return baseUrl;
  }

  protected String getApiName() {
    return apiName;
  }

  public void shutdown() {
    if (client != null) {
      client.dispatcher().executorService().shutdown();
      client.connectionPool().evictAll();
    }
  }
}
