package io.kristixlab.notion.api.http.error;

import io.kristixlab.notion.api.http.client.ErrorResponseHandler;
import io.kristixlab.notion.api.http.client.HttpClient.HttpRequest;
import io.kristixlab.notion.api.http.client.HttpClient.HttpResponse;
import io.kristixlab.notion.api.json.JsonConverter;
import io.kristixlab.notion.api.model.NotionError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Notion-specific implementation of {@link ErrorResponseHandler} that maps HTTP error responses to
 * domain-specific {@link NotionApiException} subclasses.
 *
 * <p>This handler maps HTTP error responses to domain-specific {@link NotionApiException}
 * subclasses. It deserializes the response body into a {@link NotionError}, extracts the error
 * code, message, and request ID, then throws the appropriate exception based on the HTTP status
 * code.
 *
 * <p>Status code mapping:
 *
 * <table>
 *   <tr><th>Status</th><th>Exception</th></tr>
 *   <tr><td>400</td><td>{@link ValidationException}</td></tr>
 *   <tr><td>401</td><td>{@link UnauthorizedException}</td></tr>
 *   <tr><td>403</td><td>{@link ForbiddenException}</td></tr>
 *   <tr><td>404</td><td>{@link NotFoundException}</td></tr>
 *   <tr><td>409</td><td>{@link ConflictException}</td></tr>
 *   <tr><td>429</td><td>{@link TooManyRequestsException}</td></tr>
 *   <tr><td>500</td><td>{@link InternalServerException}</td></tr>
 *   <tr><td>502</td><td>{@link BadGatewayException}</td></tr>
 *   <tr><td>503</td><td>{@link ServiceUnavailableException}</td></tr>
 *   <tr><td>504</td><td>{@link GatewayTimeoutException}</td></tr>
 *   <tr><td>other</td><td>{@link NotionApiException}</td></tr>
 * </table>
 *
 * @see ErrorResponseHandler
 * @see io.kristixlab.notion.api.http.client.ErrorHandlingHttpClient
 */
public class NotionErrorResponseHandler implements ErrorResponseHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(NotionErrorResponseHandler.class);

  /**
   * Maps an HTTP status code to the corresponding {@link NotionApiException} subclass.
   *
   * @param status the HTTP status code
   * @param code the Notion error code (e.g. {@code "validation_error"})
   * @param message the human-readable error message
   * @param requestId the Notion request ID for support inquiries
   * @return the mapped exception (never {@code null})
   */
  private static NotionApiException toException(
      int status, String code, String message, String requestId) {
    switch (status) {
      case 400:
        return new ValidationException(code, message, requestId);
      case 401:
        return new UnauthorizedException(code, message, requestId);
      case 403:
        return new ForbiddenException(code, message, requestId);
      case 404:
        return new NotFoundException(code, message, requestId);
      case 409:
        return new ConflictException(code, message, requestId);
      case 429:
        return new TooManyRequestsException(code, message, requestId);
      case 500:
        return new InternalServerException(code, message, requestId);
      case 502:
        return new BadGatewayException(code, message, requestId);
      case 503:
        return new ServiceUnavailableException(code, message, requestId);
      case 504:
        return new GatewayTimeoutException(code, message, requestId);
      default:
        return new NotionApiException(status, code, message, requestId);
    }
  }

  @Override
  public void handle(HttpRequest request, HttpResponse response) {
    if (response.statusCode() < 400) {
      return;
    }

    String bodyString = response.bodyAsString();
    String message;
    String code = null;
    String requestId = null;

    try {
      NotionError error =
          JsonConverter.getInstance().toObject(bodyString, NotionError.class, false);
      message = error.getMessage();
      code = error.getCode() != null ? error.getCode() : error.getError();
      requestId = error.getRequestId();
    } catch (Exception e) {
      LOGGER.debug("Failed to parse Notion error body, using raw body as message", e);
      message = bodyString != null ? bodyString : "unknown error";
    }

    throw toException(response.statusCode(), code, message, requestId);
  }
}
