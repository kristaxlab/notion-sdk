# Notion SDK for Java

A typed Java client for the [Notion API](https://developers.notion.com/). Java 17+. The main focus of this SDK is clear, fluent and 
well-documented APIs for common operations.

## Installation

**Gradle**

```groovy
implementation 'io.github.kristaxlab:notion-sdk:0.1.0'
```

**Maven**

```xml

<dependency>
    <groupId>io.github.kristaxlab</groupId>
    <artifactId>notion-sdk</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Create a client

```java
NotionClient client = NotionClient.forToken("ntn_abc123...");
```

Defaults: Notion API version `2026-03-11`, base URL `https://api.notion.com/v1`, OkHttp with
30-second timeouts, Jackson for JSON.

## Append content to a page

```java
import static io.kristaxlab.notion.fluent.NotionBlocks.*;

client.blocks().appendChildren("page-id",
    content -> content
        .heading1("Project Aurora — Q2 Review")
        .todo("Set up project dashboard")
        .todo("Gather metrics")
        .todo("Share with team"));
```

## Create a page with content

```java
import static io.kristaxlab.notion.fluent.NotionBlocks.*;

client.pages().create(page -> page
    .underPage("parent-page-id")
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

- `io.kristaxlab.notion.http`
- `io.kristaxlab.notion.http.base.interceptor.LoggingHttpInterceptor`

## Cookbook

Task-oriented recipes for creating, reading and updating Notion content, from page creation and the
fluent block DSL through property pagination, file uploads and end-to-end workflows.

**→ [Browse the cookbook](docs/cookbook/README.md)**

## Documentation

| Document | What it covers |
| --- | --- |
| [Cookbook](docs/cookbook/README.md) | Task-oriented recipes with runnable examples |
| [CONTEXT.md](CONTEXT.md) | Glossary — the terminology used across all docs and Javadoc |
| [Authentication](docs/authentication.md) | Token providers and auth configuration |
| [Error handling](docs/error-handling.md) | Full exception hierarchy and retry guidance |
| [Architecture decisions](docs/adr/README.md) | Why the models look the way they do |
| [Internals](docs/internals/architecture.md) | HTTP pipeline, interceptors, testing, releases — for contributors |
| [Documentation guide](docs/internals/documentation-guide.md) | Where each kind of knowledge belongs, and how docs are maintained |

## License

[PolyForm Shield License 1.0.0](https://polyformproject.org/licenses/shield/1.0.0/)

You may use this SDK freely for any purpose — including in commercial applications — except to provide a product that competes with this SDK itself. See [LICENSE](LICENSE) for the full terms.
