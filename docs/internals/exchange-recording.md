# Exchange Recording

The `ExchangeRecordingInterceptor` writes every HTTP exchange (request + response) as a pair of pretty-printed JSON
files to a directory you specify. This is invaluable for debugging and testing purpose.

## Enabling Exchange Logging

```java
import java.nio.file.Path;

NotionClient client = NotionClient.builder()
        .authToken("ntn_abc123...")
        .writeExchangeTo(Path.of("exchanges"))
        .build();
```

The directory is created automatically if it does not exist. Pass `null` or skip interacting with '
writeExchangeTo' method to leave it disabled.

## File Naming

Each exchange produces two files with a shared prefix:

```
<epoch_ms>_<thread_id>_<service>_rq.json   -- request
<epoch_ms>_<thread_id>_<service>_rs.json   -- response
```

The `<service>` segment is derived from the URL path and HTTP method. For example:

| Request                            | Service name               |
|------------------------------------|----------------------------|
| `GET /v1/pages/abc-123`            | `pages_retrieve`           |
| `POST /v1/databases/abc-123/query` | `databases_query_create`   |
| `PATCH /v1/blocks/abc-123`         | `blocks_update`            |
| `GET /v1/blocks/abc-123/children`  | `blocks_children_retrieve` |
| `GET /v1/users`                    | `users_retrieve`           |

UUID-like path segments are automatically stripped from the service name.

## Request File Format (`*_rq.json`)

```json
{
  "method": "GET",
  "url": "https://api.notion.com/v1/pages/abc-123",
  "requestHeaders": {
    "Notion-Version": "2026-03-11",
    "Authorization": "[redacted]",
    "Accept": "application/json"
  },
  "requestBody": null
}
```

**Security:** The `Authorization` header value is always replaced with `[redacted]`.

For POST/PATCH requests with a JSON body, the `requestBody` field contains the parsed JSON object (not a string), making
it readable:

```json
{
  "method": "POST",
  "url": "https://api.notion.com/v1/databases/abc-123/query",
  "requestHeaders": {
    "...": "..."
  },
  "requestBody": {
    "filter": {
      "property": "Status",
      "select": {
        "equals": "In Progress"
      }
    }
  }
}
```

## Response File Format (`*_rs.json`)

```json
{
  "statusCode": 200,
  "responseHeaders": {
    "Content-Type": [
      "application/json; charset=utf-8"
    ],
    "X-Request-Id": [
      "req-abc-123"
    ]
  },
  "responseBody": {
    "object": "page",
    "id": "abc-123",
    "...": "..."
  }
}
```

## Test Fixture Pattern

Exchange files make excellent test fixtures. Record real API responses, then replay them in tests:

```java
// In your test setup, record exchanges to a test-specific directory
Path fixtureDir = Path.of("src/test/resources/fixtures",
                getClass().getSimpleName(),
                testInfo.getTestMethod().get().getName());

NotionClient client = NotionClient.builder()
        .authToken(System.getenv("NOTION_TOKEN"))
        .exchangeLogging(fixtureDir)
        .build();

// Run the test against the real API
// The response JSON files become your test fixtures
```

## Error Handling

Write failures are logged at WARN level and never propagate -- exchange logging never breaks your API calls.

## See Also

- [Interceptors](architecture.md#interceptors) -- where exchange logging sits in the pipeline
- [Testing Guide](testing-guide.md) -- using exchange files as test fixtures
