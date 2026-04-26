# Reading page content

Read page metadata and blocks with `PagesEndpoint` and `BlocksEndpoint`, then use `NotionBlocksViewer` for fluent filtering/traversal.

## Retrieve page metadata

```java
Page page = client.pages().retrieve("page-id");

String pageId = page.getId();
String lastEdited = page.getLastEditedTime();
```

## Retrieve page blocks

```java
BlockList blockList = client.blocks().retrieveChildren("page-id");

for (Block block : blockList.getResults()) {
  System.out.println(block.getType() + " -> " + block.getId());
}
```

## Paginate children

```java
BlockList firstPage = client.blocks().retrieveChildren("page-id", null, 50);
BlockList nextPage = client.blocks().retrieveChildren("page-id", firstPage.getNextCursor(), 50);
```

## Read content fluently with `NotionBlocksViewer`

```java
BlockList response = client.blocks().retrieveChildren("page-id");
NotionBlocksViewer viewer = NotionBlocksViewer.of(response);

List<String> uncheckedTodos = viewer.todos().unchecked().plainTextList();
List<String> links = viewer.flatten().links();
String allText = viewer.flatten().textual().plainText("\n");
```

## Read a page as Markdown

```java
PageAsMarkdown markdown = client.pages().retrieveAsMarkdown("page-id");
System.out.println(markdown.getMarkdown());
```

## Related cookbook pages

- [Adding blocks](02-adding-blocks.md)
- [Updating blocks](07-updating-blocks.md)
- [End-to-end recipes](09-end-to-end-recipes.md)
- [Back to README](../../README.md#cookbook)
