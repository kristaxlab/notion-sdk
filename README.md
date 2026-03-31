# Notion SDK for Java

This guide walks you through installing the Notion SDK for Java and making your first API call.

## Prerequisites

- **Java 17** or later
- A **Notion integration token** -- create one at [notion.so/my-integrations](https://www.notion.so/my-integrations)
- **Gradle** or **Maven** for dependency management

## Installation

### Gradle

```groovy
implementation 'TODO'
```

### Maven

```xml
<dependency>
  <groupId>TODO</groupId>
  <artifactId>notion-sdk</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Create a Client

The simplest way to create a client is with a token:

```java
import io.kristixlab.notion.api.NotionClient;

NotionClient client = NotionClient.forToken("secret_abc123...");
```

This creates a fully-wired client with all defaults: Notion API version `2026-03-11`, base URL `https://api.notion.com/v1`, OkHttp with 30-second timeouts, and Jackson for JSON serialization.

For more control, use the builder:

```java
NotionClient client = NotionClient.builder()
    .authToken("secret_abc123...")
    .version("2026-03-11")
    .baseUrl("https://api.notion.com/v1")
    .build();
```

See [Configuration](configuration.md) for the full list of options.

## Make Your First API Call

### Retrieve a page

> **Status: Planned** -- The high-level `pages()` API shown below is not yet implemented.

```java
Page page = client.pages().get("your-page-id");
System.out.println(page.getTitle());
```

### Query a database

> **Status: Planned**

```java
DatabaseQuery query = DatabaseQuery.builder()
    .filter(Filter.property("Status").select().equals("In Progress"))
    .sort(Sort.property("Created").descending())
    .build();

PaginatedList<Page> results = client.databases().query("your-db-id", query);

for (Page page : results) {
    System.out.println(page.getTitle());
}
```

### Append blocks to a page

> **Status: Planned**

```java
List<Block> children = List.of(
    ParagraphBlock.of("Hello from the Notion SDK for Java!"),
    HeadingTwoBlock.of("Section Title"),
    ToDoBlock.of("Buy groceries", false)
);

client.blocks().children().append("your-page-id", children);
```

## Error Handling

The SDK automatically maps Notion API errors to typed exceptions:

```java
try {
    String page = client.getHttpClient().call("GET",
        ApiPath.from("/pages/nonexistent-id"), String.class);
} catch (NotFoundException e) {
    System.out.printf("Page not found: %s, requestId: %s ", e.getMessage(), e.getRequestId());
} catch (NotionApiException e) {
    System.out.printf("API error %s, %s", e.getStatus(), e.getMessage());
}
```

## What You Can Do

| Capability | Guide |
|---|---|
| Create, read, update, and archive pages | [Pages](docs/guides/pages.md) |
| Create and query databases, manage schemas | [Databases](docs/guides/databases.md) |
| Append, read, update, and delete block content | [Blocks](docs/guides/blocks.md) |
| List workspace users | [Users](docs/guides/users.md) |
| Full-text search across your workspace | [Search](docs/guides/search.md) |
| Create and read comments | [Comments](docs/guides/comments.md) |
| Upload and reference files | [Files & Media](docs/guides/files-and-media.md) |
| Compose and parse rich text | [Rich Text](docs/guides/rich-text.md) |
| Cursor-based pagination | [Pagination](docs/guides/pagination.md) |

## Configuration at a Glance

```java
NotionClient client = NotionClient.builder()
    .authToken("secret_abc123...")          // required
    .version("2026-03-11")                 // default
    .baseUrl("https://api.notion.com/v1")  // default
    .writeExchangeTo(Path.of("logs"))      // optional, off by default
    .build();
```

| Option                      | Default | Description              |
|-----------------------------|---|--------------------------|
| `authToken(String)`         | *required* | Notion integration token |
| `version(String)`           | `"2026-03-11"` | `Notion-Version` header  |
| `baseUrl(String)`           | `"https://api.notion.com/v1"` | API base URL             |
| `rawHttpClient(HttpClient)` | OkHttp, 30 s timeouts | Custom HTTP transport    |
| `writeExchangeTo(Path)`     | disabled | Write rq/rs JSON to disk |

Full details: [Configuration Reference](docs/configuration.md)

## Advanced Topics

Deeper dives into the SDK internals and extension points:

- [Error Handling](advanced/error-handling.md) -- exception hierarchy, retry strategies, rate limits
- [Custom Interceptors](advanced/custom-interceptors.md) -- architecture, writing custom interceptors
- [Exchange Recording](advanced/exchange-recording.md) -- debug with request/response JSON files
- [Custom HTTP Client](advanced/custom-http-client.md) -- replace OkHttp with your own http client implementation
- [Notion Http Client](advanced/notion-http-client.md) -- use `NotionHttpClient` directly


## Recipes

End-to-end examples for common integration cases:

- [Sync External Data to Notion](docs/recipes/sync-external-to-notion.md)
- [Notion as CMS](docs/recipes/notion-as-cms.md)
- [Task Automation](docs/recipes/task-automation.md)
- [Bulk Operations](docs/recipes/bulk-operations.md)
- [Webhooks](docs/recipes/webhooks.md)

## Contributing

See the [Development Setup](docs/contributing/development-setup.md) guide to get started, and read the [Architecture Overview](docs/contributing/architecture.md) to understand the codebase.

- [Development Setup](docs/contributing/development-setup.md) -- clone, build, test, format
- [Architecture](docs/contributing/architecture.md) -- package design, pipeline, extension points
- [Testing Guide](docs/contributing/testing-guide.md) -- unit vs integration tests, test doubles
- [Release Process](docs/contributing/release-process.md) -- versioning, publishing, changelog

## License

<!-- TODO: choose license -->
TBD
