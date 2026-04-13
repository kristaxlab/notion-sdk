# Updating & managing blocks

Retrieve, update, delete, and restore blocks on a page.

```java

```

## Retrieve a block

```java
Block block = client.blocks().retrieve("block-id");
```

## Retrieve a page's blocks

```java
BlockList page = client.blocks().retrieveChildren("page-id");

for (Block block : page.getResults()) {
    System.out.println(block.getType() + ": " + block.getId());
}
```

Retrieve a specific window of blocks using a cursor and page size:

```java
BlockList page = client.blocks().retrieveChildren("page-id", startCursor, 50);
String nextCursor = page.getNextCursor();
```

## Update a block

Retrieve a block, modify it in place, then send it back. All block types follow the same pattern.

```java
Block block = client.blocks().retrieve("block-id");

if (block instanceof ParagraphBlock paragraph) {
    paragraph.getParagraph().setText(List.of(plainText("Updated paragraph text.")));
    client.blocks().update("block-id", paragraph);
}
```

## Delete a block

Moves the block to the trash. The block still exists and can be restored.

```java
client.blocks().delete("block-id");
```

## Restore a block

```java
client.blocks().restore("block-id");
```

## See also

- [Writing content](writing-content.md) — append new blocks
- [Page lifecycle](page-lifecycle.md) — archive and restore pages
