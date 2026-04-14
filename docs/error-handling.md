# Error Handling

The SDK automatically maps Notion API error responses (HTTP 4xx/5xx) to typed Java exceptions. Every error carries the HTTP status, Notion error code, human-readable message, and request ID for diagnostics.

## Exception Hierarchy

All Notion API errors extend `NotionApiException`, which is a `RuntimeException`:

```
RuntimeException
  └── NotionApiException (status, code, message, requestId)
        ├── ValidationException          (400)
        ├── UnauthorizedException        (401)
        ├── ForbiddenException           (403)
        ├── NotFoundException            (404)
        ├── ConflictException            (409)
        ├── TooManyRequestsException     (429)
        ├── InternalServerException      (500)
        ├── BadGatewayException          (502)
        ├── ServiceUnavailableException  (503)
        └── GatewayTimeoutException      (504)
```

Unrecognized status codes fall through to the base `NotionApiException`.

## Catching Errors

Network-level failures (DNS resolution, connection timeouts, etc.) are wrapped in `UncheckedIOException` by `ApiClientImpl`:

```java
try {
    // any API call
} catch (NotFoundException e) {
    // Catch specific API error
} catch (NotionApiException e) {
    // Catch all API errors
} catch (UncheckedIOException e) {
    // Catch network-level failure
}
```

## Exception Properties

Every `NotionApiException` provides:

| Property | Type | Description |
|---|---|---|
| `getStatus()` | `int` | HTTP status code (400, 401, 404, etc.) |
| `getCode()` | `String` | Notion error code (e.g. `"object_not_found"`, `"validation_error"`) |
| `getMessage()` | `String` | Human-readable error description |
| `getRequestId()` | `String` | Notion request ID for support tickets |

## Retry Strategies

> **Status: Planned** -- Built-in retry support is not yet implemented.

## Reference

- [**Official Notion Doc:** API Status codes](https://developers.notion.com/reference/status-codes)
- [Back to README](../README.md)