# Getting Started

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

See [Error Handling](advanced/error-handling.md) for the full exception hierarchy.

## Next Steps

- [Configuration](configuration.md) -- customize notion client
- [Notion SDK Documentation](index.md) -- deep dive into all the SDK capabilities and extension points
