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

### Catch a specific error

```java
try {
    Page page = client.pages().retrieve("nonexistent");
} catch (NotFoundException e) {
    System.out.println("Not found: " + e.getMessage());
}
```

### Catch all API errors

```java
try {
    // any API call
} catch (NotionApiException e) {
    System.out.printf(
        "Status=%d code=%s requestId=%s message=%s%n",
        e.getStatus(), e.getCode(), e.getRequestId(), e.getMessage());
}
```

### Catch network errors

Network-level failures (DNS resolution, connection timeouts, etc.) are wrapped in `UncheckedIOException` by `ApiClientImpl`:

```java
try {
    // any API call
} catch (NotionApiException e) {
    // Notion returned an error response
} catch (UncheckedIOException e) {
    // network-level failure
    System.out.println("Network error: " + e.getCause().getMessage());
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

### Rate limiting (429)
> **Status: Planned** 


### Transient server errors (500, 502, 503, 504)
> **Status: Planned** 

## How Error Mapping Works

`NotionErrorResponseHandler` handles every response with status >= 400, parses the body as `NotionError`, and maps it to the corresponding `NotionApiException` subclass.

For implementation details, see: [Architecture – Error mapping internals](../contributing/architecture.md#error-mapping-internals).


## Reference

- [Notion API Status codes](https://developers.notion.com/reference/status-codes) - Notion's canonical list of status codes and error codes

## See Also

- [Error Mapping Handler](../contributing/architecture.md#error-mapping-internals) -- internal details of how error responses are processed and mapped to exceptions
