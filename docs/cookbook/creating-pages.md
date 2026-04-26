# Creating pages

Create pages under a parent page. Full support for title, icon, cover, and initial block content.

> **Note:** Creating pages inside a database (with typed properties such as Status, Assignee, or Date)
> will be covered once database property support is finalized.


## Create a page with a title

```java
Page page = client.pages().create(p -> p
    .underPage("parent-page-id")
    .title("My new page"));
```

## Create a page with an icon and cover

```java
Page page = client.pages().create(p -> p
    .underPage("parent-page-id")
    .title("Project Aurora — Q2 Dashboard")
    .icon("📊")
    .cover("https://images.unsplash.com/photo-1518770660439-4636190af475"));
```

Pass a UUID string to `.cover()` to reference a previously uploaded file instead of an external URL.

## Create a page with initial content

Use `.children()` to set the page body at creation time — no separate append call needed.

```java
Page page = client.pages().create(p -> p
    .underPage("parent-page-id")
    .title("Error Handling Best Practices")
    .icon("🛡️")
    .children(content -> content
        .heading2("Principles")
        .numbered("Fail fast — detect errors at the boundary.")
        .numbered("Be specific — throw the most precise exception type.")
        .numbered("Log once — avoid duplicate log entries.")
        .divider()
        .callout("💡", "Use typed exceptions from the SDK's exception hierarchy.")));
```

## Use a pre-built params object

When you need to construct the request separately from the API call, use `CreatePageParams.builder()`.

```java
CreatePageParams params = CreatePageParams.builder()
    .underPage("parent-page-id")
    .title("Weekly Retrospective")
    .icon("🔁")
    .children(content -> content
        .heading2("What went well")
        .bullet("Shipped the feature on time")
        .heading2("What to improve")
        .bullet("Add more integration tests"))
    .build();

Page page = client.pages().create(params);
```

## See also

- [Page lifecycle](page-lifecycle.md) — update, archive, restore, move
- [Adding blocks](blocks.md) — append blocks after creation
