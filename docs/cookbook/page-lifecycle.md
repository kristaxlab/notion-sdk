# Page lifecycle

Retrieve, update, archive, restore, and move pages.

## Retrieve a page

```java
Page page = client.pages().retrieve("page-id");

String id          = page.getId();
String lastEdited  = page.getLastEditedTime();
boolean inTrash    = Boolean.TRUE.equals(page.getInTrash());
```

## Update the title

```java
client.pages().update("page-id", UpdatePageParams.builder()
    .title("New title")
    .build());
```

## Update icon and cover

```java
client.pages().update("page-id", UpdatePageParams.builder()
    .icon(Icon.emoji("🚀"))
    .cover(Cover.external("https://images.unsplash.com/photo-1517816743773-6e0fd518b4a6"))
    .build());
```

## Archive a page

Moves the page to the trash.

```java
client.pages().delete("page-id");
```

## Restore a page

Brings a trashed page back.

```java
client.pages().restore("page-id");
```

## Lock and unlock a page

```java
// Lock
client.pages().update("page-id", UpdatePageParams.builder()
    .isLocked(true)
    .build());

// Unlock
client.pages().update("page-id", UpdatePageParams.builder()
    .isLocked(false)
    .build());
```

## Move a page

Move a page to a different parent page.

```java
client.pages().move("page-id", Parent.pageParent("new-parent-id"));
```

## See also

- [Creating pages](creating-pages.md) — create pages with title, icon, cover, and content
- [Updating & managing blocks](updating-blocks.md) — update block content within a page
