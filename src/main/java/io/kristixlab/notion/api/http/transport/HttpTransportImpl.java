package io.kristixlab.notion.api.http.transport;

import io.kristixlab.notion.api.http.transport.exception.HttpResponseException;
import io.kristixlab.notion.api.http.transport.exception.HttpTransportException;
import io.kristixlab.notion.api.http.transport.rq.MultipartFormDataRequest;
import io.kristixlab.notion.api.http.transport.rq.URLInfo;
import io.kristixlab.notion.api.http.transport.rs.ApiResponse;
import io.kristixlab.notion.api.http.transport.util.HttpTransportConfig;
import io.kristixlab.notion.api.http.transport.util.MultipartFormDataUtil;
import io.kristixlab.notion.api.http.transport.util.UrlUtil;
import io.kristixlab.notion.api.json.JsonConverter;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpTransportImpl implements HttpTransport {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpTransportImpl.class);
  private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");
  private final OkHttpClient httpClient;
  @Getter private final String apiName;
  @Getter private String baseUrl = "https://api.example.com"; // Default base URL, can be overridden
  private HttpTransportConfig config = new HttpTransportConfig();

  public HttpTransportImpl() {
    this.httpClient = createHttpClient();
    if (baseUrl == null) {
      baseUrl = "";
    }
    this.apiName = "API Transport";
  }

  public HttpTransportImpl(String baseUrl, String apiName) {
    this.httpClient = createHttpClient();
    if (baseUrl == null) {
      baseUrl = "";
    } else if (baseUrl.endsWith("/") && !baseUrl.equals("https://") && !baseUrl.equals("http://")) {
      baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
    }
    this.baseUrl = baseUrl;
    this.apiName = apiName == null ? "API Transport" : apiName;
  }

  public HttpTransportImpl(HttpTransportConfig config) {
    this(config.getBaseUrl(), config.getApiName());
    this.config = config;
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

  public <T> ApiResponse<T> execute(
      String method,
      URLInfo urlInfo,
      Map<String, String> headerParams,
      Object body,
      Class<T> responseType) {
    String logBlueprint = UUID.randomUUID().toString();

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("[{}] Calling {}", logBlueprint, getApiName());
      LOGGER.debug("[{}] Building request...", logBlueprint);
    }
    Request request = buildRequest(method, urlInfo, headerParams, body, logBlueprint);

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(
          "[{}] Request method: {}, url: {}, content-type: {}",
          logBlueprint,
          method,
          request.url(),
          request.header("Content-Type"));
      if (request.body() != null) {
        // TODO adjust for MultipartFormDataRequest
        LOGGER.debug(
            "[{}] Request body: \n{}", logBlueprint, JsonConverter.getInstance().toJson(body));
      }
    }

    Response rs = executeRequest(request, logBlueprint);
    return handleResponse(rs, responseType, logBlueprint);
  }

  @Override
  public <T> T call(
      String method,
      URLInfo urlInfo,
      Map<String, String> headerParams,
      Object body,
      Class<T> responseType) {
    ApiResponse<T> apiRs = execute(method, urlInfo, headerParams, body, responseType);
    return apiRs.getBody();
  }

  private Response executeRequest(Request request, String logBlueprint) {
    Call call = httpClient.newCall(request);
    try {
      return call.execute();
    } catch (IOException e) {
      LOGGER.error("[{}] Network error calling {} API", logBlueprint, getApiName(), e);
      throw new HttpTransportException("Network error: " + e.getMessage(), e);
    }
  }

  protected <T> ApiResponse<T> handleResponse(
      Response response, Class<T> responseType, String logBlueprint) throws HttpResponseException {
    if (!response.isSuccessful()) {
      return handleErrorResponse(response, logBlueprint);
    }
    return handleSuccessfulResponse(response, responseType, logBlueprint);
  }

  protected <T> ApiResponse<T> handleErrorResponse(Response response, String logBlueprint) {
    String rsBodyString = readBodyString(response, logBlueprint);

    LOGGER.error(
        "[{}] Response from {} with error: {}", logBlueprint, getApiName(), response.code());
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("[{}] Error response body: \n{}", logBlueprint, rsBodyString);
    }

    throw new HttpResponseException(getApiName(), response.code(), rsBodyString);
  }

  protected <T> ApiResponse<T> handleSuccessfulResponse(
      Response response, Class<T> responseType, String logBlueprint) throws HttpResponseException {

    ApiResponse<T> rs = new ApiResponse<>();
    rs.setStatus(response.code());
    response.headers().forEach(h -> rs.getHeaders().put(h.getFirst(), h.getSecond()));

    String rsBodyString = readBodyString(response, logBlueprint);
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(
          "[{}] Response from {} with code: {}", logBlueprint, getApiName(), response.code());
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("[{}] Response body: \n{}", logBlueprint, rsBodyString);
      }
    }

    if (responseType != String.class) {
      rs.setBody(
          JsonConverter.getInstance()
              .toObject(rsBodyString, responseType, getConfig().isJsonFailOnUnknownProperties()));
    } else {
      rs.setBody((T) rsBodyString);
    }
    return rs;
  }

  private String readBodyString(Response response, String logBlueprint) {
    try (ResponseBody body = response.body()) {
      return body.string();
    } catch (Exception e) {
      LOGGER.error("[{}] Failed to read response body ", logBlueprint, e);
      throw new HttpTransportException("[" + logBlueprint + "] Failed to read response body", e);
    }
  }

  private Request buildRequest(
      String method,
      URLInfo urlInfo,
      Map<String, String> headers,
      Object body,
      String logBlueprint) {
    // url
    String url = UrlUtil.toUrlString(getBaseUrl(), urlInfo);
    Request.Builder requestBuilder = new Request.Builder().url(url);

    // headers
    if (headers != null) {
      headers.forEach(requestBuilder::addHeader);
    }

    // body
    RequestBody requestBody = null;
    if (body != null) {
      if (body instanceof MultipartFormDataRequest) {
        // TODO impl test to check that streaming configuration is working
        Long thresholdBytes = getConfig().getStreamFileAfterBytes();
        boolean asStream = shouldBeStreamed((MultipartFormDataRequest) body, thresholdBytes);
        requestBody =
            MultipartFormDataUtil.toRequestBody(((MultipartFormDataRequest) body), asStream);
      } else {
        String jsonContent = JsonConverter.getInstance().toJson(body);
        requestBody = RequestBody.create(jsonContent, JSON_MEDIA_TYPE);
        requestBuilder.addHeader("Content-Type", "application/json");
      }
    } else {
      // Handle methods that don't allow body (GET, HEAD) vs those that do
      if ("GET".equalsIgnoreCase(method) || "HEAD".equalsIgnoreCase(method)) {
        requestBuilder.method(method, null);
      } else {
        // TODO do I need it?
        // For POST, PUT, PATCH without body, use empty body
        RequestBody emptyBody = RequestBody.create("", JSON_MEDIA_TYPE);
        requestBuilder.method(method, emptyBody);
      }
    }
    requestBuilder.method(method, requestBody);

    return afterBuildRequest(url, requestBuilder, logBlueprint).build();
  }

  private boolean shouldBeStreamed(MultipartFormDataRequest body, Long thresholdBytes) {
    if (thresholdBytes == null) {
      return false;
    }
    MultipartFormDataRequest.FilePart filePart =
        body.getParts().stream()
            .filter(part -> part instanceof MultipartFormDataRequest.FilePart)
            .map(part -> (MultipartFormDataRequest.FilePart) part)
            .findFirst()
            .orElse(null);
    if (filePart != null) {
      File file = filePart.getContent();
      return file != null && file.length() >= thresholdBytes;
    }
    return false;
  }

  protected Request.Builder afterBuildRequest(
      String url, Request.Builder requestBuilder, String logBlueprint) {
    return requestBuilder;
  }

  // TODO customizable logger
  protected Logger getLogger() {
    return LOGGER;
  }

  protected HttpTransportConfig getConfig() {
    return config;
  }

  // TODO do I need this?
  public void shutdown() {
    if (httpClient != null) {
      httpClient.dispatcher().executorService().shutdown();
      httpClient.connectionPool().evictAll();
    }
  }
}
