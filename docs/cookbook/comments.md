# Comments

Create, list, retrieve, update, and delete comments with `CommentsEndpoint` (`client.comments()`).

Use `NotionText` when you want formatted rich text instead of a plain string. Static import:

```java
import static io.kristaxlab.notion.fluent.NotionText.*;
```

The integration token needs comment read and insert capabilities. See [Working with comments](https://developers.notion.com/guides/data-apis/working-with-comments).

## Create a comment on a page

```java
Comment created = client.comments().create(
    CommentCreateParams.builder()
        .inPage("page-id")
        .richText("Hello from the SDK")
        .build());
```

## Create a comment on a block

```java
Comment created = client.comments().create(
    CommentCreateParams.builder()
        .inBlock("block-id")
        .markdown("Inline **bold** and *italic*")
        .build());
```

## Reply using an existing `discussion_id`

```java
Comment reply = client.comments().create(
    CommentCreateParams.builder()
        .discussionId(created.getDiscussionId())
        .richText("Following up")
        .build());
```

## Set a comment display name and attach a file

```java
Comment created = client.comments().create(
    CommentCreateParams.builder()
        .inPage("page-id")
        .richText("Thanks for the review")
        .displayName(CommentDisplayName.custom("Release bot"))
        .attachment(CommentAttachment.fileUpload("file-upload-id"))
        .build());
```

## List unresolved comments

The query parameter is `block_id` for both pages and blocks. The list includes unresolved comments
only. Feed `next_cursor` back in as the start cursor until it is `null` (the last page).

```java
List<Comment> all = new ArrayList<>();
String cursor = null;

do {
  CommentList chunk = client.comments().listComments("page-or-block-id", cursor, 100);
  all.addAll(chunk.getResults());
  cursor = chunk.getNextCursor();
} while (cursor != null);
```

Omit the last two arguments to fetch the first page with the API default page size:

```java
CommentList firstPage = client.comments().listComments("page-or-block-id");
```

## Retrieve, update, and delete

```java
Comment loaded = client.comments().retrieve(created.getId());

Comment updated = client.comments().update(
    loaded.getId(),
    CommentUpdateParams.builder().richText("Updated comment text").build());

Comment deleted = client.comments().delete(updated.getId());
```

The current integration can update or delete only comments it created.

## Related cookbook pages

- [Rich text and inline formatting](rich-text.md)
- [Adding blocks](adding-blocks.md)
- [Files and media uploads](files-and-media.md)
- [Back to README](../../README.md#cookbook)
