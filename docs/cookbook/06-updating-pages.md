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

## Replace content with Markdown

```java
UpdatePageAsMarkdownParams request = UpdatePageAsMarkdownParams.builder()
    .markdown("""
        # Release notes
        - Shipped onboarding improvements
        - Improved error handling
        """)
    .build();

client.pages().updateAsMarkdown("page-id", request);
```

## Related cookbook pages

- [Creating pages](01-creating-pages.md)
- [Reading page content](05-reading-content.md)
- [Files and media uploads](08-files-and-media.md)
- [Back to README](../../README.md#cookbook)
