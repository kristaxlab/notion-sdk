# Structured layouts

Compose richer page structure with columns, tables, callouts, code, and table of contents.

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

## Related cookbook pages

- [Adding blocks](02-adding-blocks.md)
- [Rich text and inline formatting](03-rich-text.md)
- [Files and media uploads](08-files-and-media.md)
- [Back to README](../../README.md#cookbook)
