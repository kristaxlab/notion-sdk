# Notion SDK for Java

A typed Java client for the [Notion API](https://developers.notion.com/). Java 17+. The main focus of this SDK is clear, fluent and 
well-documented APIs for common operations.

## Installation

**Gradle**

```groovy
implementation 'io.github.kristaxlab.notion'
```

**Maven**

```xml

<dependency>
    <groupId>io.github.kristaxlab.notion</groupId>
    <artifactId>notion-sdk</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Create a client

```java
NotionClient client = NotionClient.forToken("secret_abc123...");
```

Defaults: Notion API version `2026-03-11`, base URL `https://api.notion.com/v1`, OkHttp with
30-second timeouts, Jackson for JSON.

## Append content to a page

```java
import static io.kristixlab.notion.api.model.helper.NotionBlocks.*;

client.blocks().appendChildren("page-id",
    content -> content
        .heading1("Project Aurora — Q2 Review")
        .todo("Set up project dashboard")
        .todo("Gather metrics")
        .todo("Share with team"));
```

## Create a page with content

```java
import static io.kristixlab.notion.api.model.helper.NotionBlocks.*;

client.pages().create(page -> page
    .inPage("parent-page-id")
    .title("Error Handling Best Practices")
    .icon("🛡️")
    .children(content -> content
        .heading2("Principles")
        .numbered("Fail fast — detect errors at the boundary.")
        .numbered("Be specific — throw the most precise exception type.")
        .numbered("Log once — avoid duplicate log entries.")
        .divider()
        .callout("💡", "Use typed exceptions from the SDK's exception hierarchy.")));
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

See [Error Handling](docs/error-handling.md) for the full exception hierarchy.

## Logging

Notion SDK requires an SLF4J implementation for logging. To see requests and responses in logs, set logging level to
DEBUG for any of the following packages:

- `io.github.kristaxlab.notion.http`
- `io.kristaxlab.notion.http.base.interceptor.LoggingHttpInterceptor`

## Cookbook

Examples of common tasks and patterns:

#### Page content:

- [Adding Blocks](docs/cookbook/adding-blocks.md)
- [Text Formatting and Styles](docs/cookbook/rich-text.md)
- [Structured Layout: columns, tables, tabs, etc.](docs/cookbook/structured-layouts.md)
- [Reading page content](cookbook/cookbook/)
- [Page content as markdown](cookbook/cookbook/)

#### Files and media:

- [Uploding and embedding files (images, video, pdf files, etc.](cookbook/cookbook/)

## License

The MIT License
