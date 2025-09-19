package io.kristixlab.notion.api;

import io.kristixlab.notion.api.exchange.exception.*;
import io.kristixlab.notion.api.exchange.transport.ApiExchangeException;
import io.kristixlab.notion.api.exchange.transport.ApiTransport;
import io.kristixlab.notion.api.model.ErrorResponse;
import io.kristixlab.notion.api.util.JsonConverter;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Custom transport implementation for Notion API that adds authentication headers.
 */
public class NotionApiTransport extends ApiTransport {

  private final String version;
  private static final String DEFAULT_VERSION = "2025-09-03"; /*"2022-06-28";*/
  private static final String DEFAULT_BASE_URL = "https://api.notion.com/v1/";

  private NotionApiClient notionApiClient;

  public NotionApiTransport() {
    this(DEFAULT_BASE_URL, DEFAULT_VERSION);
  }

  public NotionApiTransport(String baseUrl, String version) {
    super(baseUrl, "NotionAPIv" + version);

    this.version = version;
  }

  public NotionApiTransport(NotionApiClient client) {
    super(client.getBaseUrl(), "NotionAPIv" + client.getVersion());
    this.notionApiClient = client;
    this.version = client.getVersion();
  }

  @Override
  protected Request.Builder afterBuildRequest(String url, Request.Builder requestBuilder) {
    String authHeader = null;
    if ("/oauth/token".equals(url)
            || "/oauth/introspect".equals(url)
            || "/oauth/revoke".equals(url)) {
      authHeader = notionApiClient.getAuthSettings().getPublicIntegrationBase64AuthHeader();
      if (authHeader == null) {
        throw new IllegalStateException(
                "Client ID and Client Secret must be set for OAuth token exchange");
      }
    } else {
      authHeader = notionApiClient.getAuthSettings().getTokenAuthHeader();
    }
    return requestBuilder
            .addHeader("Authorization", authHeader)
            .addHeader("Notion-Version", this.version)
            .addHeader("Content-Type", "application/json");
  }

  @Override
  protected <RS> RS handleResponse(Response response, Class<RS> responseType, String logBlueprint)
          throws IOException, ApiExchangeException {
    try {
      return super.handleResponse(response, responseType, logBlueprint);
    } catch (ApiExchangeException e) {
      String message = null;
      String code = null;
      String requestId = null;

      try {
        ErrorResponse error =
                JsonConverter.getInstance().toObject(e.getBody(), ErrorResponse.class);
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
