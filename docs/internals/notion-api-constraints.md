# Notion API constraints

Rules the Notion API enforces that the SDK cannot express in its types, and that are not visible from reading SDK code. Violating one produces a `ValidationException` at runtime rather than a compile error.

**What belongs here:** a constraint imposed by Notion, not by this SDK, that a caller or test author would otherwise have to discover by getting a 400 back. Anything derivable from a class definition belongs in Javadoc instead; rationale for SDK design belongs in [an ADR](../adr/README.md). Terminology is defined in [CONTEXT.md](../../CONTEXT.md).

## Page properties

- **`unique_id` is singular and server-owned.** A page in a data source can carry at most one `unique_id` property. Its numeric part is auto-incremented by Notion and cannot be set through the API; only the prefix can be changed, and only on properties that already exist. Tests must therefore read the value rather than seed it.
- **The `title` property id is always `title`.** Every other property has a URL-encoded short id. Title is auto-added and cannot be removed, so `title` is safe to hard-code as an id.
- **Paginated property values can arrive truncated.** The page retrieve endpoint may return a partial value for `relation`, `people`, `rich_text` and `title`. See [ADR-0001](../adr/0001-complex-hierarchy-and-deserialization-of-page-properties.md) and [Page properties](../cookbook/page-properties.md).

## Data sources and databases

- **A database is a container; a data source holds the schema.** Since API version `2025-09-03` one database can host several data sources. The parent of a data source is always a **database** — not a page and not another data source.
- **Exactly one title column per data source.** A schema without a title column, or with two, is rejected.
- **Deleting a column means sending an explicit `null`.** `UpdateDataSourceParams.properties` is annotated so that `null` map values are serialized rather than omitted; that null is what removes the column. Dropping the entry from the map instead leaves the column untouched. `NotionSchemaBuilder.remove(nameOrId)` encodes this correctly.

## Markdown endpoints

- **Synced pages cannot be modified** through the Markdown endpoints, and neither can databases or non-page blocks.
- **Destructive updates are opt-in.** An update that would remove a child page or database fails unless `allowDeletingContent` is `true`. The full list of validation failures is in [Updating pages](../cookbook/updating-pages.md#failures-to-expect).

## Templates

- **Template content is applied asynchronously.** A page created with a template comes back before its blocks and properties exist, so reading it immediately returns an incomplete page. There is no completion signal — the caller has to poll until the expected content appears. `TemplatePoller` does this; see [Creating pages from templates](../cookbook/templates.md).

## Concurrency

- **Concurrent writes to the same parent page can return `409 Conflict`.** This is why parallel execution is disabled for the integration suite; see [Testing Guide](testing-guide.md).

## See also

- [Notion API status and error codes](https://developers.notion.com/reference/status-codes)
- [Architecture](architecture.md) — how error responses map to typed exceptions
