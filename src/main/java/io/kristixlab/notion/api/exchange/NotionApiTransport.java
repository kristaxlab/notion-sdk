package io.kristixlab.notion.api.exchange;

import io.kristixlab.notion.NotionClient;
import io.kristixlab.notion.api.exchange.exception.*;
import io.kristixlab.notion.api.model.ErrorResponse;
import io.kristixlab.notion.api.util.JsonConverter;
import java.io.IOException;
import okhttp3.Request;
import okhttp3.Response;

/** Custom transport implementation for Notion API that adds authentication headers. */
public class NotionApiTransport extends ApiTransport {
  private final NotionClient client;
  private final String apiName;

  public NotionApiTransport(NotionClient client, String apiName) {
    super(client.getBaseUrl(), new RateLimiter()); // TODO: Configure rate limiter properly
    this.client = client;
    this.apiName = apiName;
  }

  @Override
  protected Request.Builder afterBuildRequest(Request.Builder requestBuilder) {
    String authHeader = null;
    if (client.getBase64AuthHeader() != null) {
      authHeader = client.getBase64AuthHeader();
    } else {
      authHeader = client.getTokenAuthHeader();
    }
    return requestBuilder
            .addHeader("Authorization", authHeader)
            .addHeader("Notion-Version", client.getVersion())
            .addHeader("Content-Type", "application/json");
  }

  @Override
  protected <RS> RS handleResponse(Response response, Class<RS> responseType)
          throws IOException, ApiExchangeException {
    try {
      return super.handleResponse(response, responseType);
    } catch (ApiExchangeException e) {
      String message = null;
      String code = null;
      String requestId = null;

      try {
        ErrorResponse error =
                JsonConverter.getInstance().toObject(e.getBody(), ErrorResponse.class);
        message = error.getMessage();
        code = error.getCode();
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

  @Override
  protected String getApiName() {
    return apiName;
  }
}
