# Text Formatting and Styles

Build styled text for paragraphs, headings, list items, and any other block that accepts rich text. The examples 
below use static helper methods from `NotionText` for brevity, but you can also construct `RichText` objects directly.

```java
import static io.kristaxlab.notion.fluent.NotionText.*;
```

## Plain text

```java
RichText text = plainText("Hello, Notion!");
```

## Inline formatting

```java
RichText boldText        = bold("Important");
RichText italicText      = italic("emphasis");
RichText underlineText   = underline("underlined");
RichText strikeText      = strikethrough("outdated");
RichText inlineCode      = code("System.out.println()");
RichText linkedText      = url("https://notion.so");
```

## Colors

```java
RichText blueText        = blue("info");
RichText redText         = red("error");
RichText greenText       = green("success");
RichText highlighted     = yellowBackground("highlight");
```

All Notion colors are available as static helpers on `NotionText`.

## Combine styles

Call helpers on the result of another helper to combine styles:

```java
RichText boldBlue = bold("Warning").blue();   // RichText is mutable
```

## Compose a mixed-format paragraph

Pass multiple `RichText` objects to any block factory:

```java
ParagraphBlock para = paragraph(
    plainText("This is "),
    bold("bold"),
    plainText(", this is "),
    italic("italic and ").blue(),
    code("monospace"),
    plainText(".")
);

client.blocks().appendChildren("page-id", para);
```

## Use NotionTextBuilder for a fluent chain

`NotionTextBuilder` lets you compose a list of rich text runs in a single expression.
Use it when there are many runs, or when you want to attach the list to a block builder.

```java
client.blocks().appendChildren(
         "page-id",
         content -> content.paragraph(
              t -> t.text(
                    textBuilder()
                        .plainText("Status: ")
                        .plainText("DONE").green().bold()
                        .build()
    ))
);
```

## Mentions

```java
RichText userRef  = userMention("user-id");
RichText dateRef  = dateMention("2026-06-01");
RichText dateRange = dateMention("2026-06-01", "2026-06-30");
RichText pageRef  = blockMention("page-or-block-id");
```

## See also

- [Adding blocks](blocks.md) — append blocks that contain rich text
- [Structured layouts](structured-layouts.md) — block types that support rich text
- [Back to README](../../README.md#cookbook)