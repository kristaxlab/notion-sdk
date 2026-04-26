# Custom HTTP Client

The SDK's transport layer is abstracted behind the `HttpClient` interface. By default, `OkHttp3Client` is used, but you can replace it with any implementation for integration with your existing HTTP infrastructure.

## The `HttpClient` Interface

```java
public interface HttpClient {
    HttpResponse send(HttpRequest request) throws IOException;
}
```

The `HttpRequest` and `HttpResponse` records, along with the `Body` sealed hierarchy, provide a library-agnostic representation of HTTP exchanges.

### Body Types

```
Body (sealed)
  ├── EmptyBody
  ├── StringBody(content, contentType)
  ├── BytesBody(bytes, contentType)
  ├── FileBody(file, contentType)
  ├── InputStreamBody(inputStream, contentType, contentLength)
  └── MultipartBody(parts: List<Part>)
        Part (sealed)
          ├── FieldPart(name, value)
          └── FilePart(name, filename, body)
```

## Replacing the Transport

Use `NotionClientBuilder.rawHttpClient()` to supply a custom transport. The interceptor pipeline and error handler are still layered on top:

```java
HttpClient myTransport = new MyCustomHttpClient();

NotionClient client = NotionClient.builder()
    .authToken("ntn_abc123...")
    .rawHttpClient(myTransport)
    .build();
```

## Customizing OkHttp Timeouts

The most common customization is adjusting timeouts:

```java
import client.base.http.io.kristaxlab.notion.OkHttp3Client;
import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

OkHttpClient ok = new OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)   // longer for large queries
    .writeTimeout(60, TimeUnit.SECONDS)
    .build();

NotionClient client = NotionClient.builder()
    .authToken("ntn_abc123...")
    .rawHttpClient(new OkHttp3Client(ok))
    .build();
```

## Adding OkHttp Interceptors

If you prefer OkHttp-native interceptors alongside the SDK pipeline:

```java
OkHttpClient ok = new OkHttpClient.Builder()
    .addInterceptor(chain -> {
        System.out.println("OkHttp: " + chain.request().url());
        return chain.proceed(chain.request());
    })
    .build();

NotionClient client = NotionClient.builder()
    .authToken("ntn_abc123...")
    .rawHttpClient(new OkHttp3Client(ok))
    .build();
```


## Implementing a Custom HttpClient

To implement `HttpClient` for a different HTTP library (e.g. java.net.http, Apache HttpClient):

```java
public class JdkHttpClient implements HttpClient {

    private final java.net.http.HttpClient client =
        java.net.http.HttpClient.newHttpClient();

    @Override
    public HttpResponse send(HttpRequest request) throws IOException {
        java.net.http.HttpRequest.Builder builder =
            java.net.http.HttpRequest.newBuilder()
                .uri(URI.create(request.url()))
                .method(request.method().name(), toBodyPublisher(request.body()));

        request.headers().forEach(builder::header);

        try {
            var response = client.send(builder.build(),
                java.net.http.HttpResponse.BodyHandlers.ofString());

            return new HttpResponse(
                response.statusCode(),
                toHeaderMap(response.headers()),
                response.body()
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }

    // ... helper methods for body/header conversion
}
```

## Pipeline Assembly

When you provide a custom `rawHttpClient`, the builder still wraps it:

```
  ErrorHandlingHttpClient
    └── InterceptingHttpClient
          ├── NotionVersionInterceptor
          ├── NotionAuthInterceptor
          ├── ExchangeRecordingInterceptor (if enabled)
          └── LoggingHttpInterceptor
          └── ** Your custom HttpClient **
```

## See Also

- [Create a client](../../README.md#create-a-client) -- `rawHttpClient()` builder option
- [Interceptors](architecture.md#interceptors) -- the pipeline that wraps your transport
- [Testing Guide](testing-guide.md) -- MockWebServer patterns
