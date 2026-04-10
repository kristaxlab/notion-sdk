# Reading page content

> **Coming soon.** A dedicated DSL for reading and traversing page content is planned.
> This section will be updated once it ships.

In the meantime, you can retrieve a page's metadata and raw block structure using the low-level calls below.

## Retrieve a page

```java
Page page = client.pages().retrieve("page-id");

String id = page.getId();
String lastEdited = page.getLastEditedTime();
```

## Retrieve a page's blocks

```java
BlockList blocks = client.blocks().retrieveChildren("page-id");

for (Block block : blocks.getResults()) {
    System.out.println(block.getType() + ": " + block.getId());
}
```

## Retrieve a single block

```java
Block block = client.blocks().retrieve("block-id");
```

## See also

- [Updating & managing blocks](updating-blocks.md) — update, delete, restore blocks
- [Page lifecycle](page-lifecycle.md) — update, archive, restore, move pages
