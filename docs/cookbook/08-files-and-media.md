# Files and media uploads

Upload files with `FileUploadsEndpoint` (`client.fileUploads()`), then reference them in pages and blocks.

This page uses fluent helpers from `NotionBlocks` and `NotionText`. Use static imports in examples for readability:

```java
import static io.kristaxlab.notion.fluent.NotionBlocks.*;
import static io.kristaxlab.notion.fluent.NotionText.*;
```

## Single-part upload from a local file

```java
FileUpload create = client.fileUploads().create(
    FileUploadCreateParams.singlePart("architecture.png")
);

FileUpload uploaded = client.fileUploads().upload(
    create.getId(),
    FileUploadSendParams.of(new File("/tmp/architecture.png"), "image/png")
);
```

## Complete a multipart upload

```java
FileUpload create = client.fileUploads().create(
    FileUploadCreateParams.multiPart("large-video.mp4", 2)
);

client.fileUploads().upload(create.getId(),
    FileUploadSendParams.builder()
        .file(new File("/tmp/video-part-1.bin"))
        .contentType("video/mp4")
        .partNumber(1)
        .build());

client.fileUploads().upload(create.getId(),
    FileUploadSendParams.builder()
        .file(new File("/tmp/video-part-2.bin"))
        .contentType("video/mp4")
        .partNumber(2)
        .build());

client.fileUploads().complete(create.getId());
```

## Use uploaded file as page icon and cover

```java
String fileUploadId = "550e8400-e29b-41d4-a716-446655440000";

client.pages().update("page-id", UpdatePageParams.builder()
    .icon(Icon.fileUpload(fileUploadId))
    .cover(Cover.fileUpload(fileUploadId))
    .build());
```

## Embed uploaded media into page content

```java
String imageUploadId = "550e8400-e29b-41d4-a716-446655440001";
String pdfUploadId = "550e8400-e29b-41d4-a716-446655440002";

client.blocks().appendChildren("page-id", List.of(
    image(imageUploadId),
    pdf(data -> data
        .fileUpload(pdfUploadId)
        .caption(plainText("Release spec "), bold("v2.1")))
));
```

## Import from external URL

```java
FileUpload imported = client.fileUploads().create(
    FileUploadCreateParams.external(
        "logo.png",
        "https://example.com/assets/logo.png"
    )
);
```

## Related cookbook pages

- [Structured layouts](04-structured-layouts.md)
- [Updating pages](06-updating-pages.md)
- [End-to-end recipes](09-end-to-end-recipes.md)
- [Back to README](../../README.md#cookbook)
