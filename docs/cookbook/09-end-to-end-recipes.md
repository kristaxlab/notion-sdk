# End-to-end recipes

Practical workflows combining `PagesEndpoint`, `BlocksEndpoint`, and `FileUploadsEndpoint`.

This page uses fluent helpers from `NotionBlocks` and `NotionText`. Use static imports in examples for readability:

```java
import static io.kristaxlab.notion.fluent.NotionBlocks.*;
import static io.kristaxlab.notion.fluent.NotionText.*;
```

## Recipe: create a project page and seed checklist

```java
Page page = client.pages().create(p -> p
    .inPage("projects-root-page-id")
    .title("Project Atlas")
    .icon("🧭")
    .children(c -> c
        .heading2("Launch checklist")
        .todos("Finalize scope", "Run QA", "Publish release notes")));

client.blocks().appendChildren(page.getId(), c -> c
    .divider()
    .callout("💡", "Keep this page as the single source of truth"));
```

## Recipe: upload an image and embed it with caption

```java
FileUpload upload = client.fileUploads().create(
    FileUploadCreateParams.singlePart("dashboard.png")
);

client.fileUploads().upload(
    upload.getId(),
    FileUploadSendParams.of(new File("/tmp/dashboard.png"), "image/png")
);

client.blocks().appendChildren("page-id", image(data -> data
    .fileUpload(upload.getId())
    .caption(plainText("Dashboard snapshot - "), blue("weekly build"))));
```

## Recipe: read page tasks and print unchecked items

```java
BlockList blocks = client.blocks().retrieveChildren("page-id");
NotionBlocksViewer viewer = NotionBlocksViewer.of(blocks);

for (String task : viewer.flatten().todos().unchecked().plainTextList()) {
  System.out.println("TODO: " + task);
}
```

## Recipe: migrate markdown notes into a Notion page

```java
Page page = client.pages().create(p -> p
    .inPage("parent-page-id")
    .title("Migration target"));

client.pages().updateAsMarkdown(
    page.getId(),
    UpdatePageAsMarkdownParams.builder()
        .markdown("""
            # Imported notes
            - Item one
            - Item two
            """)
        .build()
);
```

## Related cookbook pages

- [Creating pages](01-creating-pages.md)
- [Reading page content](05-reading-content.md)
- [Files and media uploads](08-files-and-media.md)
- [Back to README](../../README.md#cookbook)
