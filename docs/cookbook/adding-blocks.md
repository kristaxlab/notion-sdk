# Adding blocks

Append content to an existing page or block using the `BlocksEndpoint` (`client.blocks()`).

This page uses fluent helpers from `NotionBlocks` and `NotionText`. Use static imports in examples for readability:

```java
import static io.kristaxlab.notion.fluent.NotionBlocks.*;
import static io.kristaxlab.notion.fluent.NotionText.*;
```

## Append a single block

```java
client.blocks().appendChildren("page-id", paragraph("Hello, Notion SDK"));
```

## Append a list of blocks

```java
client.blocks().appendChildren("page-id", List.of(
    heading2("Today"),
    bullet("Review PRs"),
    bullet("Update docs"),
    bullet("Deploy staging")
));
```

## Compose content inline with the fluent builder

The lambda-based builder keeps complex content readable without manually building `List<Block>`.

```java
client.blocks().appendChildren("page-id", content -> content
    .heading1("Sprint kickoff")
    .divider()
    .heading2("Goals")
    .numbered("Ship onboarding improvements")
    .numbered("Close P1 bugs")
    .paragraph(plainText("Status: "), green("on track")));
```

## Mix pre-built blocks with builder calls

```java
List<Block> checklist = List.of(
    todo("Set release date"),
    todo("Prepare changelog")
);

client.blocks().appendChildren("page-id", content -> content
    .heading2("Release prep")
    .blocks(checklist)
    .callout("✅", "Ready for PM review"));
```

## Insert blocks at a specific position

Every `appendChildren` overload accepts an optional `Position`: page start, after a known sibling,
or omit / pass `null` to append at the end.

```java
import io.kristaxlab.notion.model.common.Position;

// Single block
client.blocks().appendChildren(
    "page-id",
    callout("⚠️", "Draft content"),
    Position.pageStart()
);

// List
client.blocks().appendChildren(
    "page-id",
    List.of(divider(), paragraph("Inserted after a known block")),
    Position.afterBlock("existing-block-id")
);

// Fluent builder
client.blocks().appendChildren(
    "page-id",
    content -> content.heading2("Inserted section").bullet("Item"),
    Position.afterBlock("existing-block-id")
);

// Lazy supplier
client.blocks().appendChildren(
    "page-id",
    () -> List.of(paragraph("Built just in time")),
    Position.pageStart()
);
```

## Related cookbook pages

- [Creating pages](creating-pages.md)
- [Rich text and inline formatting](rich-text.md)
- [Structured layouts](structured-layouts.md)
- [Updating blocks](updating-blocks.md)
- [Back to README](../../README.md#cookbook)
