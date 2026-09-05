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

## Update a to-do checked state

```java
Block block = client.blocks().retrieve("todo-block-id");

if (block instanceof ToDoBlock todo) {
  todo.getToDo().setChecked(true);
  client.blocks().update("todo-block-id", todo);
}
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
- [Back to README](../../README.md#cookbook)
