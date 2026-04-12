# Structured layouts

Columns, tables, callouts, code blocks, and other structural block types.

```java
import static io.kristixlab.notion.api.model.helper.NotionBlocks.*;
import static io.kristixlab.notion.api.model.helper.NotionText.*;
```

## Columns

Columns must come in pairs or more. Use lambdas to describe each column's content inline.

```java
client.blocks().appendChildren("page-id",
    c -> c.columns(
        left  -> left.heading2("To Do").todos("Write tests", "Update docs"),
        right -> right.heading2("Done").bullets("Set up CI", "Code review")
    )
);
```

Pass pre-built block lists if the columns are built separately:

```java
List<Block> leftColumn  = blocksBuilder().heading2("Q1").bullet("Shipped onboarding").build();
List<Block> rightColumn = blocksBuilder().heading2("Q2").bullet("Improve search").build();

client.blocks().appendChildren("page-id", columns(leftColumn, rightColumn));
```

Use `column()` with a width ratio for an unequal split:

```java
client.blocks().appendChildren("page-id",
    c -> c.columns(
        column(0.33, heading2("Narrow"), paragraph("Short note")),
        column(0.67, heading2("Wide"),   paragraph("Detailed content here"))
    )
);
```

## Tables

Build a table by passing rows. All rows must have the same number of cells.

```java
client.blocks().appendChildren("page-id",
    table(
        tableRow("Name",    "Role",      "Status"),
        tableRow("Alice",   "Engineer",  "Active"),
        tableRow("Bob",     "Designer",  "On leave")
    )
);
```

Use `RichText` cells for per-cell formatting:

```java
tableRow(bold("Name"), bold("Role"), bold("Status"))   // header row
```

## Callouts

```java
// With an emoji icon
callout("💡", "Pro tip: use the builder API to keep long block lists readable.");

// Default icon
callout("This page is still a work in progress.");
```

## Code blocks

```java
code("java",
    """
    NotionClient client = NotionClient.forToken("secret_xxx");
    client.blocks().appendChildren("page-id", paragraph("Hello!"));
    """);
```

Or use the builder for a caption:

```java
code(b -> b
    .language("shell")
    .code("./gradlew test")
    .caption("Run all tests"));
```

## Table of contents

Inserts a table of contents block that links to all headings on the page.

```java
client.blocks().appendChildren("page-id", tableOfContents());
```

## See also

- [Writing content](writing-content.md) — append any block to a page
- [Rich text & formatting](rich-text.md) — styled text inside blocks
