# Architecture

This document describes the Notion SDK's internal architecture, package structure, and key design decisions. Read this before contributing code.

## Package Overview

```
io.kristaxlab.notion
├── auth/        # Authentication providers and interceptors
├── http/        # Notion-specific HTTP layer and reusable HTTP infrastructure
├── model/       # Notion object models (blocks, pages, users, etc.)
```

## Request Lifecycle

A typical API call flows through these layers:

```
1. NotionClient / user code
   │
   │  client.getHttpClient().call("GET", ApiPath.from("/pages/id"), String.class)
   │
2. ApiClientImpl
   │  - Resolves URL: baseUrl + ApiPath → full URL
   │  - Serializes body (if any) → JSON string
   │  - Builds HttpRequest
   │
3. ErrorHandlingHttpClient
   │  - Delegates to InterceptingHttpClient
   │  - On response: if status >= 400, throws NotionApiException
   │
4. InterceptingHttpClient
   │  - Runs beforeSend on each interceptor (in order):
   │    a) NotionVersionInterceptor  → adds Notion-Version header
   │    b) NotionAuthInterceptor     → adds Authorization header
   │    c) ExchangeRecordingInterceptor → logs request to file
   │    d) LoggingHttpInterceptor    → logs via SLF4J
   │  - Delegates to raw HttpClient
   │  - Runs afterReceive on each interceptor (reverse order)
   │
5. OkHttp3Client
   │  - Converts HttpRequest → OkHttp Request
   │  - Sends HTTP request
   │  - Converts OkHttp Response → HttpResponse
   │
6. Response flows back up through the pipeline
   │
7. ApiClientImpl
   - Deserializes response body → target type
   - Returns typed result to caller
```

## Key Design Decisions

### Why a `base` package?

The `base` package (`http.base.*`) contains zero Notion-specific knowledge. It's a general-purpose HTTP client framework that could be extracted into a separate library. This separation ensures:

- Notion-specific logic (auth, versioning, error codes) is isolated
- The HTTP infrastructure is independently testable

### Why `ErrorHandlingHttpClient` sits outside `InterceptingHttpClient`

Error handling wraps the interceptor chain so that interceptors (especially logging and exchange recording) observe the raw response before any exception is thrown. This means log files capture the error response body, and exchange recordings contain the full error payload.

## Error Mapping Internals

This section documents how Notion API errors (HTTP status >= 400) are turned into typed Java exceptions.

### How Error Mapping Works

`NotionErrorResponseHandler` handles every response with status >= 400, parses the body as `NotionError`, and maps it to the corresponding `NotionApiException` subclass.


### Components

- `ErrorHandlingHttpClient`: decorator that invokes an `ErrorResponseHandler` on every response
- `NotionErrorResponseHandler`: parses the error body and throws the corresponding `NotionApiException` subtype
- `NotionError`: DTO representing Notion's error response body
- `NotionApiException`: base exception carrying `status`, `code`, `message`, and `requestId`

### Control flow

1. `ApiClientImpl` sends a request using the composed `HttpClient` pipeline
2. The raw transport returns an `HttpResponse`
3. Interceptors run `afterReceive` (logging and exchange recording can capture the raw error response)
4. `ErrorHandlingHttpClient` invokes `NotionErrorResponseHandler.handle(request, response)`
5. If `response.statusCode() >= 400`, the handler:
   - Attempts to deserialize the response body as `NotionError`
   - Extracts `code`, `message`, and `requestId`
   - Throws a typed exception based on HTTP status (400 → `ValidationException`, 401 → `UnauthorizedException`, 404 → `NotFoundException`, 429 → `TooManyRequestsException`, etc.)
   - If parsing fails, uses the raw response body as the exception message

### Where to update mappings

- Add or change mappings in `error.http.io.kristaxlab.notion.NotionErrorResponseHandler`
- Add new exception types in `io.kristaxlab.notion.http.error.*`
- Update/extend tests in `src/test/java/io/kristaxlab/notion/http/error/NotionErrorResponseHandlerTest.java`

### Reference

Notion's canonical list of status codes and error codes is maintained here:

- [Notion API Status codes](https://developers.notion.com/reference/status-codes)


## Interceptors

The SDK uses a composable interceptor pipeline to handle cross-cutting concerns like authentication, API versioning, logging, etc.

## Pipeline Architecture

Every request flows through this pipeline:

```
  Your Code
    │
    ▼
  ApiClientImpl              URL building, body serialization, response deserialization
    │
    ▼
  ErrorHandlingHttpClient    Maps 4xx/5xx responses to NotionApiException (outermost)
    │
    ▼
  InterceptingHttpClient     Runs interceptors in order
    │
    ├── NotionVersionInterceptor     Adds Notion-Version header
    ├── NotionAuthInterceptor        Adds Authorization header
    ├── ExchangeRecordingInterceptor Logs request/response to disk (if enabled)
    └── LoggingHttpInterceptor       Logs via SLF4J
    │ 
    ▼
  OkHttp3Client              Sends the actual HTTP request
    │
    ▼
  Notion API
```

**Request flow:** interceptors execute `beforeSend` in registration order (top to bottom).

**Response flow:** interceptors execute `afterReceive` in reverse order (bottom to top), then `ErrorHandlingHttpClient` checks the status.

## The `HttpClientInterceptor` Interface

```java
public interface HttpClientInterceptor {

    HttpRequest beforeSend(HttpRequest request);

    void afterReceive(HttpRequest request, HttpResponse response);
}
```

### Why interceptors use `beforeSend` / `afterReceive`

This two-hook design is simpler than a full "chain" pattern (like OkHttp's `Interceptor.Chain`). Most cross-cutting concerns only need one hook -- adding a header (`beforeSend`) or logging a response (`afterReceive`). The tradeoff is that interceptors cannot short-circuit the chain.

### Built-in Interceptors

#### NotionVersionInterceptor

Adds the `Notion-Version` header to every request. Configured via `NotionClientBuilder.version()`.

```java
// Adds: Notion-Version: 2026-03-11
new NotionVersionInterceptor("2026-03-11")
```

#### NotionAuthInterceptor

Adds `Authorization: Bearer <token>` to every request that doesn't already have an `Authorization` header.

```java
new NotionAuthInterceptor(new FixedTokenProvider("secret_xxx"))
```

#### ExchangeRecordingInterceptor

Writes each request/response pair as JSON files to a directory. See [Exchange Recording](exchange-recording.md).

#### LoggingHttpInterceptor

Logs request and response summaries via SLF4J. The logger name includes the service label (e.g. `"Notion"`).



## JSON serialization

### Why Jackson for serialization

Jackson is the most widely used JSON library in Java, has excellent polymorphic type support (critical for block deserialization), and integrates with Lombok's `@Builder` and Java records.

## See Also

- [Development Setup](development-setup.md) -- build and test commands
- [Testing Guide](testing-guide.md) -- test doubles and patterns
- [Custom Interceptors](custom-interceptors.md) -- writing custom interceptors
