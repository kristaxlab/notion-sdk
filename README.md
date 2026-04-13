# Notion SDK for Java

A typed Java client for the [Notion API](https://developers.notion.com/). Java 17+.

## Installation

**Gradle**

```groovy
implementation 'TODO'
```

**Maven**

```xml
<dependency>
  <groupId>TODO</groupId>
  <artifactId>notion-sdk</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Create a client

```java
NotionClient client = NotionClient.forToken("secret_abc123...");
```

Defaults: Notion API version `2026-03-11`, base URL `https://api.notion.com/v1`, OkHttp with
30-second timeouts, Jackson for JSON. See [Configuration](docs/configuration.md) for all options.

## Append content to a page

```java


client.blocks().

appendChildren("page-id",
               content ->content
        .

heading1("Project Aurora — Q2 Review")
        .

todo("Set up project dashboard")
        .

todo("Gather metrics")
        .

todo("Share with team"));
```

## Create a page with content

```java


client.pages().

create(page ->page
        .

inPage("parent-page-id")
    .

title("Error Handling Best Practices")
    .

icon("🛡️")
    .

children(content ->content
        .

heading2("Principles")
        .

numbered("Fail fast — detect errors at the boundary.")
        .

numbered("Be specific — throw the most precise exception type.")
        .

numbered("Log once — avoid duplicate log entries.")
        .

divider()
        .

callout("💡","Use typed exceptions from the SDK's exception hierarchy.")));
```

## Error handling

Every Notion API error maps to a typed exception:

```java
try {
    Page page = client.pages().retrieve("page-id");
} catch (NotFoundException e) {
    System.out.printf("Not found — request ID: %s%n", e.getRequestId());
} catch (TooManyRequestsException e) {
    // back off and retry
} catch (NotionApiException e) {
    System.out.printf("API error %d: %s%n", e.getStatus(), e.getMessage());
}
```

See [Error Handling](docs/advanced/error-handling.md) for the full exception hierarchy.

## Resources

- [Cookbook](docs/cookbook/) — recipes for pages, blocks, rich text, layouts
- [Configuration](docs/configuration.md) — timeouts, logging, custom HTTP clients
- [Advanced Topics](docs/advanced/) — interceptors, exchange recording, custom HTTP client
- [Contributing](docs/contributing/)

## License

<!-- TODO: choose license -->
The MIT License
