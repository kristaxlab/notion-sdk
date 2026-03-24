package io.kristixlab.notion.api.http;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.client.HttpClient.*;
import io.kristixlab.notion.api.http.error.*;
import io.kristixlab.notion.api.json.JsonConverter;
import io.kristixlab.notion.api.model.NotionError;
import java.util.Map;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Unit tests for {@link NotionErrorResponseHandler}. */
class NotionErrorResponseHandlerTest {

  private final NotionErrorResponseHandler handler = new NotionErrorResponseHandler();

  // ------------------------------------------------------------------
  // Helpers
  // ------------------------------------------------------------------

  private static HttpRequest anyRequest() {
    return HttpRequest.builder()
        .url("https://api.notion.com/v1/pages")
        .method(HttpMethod.GET)
        .build();
  }

  private static HttpResponse responseOf(int status, String body) {
    return new HttpResponse(status, Map.of(), body != null ? body.getBytes() : new byte[0]);
  }

  private static String notionErrorJson(String code, String message, String requestId) {
    NotionError error = new NotionError();
    error.setObject("error");
    error.setCode(code);
    error.setMessage(message);
    error.setRequestId(requestId);
    error.setStatus(400); // status in the body isn't used for mapping
    return JsonConverter.getInstance().toJson(error);
  }

  // ------------------------------------------------------------------
  // 2xx — no exception
  // ------------------------------------------------------------------

  @ParameterizedTest(name = "status {0} passes through")
  @CsvSource({"200", "201", "204", "302"})
  void successfulStatusDoesNotThrow(int status) {
    HttpResponse response = responseOf(status, "{\"object\": \"page\"}");
    assertDoesNotThrow(() -> handler.handle(anyRequest(), response));
  }

  // ------------------------------------------------------------------
  // Status → specific exception type
  // ------------------------------------------------------------------

  @Test
  @DisplayName("400 → ValidationException")
  void status400() {
    String body = notionErrorJson("validation_error", "Invalid input", "req-001");
    ValidationException ex =
        assertThrows(
            ValidationException.class, () -> handler.handle(anyRequest(), responseOf(400, body)));
    assertEquals("validation_error", ex.getCode());
    assertEquals("Invalid input", ex.getMessage());
    assertEquals("req-001", ex.getRequestId());
    assertEquals(400, ex.getStatus());
  }

  @Test
  @DisplayName("401 → UnauthorizedException")
  void status401() {
    String body = notionErrorJson("unauthorized", "API token is invalid", "req-002");
    UnauthorizedException ex =
        assertThrows(
            UnauthorizedException.class, () -> handler.handle(anyRequest(), responseOf(401, body)));
    assertEquals("unauthorized", ex.getCode());
    assertEquals("API token is invalid", ex.getMessage());
  }

  @Test
  @DisplayName("403 → ForbiddenException")
  void status403() {
    String body = notionErrorJson("restricted_resource", "Not allowed", "req-003");
    assertThrows(
        ForbiddenException.class, () -> handler.handle(anyRequest(), responseOf(403, body)));
  }

  @Test
  @DisplayName("404 → NotFoundException")
  void status404() {
    String body = notionErrorJson("object_not_found", "Page not found", "req-004");
    assertThrows(
        NotFoundException.class, () -> handler.handle(anyRequest(), responseOf(404, body)));
  }

  @Test
  @DisplayName("409 → ConflictException")
  void status409() {
    String body = notionErrorJson("conflict_error", "Conflict", "req-005");
    assertThrows(
        ConflictException.class, () -> handler.handle(anyRequest(), responseOf(409, body)));
  }

  @Test
  @DisplayName("429 → TooManyRequestsException")
  void status429() {
    String body = notionErrorJson("rate_limited", "Rate limited", "req-006");
    assertThrows(
        TooManyRequestsException.class, () -> handler.handle(anyRequest(), responseOf(429, body)));
  }

  @Test
  @DisplayName("500 → InternalServerException")
  void status500() {
    String body = notionErrorJson("internal_server_error", "Server error", "req-007");
    assertThrows(
        InternalServerException.class, () -> handler.handle(anyRequest(), responseOf(500, body)));
  }

  @Test
  @DisplayName("502 → BadGatewayException")
  void status502() {
    String body = notionErrorJson("bad_gateway", "Bad gateway", "req-008");
    assertThrows(
        BadGatewayException.class, () -> handler.handle(anyRequest(), responseOf(502, body)));
  }

  @Test
  @DisplayName("503 → ServiceUnavailableException")
  void status503() {
    String body = notionErrorJson("service_unavailable", "Unavailable", "req-009");
    assertThrows(
        ServiceUnavailableException.class,
        () -> handler.handle(anyRequest(), responseOf(503, body)));
  }

  @Test
  @DisplayName("504 → GatewayTimeoutException")
  void status504() {
    String body = notionErrorJson("gateway_timeout", "Timeout", "req-010");
    assertThrows(
        GatewayTimeoutException.class, () -> handler.handle(anyRequest(), responseOf(504, body)));
  }

  @Test
  @DisplayName("Unmapped status (e.g. 418) → generic NotionApiException")
  void unmappedStatus() {
    String body = notionErrorJson("teapot", "I'm a teapot", "req-011");
    NotionApiException ex =
        assertThrows(
            NotionApiException.class, () -> handler.handle(anyRequest(), responseOf(418, body)));
    // Must be the base class, not a subclass
    assertEquals(NotionApiException.class, ex.getClass());
    assertEquals(418, ex.getStatus());
    assertEquals("teapot", ex.getCode());
  }

  // ------------------------------------------------------------------
  // code vs error field fallback
  // ------------------------------------------------------------------

  @Test
  @DisplayName("Falls back to 'error' field when 'code' is null")
  void fallsBackToErrorField() {
    NotionError error = new NotionError();
    error.setObject("error");
    error.setError("unauthorized");
    error.setCode(null);
    error.setMessage("Token expired");
    error.setRequestId("req-012");
    String body = JsonConverter.getInstance().toJson(error);

    UnauthorizedException ex =
        assertThrows(
            UnauthorizedException.class, () -> handler.handle(anyRequest(), responseOf(401, body)));
    assertEquals("unauthorized", ex.getCode());
  }

  // ------------------------------------------------------------------
  // Malformed / non-JSON body
  // ------------------------------------------------------------------

  @Test
  @DisplayName("Non-JSON body falls back to raw body as message")
  void nonJsonBodyFallback() {
    NotionApiException ex =
        assertThrows(
            NotionApiException.class,
            () -> handler.handle(anyRequest(), responseOf(500, "Internal Server Error")));
    assertEquals("Internal Server Error", ex.getMessage());
    assertNull(ex.getCode());
    assertNull(ex.getRequestId());
  }

  @Test
  @DisplayName("Empty body falls back gracefully")
  void emptyBodyFallback() {
    NotionApiException ex =
        assertThrows(
            NotionApiException.class, () -> handler.handle(anyRequest(), responseOf(502, "")));
    assertNotNull(ex.getMessage());
  }

  @Test
  @DisplayName("Null body bytes produce 'unknown error' message")
  void nullBodyFallback() {
    HttpResponse response = new HttpResponse(503, Map.of(), null);
    NotionApiException ex =
        assertThrows(NotionApiException.class, () -> handler.handle(anyRequest(), response));
    // bodyAsString() returns null → fallback message
    assertNotNull(ex.getMessage());
  }
}
