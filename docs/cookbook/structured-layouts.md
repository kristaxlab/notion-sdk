# Structured layouts

Compose richer page structure with columns, tables, callouts, code, table of contents, and tab
blocks.

This page uses fluent helpers from `NotionBlocks` and `NotionText`. Use static imports in examples for readability:

```java
import static io.kristaxlab.notion.fluent.NotionBlocks.*;
import static io.kristaxlab.notion.fluent.NotionText.*;
```

## Columns

```java
client.blocks().appendChildren("page-id", content -> content.columns(
    left -> left.heading2("To do").todos("Write tests", "Update docs"),
    right -> right.heading2("Done").bullets("Set up CI", "Code review")
));
```

## Unequal columns

```java
client.blocks().appendChildren("page-id", content -> content.columns(
    column(0.30, heading3("Summary"), paragraph("Short status")),
    column(0.70, heading3("Details"), paragraph("Longer explanation"))
));
```

## Tables

```java
client.blocks().appendChildren("page-id", table(
    tableRow(bold("Name"), bold("Role"), bold("Status")),
    tableRow("Alice", "Engineer", "Active"),
    tableRow("Bob", "Designer", "On leave")
));
```

## Callouts and code blocks

```java
client.blocks().appendChildren("page-id", List.of(
    callout("💡", "Prefer fluent builders for long, nested content"),
    code("java", "NotionClient client = NotionClient.forToken(\"ntn_xxx\");")
));
```

## Table of contents

```java
client.blocks().appendChildren("page-id", tableOfContents());
```

## Tab block

A tab block holds one or more tabs. Each direct child must be a paragraph: that paragraph is the
tab label (`rich_text`, optional icon, and color). Nested children under the paragraph are the
content shown when that tab is selected.

```java
import static io.kristaxlab.notion.fluent.NotionBlocks.tab;
import io.kristaxlab.notion.model.block.ParagraphBlock;
import io.kristaxlab.notion.model.common.Icon;

client.blocks().appendChildren("page-id", tab(
    ParagraphBlock.builder()
        .text("Overview")
        .icon(Icon.emoji("📋"))
        .children(c -> c.paragraph("Tab 1 content"))
        .build(),
    ParagraphBlock.builder()
        .text("Details")
        .icon(Icon.emoji("🔍"))
        .children(c -> c.paragraph("Tab 2 content"))
        .build()
));
```

On retrieve, the type-named `tab` object is empty; list the tab labels with
`blocks().retrieveChildren(tabBlockId)`, then each label's panel content with
`retrieveChildren(paragraphId)`.

## Related cookbook pages

- [Adding blocks](adding-blocks.md)
- [Rich text and inline formatting](rich-text.md)
- [Files and media uploads](files-and-media.md)
- [Meeting notes](meeting-notes.md)
- [Back to README](../../README.md#cookbook)
