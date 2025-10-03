package io.kristixlab.notion.api.http.transport;

import io.kristixlab.notion.api.http.ApiRequestUtil;
import io.kristixlab.notion.api.http.ApiResponse;
import io.kristixlab.notion.api.http.FileRequest;
import io.kristixlab.notion.api.json.JsonConverter;
import lombok.Getter;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ApiTransport implements Transport {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiTransport.class);
  private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

  @Getter
  private String baseUrl = "https://api.example.com"; // Default base URL, can be overridden
  private final OkHttpClient httpClient;
  @Getter
  private final String apiName;

  public ApiTransport() {
    this.httpClient = createHttpClient();
    if (baseUrl == null) {
      baseUrl = "";
    }
    this.apiName = "API Transport";
  }

  public ApiTransport(String baseUrl, String apiName) {
    this.httpClient = createHttpClient();
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

  @Override
  public <T> T call(String method, URLInfo urlInfo, Class<T> responseType) {
    return call(method, urlInfo, null, responseType);
  }

  @Override
  public <T> T call(String method, URLInfo urlInfo, Object body, Class<T> responseType) {
    return call(method, urlInfo, null, body, responseType);
  }

  public <T> ApiResponse<T> execute(String method, URLInfo urlInfo, Map<String, String> headerParams, Object body, Class<T> responseType) {
    String logBlueprint = UUID.randomUUID().toString();

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("[{}] Calling {}", logBlueprint, getApiName());
      LOGGER.debug("[{}] Building request...", logBlueprint);
    }
    Request request = buildRequest(method, urlInfo, headerParams, body, logBlueprint);

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("[{}] Request method: {}, url: {}", logBlueprint, method, request.url());
      if (request.body() != null) {
        LOGGER.debug("[{}] Request body: \n{}", logBlueprint, JsonConverter.getInstance().toJson(body));
      }
    }

    Response rs = executeRequest(request, logBlueprint);
    return handleResponse(rs, responseType, logBlueprint);
  }

  @Override
  public <T> T call(String method, URLInfo urlInfo, Map<String, String> headerParams, Object body, Class<T> responseType) {
    ApiResponse<T> apiRs = execute(method, urlInfo, headerParams, body, responseType);
    return apiRs.getBody();
  }

  private Response executeRequest(Request request, String logBlueprint) {
    Call call = httpClient.newCall(request);
    try (Response response = call.execute()) {
      return response;
    } catch (IOException e) {
      LOGGER.error("[{}] Network error calling {} API", logBlueprint, getApiName(), e);
      throw new ApiTransportException("Network error: " + e.getMessage(), e);
    }
  }

  protected <T> ApiResponse<T> handleResponse(Response response, Class<T> responseType, String logBlueprint)
          throws ApiExchangeException {
    if (!response.isSuccessful()) {
      return handleErrorResponse(response, logBlueprint);
    }
    return handleSuccessfulResponse(response, responseType, logBlueprint);
  }

  protected <T> ApiResponse<T> handleErrorResponse(Response response, String logBlueprint) {
    String rsBodyString = readBodyString(response, logBlueprint);

    LOGGER.error("[{}] Response from {} with error: {}", logBlueprint, getApiName(), response.code());
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("[{}] Error response body: \n{}", logBlueprint, rsBodyString);
    }

    throw new ApiExchangeException(getApiName(), response.code(), rsBodyString);
  }

  protected <T> ApiResponse<T> handleSuccessfulResponse(Response response, Class<T> responseType, String logBlueprint)
          throws ApiExchangeException {

    ApiResponse<T> rs = new ApiResponse<>();
    rs.setStatus(response.code());
    response.headers().forEach(h -> rs.getHeaders().put(h.getFirst(), h.getSecond()));

    String rsBodyString = readBodyString(response, logBlueprint);
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("[{}] Response from {} with code: {}", logBlueprint, getApiName(), response.code());
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("[{}] Response body: \n{}", logBlueprint, rsBodyString);
      }
    }

    if (responseType != String.class) {
      rs.setBody(JsonConverter.getInstance().toObject(rsBodyString, responseType));
    } else {
      rs.setBody((T) rsBodyString);
    }
    return rs;
  }

  private String readBodyString(Response response, String logBlueprint) {
    try {
      return response.body().string();
    } catch (IOException e) {
      LOGGER.error("[{}] Failed to read response body ", logBlueprint);
      throw new ApiExchangeException(getApiName(), response.code(), "[{}] Failed to read response body");
    }

  }

  private Request buildRequest(String method, URLInfo urlInfo, Map<String, String> headers, Object body, String logBlueprint) {
    // url
    String url = ApiRequestUtil.buildURL(getBaseUrl(), urlInfo);
    Request.Builder requestBuilder = new Request.Builder().url(url);

    // headers
    if (headers != null) {
      headers.forEach(requestBuilder::addHeader);
    }

    // body
    RequestBody requestBody = null;
    if (body != null) {
      if (body instanceof FileRequest) {
        requestBody = ApiRequestUtil.fileToRequestBody(((FileRequest) body));
      } else {
        String jsonContent = JsonConverter.getInstance().toJson(body);
        requestBody = RequestBody.create(jsonContent, JSON_MEDIA_TYPE);
      }
    } else {
      // Handle methods that don't allow body (GET, HEAD) vs those that do
      if ("GET".equalsIgnoreCase(method) || "HEAD".equalsIgnoreCase(method)) {
        requestBuilder.method(method, null);
      } else {
        //TODO do I need it?
        // For POST, PUT, PATCH without body, use empty body
        RequestBody emptyBody = RequestBody.create("", JSON_MEDIA_TYPE);
        requestBuilder.method(method, emptyBody);
      }
    }
    requestBuilder.method(method, requestBody);

    return afterBuildRequest(url, requestBuilder, logBlueprint).build();
  }

  protected Request.Builder afterBuildRequest(String url, Request.Builder requestBuilder, String logBlueprint) {
    return requestBuilder;
  }

  //TODO customizable logger
  protected Logger getLogger() {
    return LOGGER;
  }

  //TODO do I need this?
  public void shutdown() {
    if (httpClient != null) {
      httpClient.dispatcher().executorService().shutdown();
      httpClient.connectionPool().evictAll();
    }
  }
}
