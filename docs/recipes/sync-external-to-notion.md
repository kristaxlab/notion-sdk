# Recipe: Sync External Data to Notion

<!-- TODO: This is aspiration guide, should be revised after Notion SDK api is finalized -->

Push records from external systems (CRM, issue trackers, spreadsheets) into a Notion database and keep them in sync. This is the most common integration pattern.

## Use Cases

- Sync Jira issues or GitHub issues to a Notion project tracker
- Push CRM contacts or deals into a Notion database
- Import rows from Google Sheets or CSV files
- Mirror data from an internal API into Notion for visibility

## Architecture

```
  External System ──► Your Service ──► Notion SDK ──► Notion API
       (source)         (sync logic)                    (target)
```

**Key decisions:**
- **Full sync vs. incremental** -- do you replace all records each time, or track changes?
- **ID mapping** -- how do you correlate external records with Notion pages?
- **Conflict resolution** -- what happens if a record was edited in both systems?

## Step-by-Step Example: Sync GitHub Issues

> **Status: Planned** -- Uses the aspirational high-level API.
> See [Raw API Calls](../advanced/raw-api-calls.md) for the current low-level equivalent.

### 1. Set up the Notion database

Create a database with columns matching your external data:

| Column | Property type |
|---|---|
| Name | Title |
| GitHub ID | Number (unique key for deduplication) |
| Status | Select (Open, Closed) |
| Assignee | Rich text |
| URL | URL |
| Last Synced | Date |

### 2. Initialize the client

```java
NotionClient client = NotionClient.forToken(System.getenv("NOTION_TOKEN"));
String databaseId = "your-database-id";
```

### 3. Fetch existing records for deduplication

```java
Map<Integer, String> existingByGitHubId = new HashMap<>();

PaginatedList<Page> existing = client.databases().query(databaseId,
    DatabaseQuery.builder().build());

do {
    for (Page page : existing) {
        int ghId = page.getProperty("GitHub ID").getNumber().intValue();
        existingByGitHubId.put(ghId, page.getId());
    }
    if (existing.hasMore()) {
        existing = client.databases().query(databaseId,
            DatabaseQuery.builder()
                .startCursor(existing.getNextCursor())
                .build());
    }
} while (existing.hasMore());
```

### 4. Upsert records

```java
for (GitHubIssue issue : fetchGitHubIssues()) {
    Map<String, Object> properties = Map.of(
        "Name", TitleProperty.of(issue.getTitle()),
        "GitHub ID", NumberProperty.of(issue.getNumber()),
        "Status", SelectProperty.of(issue.getState()),
        "Assignee", RichTextProperty.of(issue.getAssignee()),
        "URL", UrlProperty.of(issue.getHtmlUrl()),
        "Last Synced", DateProperty.of(Instant.now())
    );

    if (existingByGitHubId.containsKey(issue.getNumber())) {
        // Update existing page
        String pageId = existingByGitHubId.get(issue.getNumber());
        client.pages().update(pageId,
            PageUpdate.builder().properties(properties).build());
    } else {
        // Create new page
        client.pages().create(
            PageRequest.builder()
                .parentDatabase(databaseId)
                .properties(properties)
                .build());
    }
}
```

### 5. Handle rate limits

Notion enforces rate limits (3 requests/second for integrations). Wrap your sync in retry logic:

```java
for (GitHubIssue issue : issues) {
    retryWithBackoff(() -> syncIssue(client, databaseId, issue));
}

static <T> T retryWithBackoff(Supplier<T> action) {
    int maxRetries = 5;
    for (int i = 0; i < maxRetries; i++) {
        try {
            return action.get();
        } catch (TooManyRequestsException e) {
            Thread.sleep(Duration.ofSeconds((long) Math.pow(2, i)).toMillis());
        }
    }
    throw new RuntimeException("Max retries exceeded");
}
```

## Tips

- **Use a unique key** (like `GitHub ID`) to avoid creating duplicate records on re-runs.
- **Batch your reads** -- query the full database once at the start rather than searching for each record individually.
- **Log exchanges** during development using `exchangeLogging()` to debug property mapping issues.
- **Handle pagination** -- databases with 100+ records require cursor-based pagination.
- **Respect rate limits** -- Notion's API allows roughly 3 requests/second. Build in backoff.

## See Also

- [Databases](../guides/databases.md) -- querying and creating database pages
- [Pages](../guides/pages.md) -- creating and updating pages
- [Pagination](../guides/pagination.md) -- iterating large result sets
- [Error Handling](../advanced/error-handling.md) -- handling `TooManyRequestsException`
