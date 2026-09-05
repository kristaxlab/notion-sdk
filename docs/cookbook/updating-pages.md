# Updating pages

Manage page metadata and lifecycle with the `PagesEndpoint` (`client.pages()`).

## Update the page title

```java
client.pages().update("page-id", UpdatePageParams.builder()
    .title("Updated title")
    .build());
```

## Update icon and cover

```java
client.pages().update("page-id", UpdatePageParams.builder()
    .icon(Icon.emoji("🚀"))
    .cover(Cover.external("https://images.unsplash.com/photo-1517816743773-6e0fd518b4a6"))
    .build());
```

## Lock and unlock a page

```java
client.pages().update("page-id", UpdatePageParams.builder().locked(true).build());
client.pages().update("page-id", UpdatePageParams.builder().locked(false).build());
```

## Move page to another parent page

```java
client.pages().move("page-id", Parent.pageParent("new-parent-page-id"));
```

## Archive and restore

```java
client.pages().delete("page-id");
client.pages().restore("page-id");
```

## Update content with Markdown

`updateAsMarkdown` has two mutually exclusive modes. Pick the one that matches your intent — the
request carries a `type` of either `replace_content` or `update_content`.

### Replace the whole page

The shorthand overload replaces all page content:

```java
PageAsMarkdown updated = client.pages().updateAsMarkdown("page-id", """
    # Release notes
    - Shipped onboarding improvements
    - Improved error handling
    """);
```

Use the params object when you also need `allowDeletingContent`:

```java
client.pages().updateAsMarkdown("page-id",
    UpdatePageAsMarkdownParams.replaceContent("# Release notes", true));
```

### Search and replace inside the page

Targeted edits are batchable — each `ContentUpdate` is one search-replace applied to the page's
Markdown rendering.

```java
ContentUpdate update = new ContentUpdate();
update.setOldStr("Status: IN PROGRESS");
update.setNewStr("Status: SHIPPED");
update.setReplaceAllMatches(true);

client.pages().updateAsMarkdown("page-id",
    UpdatePageAsMarkdownParams.updateContent(List.of(update), false));
```

### Failures to expect

`updateAsMarkdown` is validated server-side and throws `ValidationException` when:

- `oldStr` matches several locations and `replaceAllMatches` is `false` (ambiguous match)
- `oldStr` does not appear on the page at all
- the update would delete a child page or database and `allowDeletingContent` is `false`
- the target is a database or a non-page block
- the target is a synced page, which cannot be modified

## Related cookbook pages

- [Creating pages](creating-pages.md)
- [Reading page content](reading-content.md)
- [Page properties and pagination](page-properties.md)
- [Files and media uploads](files-and-media.md)
- [Back to README](../../README.md#cookbook)
