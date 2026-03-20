package io.kristixlab.notion.api.http.transport;

import io.kristixlab.notion.api.http.transport.exception.HttpResponseException;
import io.kristixlab.notion.api.http.transport.exception.HttpTransportException;
import io.kristixlab.notion.api.http.transport.log.ExchangeContext;
import io.kristixlab.notion.api.http.transport.log.ExchangeLogger;
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
  @Getter private String apiName;
  @Getter private String baseUrl = "https://api.example.com"; // Default base URL, can be overridden
  private HttpTransportConfig config = new HttpTransportConfig();
  private ExchangeLogger exchangeLogger;

  public HttpTransportImpl() {
    this("", "API Transport");
  }

  public HttpTransportImpl(String baseUrl, String apiName) {
    // TODO possibility to pass regular logger as exchange logger??
    this(baseUrl, apiName, defaultConfig(), null);
  }

  public HttpTransportImpl(
      String baseUrl, String apiName, HttpTransportConfig config, ExchangeLogger exchangeLogger) {
    this.httpClient = createHttpClient();
    this.exchangeLogger = exchangeLogger;

    setBaseUrl(baseUrl);
    setApiName(apiName);
    setConfig(config);
  }

  private OkHttpClient createHttpClient() {
    return new OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build();
  }

  private void setBaseUrl(String baseUrl) {
    if (baseUrl == null) {
      baseUrl = "";
    } else if (baseUrl.endsWith("/") && !baseUrl.equals("https://") && !baseUrl.equals("http://")) {
      baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
    }
    this.baseUrl = baseUrl;
  }

  private void setApiName(String apiName) {
    this.apiName = apiName == null ? "API Transport" : apiName;
  }

  private void setConfig(HttpTransportConfig config) {
    this.config = config == null ? defaultConfig() : config;
  }

  private static HttpTransportConfig defaultConfig() {
    HttpTransportConfig config = new HttpTransportConfig();
    config.setJsonFailOnUnknownProperties(false);
    return config;
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
    String exchangeId =
        ExchangeContext.getCurrent()
            .computeStringIfAbsent("exchangeId", s -> UUID.randomUUID().toString());

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("[{}] Calling {}", exchangeId, getApiName());
      LOGGER.debug("[{}] Building request...", exchangeId);
    }

    Request request = buildRequest(method, urlInfo, headerParams, body, exchangeId);
    logRequest(request, body, exchangeId);

    Response rs = executeRequest(request, exchangeId);
    return handleResponse(rs, responseType, exchangeId);
  }

  protected void logRequest(Request request, Object body, String exchangeId) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(
          "[{}] Request method: {}, url: {}, content-type: {}",
          exchangeId,
          request.method(),
          request.url(),
          request.header("Content-Type"));
      if (body != null) {
        // TODO adjust for MultipartFormDataRequest
        LOGGER.debug(
            "[{}] Request body: \n{}", exchangeId, JsonConverter.getInstance().toJson(body));
      }
    }

    if (exchangeLogger != null) {
      // TODO serviceName pass from the endpoint implementation?
      // ExchangeContext.getCurrent().put("serviceName", getApiName());
      ExchangeContext.getCurrent().put("method", request.method());
      ExchangeContext.getCurrent().put("path", request.url().toString());
      ExchangeContext.getCurrent().put("requestHeaders", request.headers().toMultimap());
      if (body != null) {
        ExchangeContext.getCurrent().put("requestBody", body);
      }
      exchangeLogger.logRequest(ExchangeContext.getCurrent());
    }
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
      Response response, Class<T> responseType, String exchangeId) throws HttpResponseException {

    ApiResponse apiResponse = null;
    // TODO getApiName replace with some kind of service id passed from the endpoint implementation?
    // or maybe from the request context?
    Class convertTo = response.isSuccessful() ? responseType : getErrorType(getApiName());

    apiResponse = toApiReponse(response, convertTo, exchangeId);
    logResponse(apiResponse, apiResponse.getStatus(), exchangeId);

    if (!response.isSuccessful()) {
      throw new HttpResponseException(getApiName(), response.code(), apiResponse.getBody());
    }
    return apiResponse;
  }

  protected void logResponse(ApiResponse response, int status, String exchangeId) {
    LOGGER.error("[{}] Response from {} with error: {}", exchangeId, getApiName(), status);
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("[{}] Error response body: \n{}", exchangeId, response.getBody());
    }

    if (exchangeLogger != null) {
      ExchangeContext.getCurrent().put("responseStatus", status);
      ExchangeContext.getCurrent().put("responseHeaders", response.getHeaders());
      if (response.getBody() != null) {
        ExchangeContext.getCurrent().put("responseBody", response.getBody());
      }
      exchangeLogger.logResponse(ExchangeContext.getCurrent());
    }
  }

  protected <T> ApiResponse<T> toApiReponse(
      Response response, Class<T> responseType, String exchangeId) throws HttpResponseException {

    ApiResponse<T> rs = new ApiResponse<>();
    rs.setStatus(response.code());
    response.headers().forEach(h -> rs.getHeaders().put(h.getFirst(), h.getSecond()));

    String rsBodyString = readBodyString(response, exchangeId);
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(
          "[{}] Response from {} with code: {}", exchangeId, getApiName(), response.code());
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("[{}] Response body: \n{}", exchangeId, rsBodyString);
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
  protected Logger getLoggechr() {
    return LOGGER;
  }

  protected HttpTransportConfig getConfig() {
    return config;
  }

  protected Class<?> getErrorType(String serviceId) {
    return (Class<?>) String.class;
  }

  // TODO do I need this?
  public void shutdown() {
    if (httpClient != null) {
      httpClient.dispatcher().executorService().shutdown();
      httpClient.connectionPool().evictAll();
    }
  }
}
