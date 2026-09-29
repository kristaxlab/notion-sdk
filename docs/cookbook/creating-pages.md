# Creating pages

Create pages with `PagesEndpoint` (`client.pages()`), from a minimal page to a page with initial content.

This page uses fluent helpers from `NotionBlocks` and `NotionText`. Use static imports in examples for readability:

```java
import static io.kristaxlab.notion.fluent.NotionBlocks.*;
import static io.kristaxlab.notion.fluent.NotionText.*;
```

## Create a minimal page

```java
Page page = client.pages().create(p -> p
    .underPage("parent-page-id")
    .title("Weekly Notes"));
```

## Create a page with icon and cover

```java
Page page = client.pages().create(p -> p
    .underPage("parent-page-id")
    .title("Project Aurora")
    .icon("🚀")
    .cover("https://images.unsplash.com/photo-1518770660439-4636190af475"));
```

## Create a page with initial content (fluent DSL)

```java
Page page = client.pages().create(p -> p
    .underPage("parent-page-id")
    .title("Release checklist")
    .children(content -> content
        .heading2("Before deploy")
        .todo("Run integration tests")
        .todo("Check feature flags")
        .paragraph(plainText("Owner: "), bold("Platform Team"))
        .callout("💡", "Use rollback plan for production changes")));
```

## Build params first, create later

```java
CreatePageParams params = CreatePageParams.builder()
    .inPage("parent-page-id")
    .title("Retro")
    .children(content -> content
        .heading2("What went well")
        .bullets("Fast feedback loop", "Clean release process")
        .heading2("What to improve")
        .bullets("Earlier QA involvement"))
    .build();

Page page = client.pages().create(params);
```

## Place a new page among siblings

When the parent is a block, set `position` on the create builder (`pageStart`, `pageEnd`, or
`afterBlock`):

```java
Page page = client.pages().create(p -> p
    .inPage("parent-page-id")
    .title("Pinned note")
    .position(Position.pageStart()));
```

## Create a large markdown page asynchronously

`createAsync` is markdown-only for now. It returns an async task; poll until the write finishes:

```java
AsyncTask started = client.pages().createAsync(p -> p
    .inPage("parent-page-id")
    .title("Migration plan")
    .markdown("# Large body..."));

AsyncTask done = AsyncTaskPoller.awaitTerminal(
    client, started.getId(), PollingConfig.ofTimeout(Duration.ofMinutes(5)));
```

## Related cookbook pages

- [Adding blocks](adding-blocks.md)
- [Structured layouts](structured-layouts.md)
- [Page properties and pagination](page-properties.md)
- [Updating pages](updating-pages.md)
- [Back to README](../../README.md#cookbook)
