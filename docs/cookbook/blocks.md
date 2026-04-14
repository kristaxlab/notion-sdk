# Adding Blocks

Append blocks to an existing page or block. This is the most common Notion SDK operation. All the examples implies
a static import of `io.kristixlab.notion.api.model.helper.NotionBlocks.*` for block factory methods.

```java
import static io.kristixlab.notion.api.model.helper.NotionBlocks.*;
```

## Append a single block

```java
client.blocks().appendChildren("page-id", paragraph("Hello, Notion!"));
```

## Append a list of blocks

```java
client.blocks().appendChildren("page-id", List.of(
    heading2("Today's plan"),
    bullet("Review pull requests"),
    bullet("Update documentation"),
    bullet("Deploy to staging")
));
```

## Append blocks with the builder

The builder lets you compose a sequence of blocks inline without constructing a list manually.

```java
client.blocks().appendChildren("page-id",
    content -> content
        .heading1("Sprint 12 — Kickoff")
        .divider()
        .heading2("Goals")
        .numbered("Ship the new onboarding flow")
        .numbered("Close all P1 bugs")
        .divider()
        .heading2("Blockers")
        .callout("🚧", "Waiting on API credentials from the platform team."));
```

## Mix pre-built blocks with the builder

Use `block()` or `blocks()` to insert individually constructed blocks inside a builder chain.

```java
List<Block> importantItems = List.of(
    todo("Define CI pipeline"),
    todo("Write integration tests")
);

client.blocks().appendChildren("page-id",
    content -> content
        .heading1("GitHub project")
        .blocks(importantItems)
        .divider()
        .paragraph("See the project board for full details."));
```

## Insert at a specific position

By default, blocks are appended at the end. Use `Position` to place them elsewhere.

```java
import common.model.io.kristaxlab.notion.Position;

// Insert at the very beginning of the page
client.blocks().appendChildren("page-id",
    callout("⚠️", "This page is under construction."),
    Position.pageStart());

// Insert immediately after a known block
client.blocks().appendChildren("page-id",
    divider(),
    Position.afterBlock("block-id"));
```

## See also

- [Updating & managing blocks](updating-blocks.md) — update, delete, restore
- [Structured layouts](structured-layouts.md) — columns, tables, callouts
- [Rich text & formatting](rich-text.md) — styled text inside blocks
- [Back to README](../../README.md#cookbook)
