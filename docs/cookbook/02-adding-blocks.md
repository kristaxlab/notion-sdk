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

```java
import io.kristaxlab.notion.model.common.Position;

client.blocks().appendChildren(
    "page-id",
    () -> List.of(callout("⚠️", "Draft content")),
    Position.pageStart()
);

client.blocks().appendChildren(
    "page-id",
    () -> List.of(divider(), paragraph("Inserted after a known block")),
    Position.afterBlock("existing-block-id")
);
```

## Related cookbook pages

- [Creating pages](01-creating-pages.md)
- [Rich text and inline formatting](03-rich-text.md)
- [Updating blocks](07-updating-blocks.md)
- [Back to README](../../README.md#cookbook)
