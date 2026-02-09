package io.kristixlab.notion.api.http;

import io.kristixlab.notion.api.NotionAuthSettings;
import io.kristixlab.notion.api.http.exception.*;
import io.kristixlab.notion.api.http.transport.HttpTransportImpl;
import io.kristixlab.notion.api.http.transport.exception.HttpResponseException;
import io.kristixlab.notion.api.http.transport.rq.URLInfo;
import io.kristixlab.notion.api.http.transport.rs.ApiResponse;
import io.kristixlab.notion.api.json.JsonConverter;
import io.kristixlab.notion.api.model.NotionError;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Response;

/** Custom transport implementation for Notion API that adds authentication headers. */
public class NotionHttpTransport extends HttpTransportImpl {

  private static final String DEFAULT_VERSION = "2025-09-03"; /*"2022-06-28";*/
  private static final String DEFAULT_BASE_URL = "https://api.notion.com/v1/";
  private String version;

  private NotionAuthSettings notionAuthSettings;

  public NotionHttpTransport() {}

  public NotionHttpTransport(NotionAuthSettings notionAuthSettings) {
    this(notionAuthSettings, DEFAULT_BASE_URL, DEFAULT_VERSION);
  }

  public NotionHttpTransport(
      NotionAuthSettings notionAuthSettings, String baseUrl, String version) {
    super(baseUrl, "NotionAPIv" + version);
    this.version = version;
    this.notionAuthSettings = notionAuthSettings;
  }

  /**
   * Execute an API request, adding Notion-specific headers.
   *
   * @param method The HTTP method
   * @param urlInfo The URL information
   * @param headerParams The header parameters
   * @param body The request body
   * @param responseType The expected response type
   * @param <T> The type of the response
   * @return The ApiResponse object
   */
  @Override
  public <T> ApiResponse<T> execute(
      String method,
      URLInfo urlInfo,
      Map<String, String> headerParams,
      Object body,
      Class<T> responseType) {

    if (headerParams == null) {
      headerParams = new HashMap<>();
    }

    headerParams.put("Notion-Version", version);

    headerParams.put("Accept", "application/json");

    if (!headerParams.containsKey("Authorization")) {
      if ("/oauth/token".equals(urlInfo.getUrl())
          || "/oauth/introspect".equals(urlInfo.getUrl())
          || "/oauth/revoke".equals(urlInfo.getUrl())) {
        if (notionAuthSettings.getOauthBasicHeader() == null) {
          throw new IllegalStateException(
              "Client ID and Client Secret must be set for OAuth token exchange");
        }
        headerParams.put("Authorization", notionAuthSettings.getOauthBasicHeader());
      } else {
        if (notionAuthSettings.getTokenAuthHeader() == null) {
          throw new IllegalStateException("Auth token for Notion API is missing");
        }
        headerParams.put("Authorization", notionAuthSettings.getTokenAuthHeader());
      }
    }

    return super.execute(method, urlInfo, headerParams, body, responseType);
  }

  /**
   * Handle the API response, mapping HTTP status codes to specific exceptions.
   *
   * @param response The HTTP response
   * @param responseType The expected response type
   * @param logBlueprint The log blueprint for logging
   * @param <RS> The type of the response
   * @return The ApiResponse object
   * @throws HttpResponseException If an error occurs during the API exchange
   */
  @Override
  protected <RS> ApiResponse<RS> handleResponse(
      Response response, Class<RS> responseType, String logBlueprint) throws HttpResponseException {
    try {
      return super.handleResponse(response, responseType, logBlueprint);
    } catch (HttpResponseException e) {
      String message = null;
      String code = null;
      String requestId = null;

      try {
        NotionError error = JsonConverter.getInstance().toObject(e.getBody(), NotionError.class);
        message = error.getMessage();
        code = error.getCode() != null ? error.getCode() : error.getError();
        requestId = error.getRequestId();
      } catch (Exception ex) {
        message = e.getBody();
      }

      switch (e.getStatus()) {
        case 400:
          throw new ValidationException(code, message, requestId);
        case 401:
          throw new UnauthorizedException(code, message, requestId);
        case 403:
          throw new ForbiddenException(code, message, requestId);
        case 404:
          throw new NotFoundException(code, message, requestId);
        case 409:
          throw new ConflictException(code, message, requestId);
        case 429:
          throw new TooManyRequestsException(code, message, requestId);
        case 500:
          throw new InternalServerException(code, message, requestId);
        case 502:
          throw new BadGatewayException(code, message, requestId);
        case 503:
          throw new ServiceUnavailableException(code, message, requestId);
        case 504:
          throw new GatewayTimeoutException(code, message, requestId);
        default:
          throw e;
      }
    }
  }
}
