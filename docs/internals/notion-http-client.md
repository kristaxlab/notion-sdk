# Using Notion Http Client directly

`NotionClient` exposes a `NotionHttpClient` that lets you call any Notion API endpoint directly using `ApiPath`, while the SDK still handles authentication, API versioning, error mapping, and JSON serialization.

## Getting `NotionHttpClient`

```java
NotionClient client = NotionClient.forToken("secret_abc123...");
NotionHttpClient http = client.getHttpClient();
```

## Call examples

### GET

```java
ApiPath path = ApiPath.from("/users/me");

// Deserialize to a typed model
User user = http.call("GET", path, Page.class);

// Deserialize to String (raw JSON)
String json = http.call("GET", path, String.class);

```


### POST

```java

CreatePageParams body = 
        CreatePageParams.builder()
                        .inDataSource("database_uuid")
                        .title("New Task")
                        .build();

Page page = httpcall("POST", ApiPath.from("/pages"), body, Page.class);

```


## What the Pipeline Handles

You don't need to worry about:

- **`Authorization` header** -- added by `NotionAuthInterceptor`
- **`Notion-Version` header** -- added by `NotionVersionInterceptor`
- **Error responses** -- 4xx/5xx automatically throw `NotionApiException` subclasses
- **JSON serialization/deserialization** -- handled by `ApiClientImpl`
- **URL resolution** -- `ApiPath` is resolved against the configured `baseUrl`

## See Also

- [Create a client](../../README.md#create-a-client) -- base URL and serializer defaults
- [Error Handling](../error-handling.md) -- what happens on 4xx/5xx
- [Interceptors](architecture.md#interceptors) -- what middleware runs on every call
