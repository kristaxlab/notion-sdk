# Custom Interceptors

The SDK uses a composable interceptor pipeline to handle cross-cutting concerns like authentication, API versioning, logging, etc.

### The `HttpClientInterceptor` Interface

```java
public interface HttpClientInterceptor {

    HttpRequest beforeSend(HttpRequest request);

    void afterReceive(HttpRequest request, HttpResponse response);
  
}
```

- `beforeSend` is called before the request is sent. Use `request.toBuilder().<..>.build()` to modify the request.

- `afterReceive` is called after the response is received, regardless of status code.

## Custom Interceptor Example

```java
public class TimingInterceptor implements HttpClientInterceptor {

    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Override
    public HttpRequest beforeSend(HttpRequest request) {
        startTime.set(System.nanoTime());
        return request;
    }

    @Override
    public void afterReceive(HttpRequest request, HttpResponse response) {
        long elapsed = System.nanoTime() - startTime.get();
        startTime.remove();
        System.out.printf("%s %s -> %d in %d ms%n",
            request.method(), request.url(),
            response.statusCode(),
            TimeUnit.NANOSECONDS.toMillis(elapsed));
    }
}
```

## Registering Custom Interceptors

> **Status: Planned** -- Custom interceptor registration via the builder is not yet exposed.

```java
// Planned API
NotionClient client = NotionClient.builder()
    .authToken("secret_xxx")
    .addInterceptor(new TimingInterceptor())
    .build();
```

Currently, custom interceptors require building the pipeline manually. See [Custom HTTP Client](custom-http-client.md) for how to compose the pipeline yourself.

## See Also

- [Interceptors](architecture.md#interceptors) -- internal details of interceptors implementation
- [Exchange Logging](exchange-logging.md) -- the built-in exchange recording interceptor
- [Error Handling](../error-handling.md) -- how error mapping fits outside the interceptor chain
- [Custom HTTP Client](custom-http-client.md) -- full pipeline customization
