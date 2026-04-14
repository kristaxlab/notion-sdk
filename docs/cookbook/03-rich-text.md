# Rich text and inline formatting

Build `RichText` with `NotionText` helpers, then plug it into block helpers from `NotionBlocks`.

This page uses fluent helpers from `NotionBlocks` and `NotionText`. Use static imports in examples for readability:

```java
import static io.kristaxlab.notion.fluent.NotionBlocks.*;
import static io.kristaxlab.notion.fluent.NotionText.*;
```

## Basic rich text runs

```java
RichText normal = plainText("Normal");
RichText strong = bold("Important");
RichText mono = code("System.out.println()");
RichText link = url("https://developers.notion.com/");
```

## Colors and combined styles

```java
RichText success = green("DONE");
RichText warning = yellowBackground(bold("ATTENTION"));
RichText subtle = gray(italic("optional"));
```

## Compose a mixed paragraph

```java
client.blocks().appendChildren("page-id", paragraph(
    plainText("Build status: "),
    green("PASS").bold(),
    plainText(" | "),
    plainText("Coverage: "),
    blue("86%")
));
```

## Use `textBuilder()` for longer inline text

```java
List<RichText> text = textBuilder()
    .plainText("Owner: ")
    .plainText("Alice").bold()
    .plainText(" | Due: ")
    .plainText("2026-05-01").blue()
    .build();

client.blocks().appendChildren("page-id", paragraph(text));
```

## Mentions

```java
client.blocks().appendChildren("page-id", paragraph(
    plainText("Assigned to "),
    userMention("user-id"),
    plainText(", target "),
    dateMention("2026-05-01", "2026-05-15")
));
```

## Related cookbook pages

- [Adding blocks](02-adding-blocks.md)
- [Structured layouts](04-structured-layouts.md)
- [End-to-end recipes](09-end-to-end-recipes.md)
- [Back to README](../../README.md#cookbook)
