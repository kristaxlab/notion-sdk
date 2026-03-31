# Recipe: Notion as CMS

<!-- TODO: This is aspiration guide, should be revised after Notion SDK api is finalized -->

Use a Notion database as a content management system to power a blog, documentation site, product catalog, or any content-driven application.

## Use Cases

- Blog engine backed by a Notion database (each page = one post)
- Documentation site where content authors use Notion's editor
- Product catalog with structured metadata in database properties
- FAQ or knowledge base with categorized articles

## Architecture

```
  Notion (CMS)                       Your App
  ┌──────────────┐                  ┌──────────────┐
  │ Database     │ ──► Notion SDK ──► │ Web Server   │
  │  - Title     │     (read API)   │  - Render     │
  │  - Status    │                  │  - Cache      │
  │  - Category  │                  │  - Serve      │
  │  - Slug      │                  └──────────────┘
  │  - Content   │
  └──────────────┘
```

## Step-by-Step Example: Blog Engine

> **Status: Planned** -- Uses the aspirational high-level API.

### 1. Set up the Notion database

Create a database with columns:

| Column | Property type | Purpose |
|---|---|---|
| Title | Title | Post title |
| Slug | Rich text | URL-friendly identifier |
| Status | Select (Draft, Published, Archived) | Publication status |
| Category | Select | Content category |
| Published Date | Date | When the post goes live |
| Excerpt | Rich text | Short description for listings |

### 2. Query published posts

```java
NotionClient client = NotionClient.forToken(System.getenv("NOTION_TOKEN"));

PaginatedList<Page> posts = client.databases().query(databaseId,
    DatabaseQuery.builder()
        .filter(Filter.property("Status").select().equals("Published"))
        .sort(Sort.property("Published Date").descending())
        .build()
);
```

### 3. Render page content

```java
String postId = posts.getResults().get(0).getId();

PaginatedList<Block> blocks = client.blocks().children().list(postId);

StringBuilder html = new StringBuilder();
for (Block block : blocks) {
    html.append(renderBlock(block));
}

String renderBlock(Block block) {
    return switch (block) {
        case ParagraphBlock p -> "<p>" + renderRichText(p.getRichText()) + "</p>";
        case HeadingOneBlock h -> "<h1>" + renderRichText(h.getRichText()) + "</h1>";
        case HeadingTwoBlock h -> "<h2>" + renderRichText(h.getRichText()) + "</h2>";
        case HeadingThreeBlock h -> "<h3>" + renderRichText(h.getRichText()) + "</h3>";
        case BulletedListItemBlock b -> "<li>" + renderRichText(b.getRichText()) + "</li>";
        case CodeBlock c -> "<pre><code class=\"" + c.getLanguage() + "\">"
            + renderRichText(c.getRichText()) + "</code></pre>";
        case ImageBlock i -> "<img src=\"" + i.getFileData().getUrl() + "\" />";
        case QuoteBlock q -> "<blockquote>" + renderRichText(q.getRichText()) + "</blockquote>";
        case DividerBlock d -> "<hr />";
        default -> "<!-- unsupported: " + block.getType() + " -->";
    };
}
```

### 4. Implement caching

Notion-hosted image URLs expire after 1 hour. Cache aggressively and re-fetch when needed:

```java
// Cache post content for 5 minutes
LoadingCache<String, List<Block>> contentCache = CacheBuilder.newBuilder()
    .expireAfterWrite(5, TimeUnit.MINUTES)
    .build(CacheLoader.from(pageId ->
        client.blocks().children().list(pageId).collectAll()));

// Cache database queries for 1 minute
LoadingCache<String, List<Page>> listingCache = CacheBuilder.newBuilder()
    .expireAfterWrite(1, TimeUnit.MINUTES)
    .build(CacheLoader.from(query ->
        client.databases().query(databaseId, query).collectAll()));
```

### 5. Generate a sitemap

```java
PaginatedList<Page> allPosts = client.databases().query(databaseId,
    DatabaseQuery.builder()
        .filter(Filter.property("Status").select().equals("Published"))
        .build()
);

for (Page post : allPosts.collectAll()) {
    String slug = post.getProperty("Slug").getRichText().getPlainText();
    String lastEdited = post.getLastEditedTime().toString();
    System.out.printf("<url><loc>https://blog.example.com/%s</loc>" +
        "<lastmod>%s</lastmod></url>%n", slug, lastEdited);
}
```

## Tips

- **Use `Slug` for URLs** rather than page IDs -- this makes URLs human-readable and stable if you recreate pages.
- **Filter by `Status: Published`** so drafts don't appear on the live site.
- **Cache aggressively** -- Notion's API has rate limits, and content doesn't change every second.
- **Handle Notion-hosted images** -- their URLs expire after 1 hour. Either proxy them through your server and cache, or use external image URLs in Notion.
- **Support nested blocks** -- toggles and callouts can contain children. Render recursively.

## See Also

- [Databases](../guides/databases.md) -- querying with filters and sorts
- [Blocks](../guides/blocks.md) -- reading and rendering block content
- [Rich Text](../guides/rich-text.md) -- rendering rich text to HTML
- [Pagination](../guides/pagination.md) -- fetching all results
- [Files & Media](../guides/files-and-media.md) -- handling image expiry
