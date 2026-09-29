# Updating blocks

Update, delete, and restore content blocks with the `BlocksEndpoint` (`client.blocks()`).

This page uses `NotionText` fluent helpers for concise rich-text updates. Use static import:

```java
import static io.kristaxlab.notion.fluent.NotionText.*;
```

## Retrieve a block

```java
Block block = client.blocks().retrieve("block-id");
```

## Retrieve a page's blocks

```java
BlockList blocks = client.blocks().retrieveChildren("page-id");
```

## Update paragraph text

Prefer fluent factories with a `Consumer` for a typed partial payload:

```java
import static io.kristaxlab.notion.fluent.NotionBlocks.paragraph;
import static io.kristaxlab.notion.fluent.NotionText.*;

client.blocks().update(
    "block-id",
    paragraph(p -> p.text(plainText("Status: "), green("updated").bold()))
);
```

Or retrieve, mutate, and pass the typed block:

```java
Block block = client.blocks().retrieve("block-id");

if (block instanceof ParagraphBlock paragraph) {
  paragraph.getParagraph().setRichText(List.of(
      plainText("Status: "),
      green("updated").bold()
  ));
  client.blocks().update("block-id", paragraph);
}
```

When the block id is already on the payload, use the single-argument overload:

```java
Block patch = paragraph(p -> p.text(plainText("updated")));
patch.setId("block-id");
client.blocks().update(patch);
```

## Update a to-do checked state

```java
import static io.kristaxlab.notion.fluent.NotionBlocks.todo;

client.blocks().update("todo-block-id", todo(t -> t.text("done").checked(true)));
```

## Delete and restore a block

```java
client.blocks().delete("block-id");
client.blocks().restore("block-id");
```

## Related cookbook pages

- [Adding blocks](adding-blocks.md)
- [Reading page content](reading-content.md)
- [Updating pages](updating-pages.md)
- [Meeting notes](meeting-notes.md)
- [Comments](comments.md)
- [Back to README](../../README.md#cookbook)
