# Creating pages from templates

Create a page from a template the same way you create any other page: pass a `TemplateParams` on
`pages().create(...)`. Notion applies the template in the background, so the create call returns as
soon as the page exists. That is enough if you only need the new page id and will come back to the
content later.

If you need the template's blocks or properties immediately — to append more content, read a status,
or assert that duplication finished — wait with `TemplatePoller`. The sections after the create
examples cover that.

## Create a page from a template

### Under a parent page

```java
Page page = client.pages().create(p -> p
    .inPage(parentId)
    .title("Sprint 12 retro")
    .template(TemplateParams.templateId(templateId)));
```

`page.getId()` is usable right away. The template body may still be empty if you retrieve children
in the same moment.

### In a data source

Filter the data source's templates by name (case-insensitive substring), then pass the matching id.
`defaultTemplate()` applies the data source's default template without looking the id up.

```java
Templates templates = client.dataSources().retrieveTemplates(dataSourceId, "Weekly standup", null, null);
String templateId = templates.getResults().get(0).getId();

Page fromNamed = client.pages().create(p -> p
    .inDataSource(dataSourceId)
    .title("New task")
    .template(TemplateParams.templateId(templateId)));

Page fromDefault = client.pages().create(p -> p
    .inDataSource(dataSourceId)
    .title("New task")
    .template(TemplateParams.defaultTemplate()));
```

## Wait for the template to apply

When you create a page with a template, Notion applies the template's properties, blocks and other
content asynchronously. Immediately after create, that content may not be present yet.

### Basic Usage: Wait for Blocks

```java
Page page = client.pages().create(p -> p
    .inPage(parentId)
    .template(TemplateParams.templateId(templateId)));

// Wait for the template blocks to appear (up to 10 seconds, checking every 500ms)
BlockList blocks = TemplatePoller.awaitBlockCount(
    client, 
    page.getId(), 
    3,  // expected number of blocks
    PollingConfig.of(Duration.ofSeconds(10), Duration.ofMillis(500))
);
```

### Wait for Any Blocks

```java
// Wait for at least one block to appear
BlockList blocks = TemplatePoller.awaitAnyBlocks(
    client,
    page.getId(),
    PollingConfig.ofTimeout(Duration.ofSeconds(10))
);
```

### Wait for Minimum Block Count

```java
// Wait for at least 5 blocks (template might have more)
BlockList blocks = TemplatePoller.awaitMinBlockCount(
    client,
    page.getId(),
    5,
    PollingConfig.of(Duration.ofSeconds(10), Duration.ofMillis(500))
);
```

## Polling Configuration

### Timeout-Based Polling

```java
// Poll for up to 15 seconds with 500ms intervals
PollingConfig config = PollingConfig.ofTimeout(Duration.ofSeconds(15));
```

### Attempt-Based Polling

```java
// Poll for a maximum of 20 attempts (with default 500ms intervals)
PollingConfig config = PollingConfig.ofAttempts(20);
```

### Combined Timeout and Attempts

```java
// Stop when either limit is reached
PollingConfig config = PollingConfig.of(
    Duration.ofSeconds(10),      // timeout
    Duration.ofMillis(500)       // polling interval
);
```

### Full Control with Builder

```java
PollingConfig config = PollingConfig.builder()
    .timeout(Duration.ofSeconds(20))
    .maxAttempts(40)
    .pollingInterval(Duration.ofMillis(300))
    .build();
```

## Custom Readiness Checks

### Check Page Properties

Wait for a specific page property to be set:

```java
Page page = TemplatePoller.awaitPage(
    client,
    pageId,
    p -> {
        // Check if the "Status" property is set
        PagePropertyValue statusProp = p.getProperties().get("Status");
        return statusProp instanceof StatusProperty status && status.getStatus() != null;
    },
    PollingConfig.ofTimeout(Duration.ofSeconds(10))
);
```

### Check Page Title

```java
Page page = TemplatePoller.awaitPage(
    client,
    pageId,
    p -> p.getTitle() != null && !p.getTitle().isEmpty(),
    PollingConfig.ofTimeout(Duration.ofSeconds(10))
);
```

### Check Block Types

Wait for specific block types to appear:

```java
BlockList blocks = TemplatePoller.awaitBlocks(
    client,
    pageId,
    blockList -> {
        if (blockList.getResults() == null) return false;
        
        // Check for specific block types
        boolean hasHeading = blockList.getResults().stream()
            .anyMatch(b -> b.getType().startsWith("heading_"));
        boolean hasParagraph = blockList.getResults().stream()
            .anyMatch(b -> "paragraph".equals(b.getType()));
            
        return hasHeading && hasParagraph;
    },
    PollingConfig.of(Duration.ofSeconds(10), Duration.ofMillis(500))
);
```

### Check Block Content

Wait for blocks with specific content:

```java
BlockList blocks = TemplatePoller.awaitBlocks(
    client,
    pageId,
    blockList -> {
        if (blockList.getResults() == null || blockList.getResults().isEmpty()) {
            return false;
        }
        
        // Check if any block contains specific text
        return blockList.getResults().stream()
            .filter(b -> b instanceof ParagraphBlock)
            .map(b -> (ParagraphBlock) b)
            .anyMatch(p -> {
                var richText = p.getParagraph().getRichText();
                return richText != null && richText.stream()
                    .anyMatch(rt -> rt.getPlainText().contains("Important"));
            });
    },
    PollingConfig.ofTimeout(Duration.ofSeconds(10))
);
```

## Exception Handling

### Timeout Exception

```java
try {
    BlockList blocks = TemplatePoller.awaitBlockCount(
        client, pageId, 5,
        PollingConfig.of(Duration.ofSeconds(5), Duration.ofMillis(500))
    );
} catch (TemplatePollingException e) {
    // Template didn't apply in time, or polling was interrupted
    System.err.println("Template polling failed: " + e.getMessage());
    // Handle timeout (retry, use fallback, etc.)
}
```

### Max Attempts Exception

```java
try {
    BlockList blocks = TemplatePoller.awaitAnyBlocks(
        client, pageId,
        PollingConfig.ofAttempts(10)
    );
} catch (TemplatePollingException e) {
    // Exceeded maximum attempts
    System.err.println("Failed after max attempts: " + e.getMessage());
}
```

## Best Practices

### 1. Choose Appropriate Timeouts

Templates typically apply within 1-5 seconds, but network latency and Notion's load can affect timing:

```java
// For simple templates (few blocks)
PollingConfig.of(Duration.ofSeconds(5), Duration.ofMillis(500))

// For complex templates (many blocks, rich content)
PollingConfig.of(Duration.ofSeconds(15), Duration.ofMillis(500))
```

### 2. Use Specific Checks When Possible

Instead of polling for all content:

```java
// ❌ Waiting for arbitrary time
Thread.sleep(5000);

// ✅ Waiting for specific condition
TemplatePoller.awaitBlockCount(client, pageId, expectedCount, config);
```

### 3. Refresh Page After Template Application

After waiting for blocks, refresh the page to get updated properties:

```java
BlockList blocks = TemplatePoller.awaitBlockCount(client, pageId, 3, config);

// Refresh page to get latest properties
Page updatedPage = client.pages().retrieve(pageId);
```

Interruptions during polling are wrapped in `TemplatePollingException` with the interrupt flag restored on the current thread. Check `Thread.currentThread().isInterrupted()` or inspect `getCause()` if you need to distinguish cancellation from timeout.

## Common Use Cases

### Duplication with Template

```java
// Create a duplicate of an existing page using it as a template
Page original = // ... existing page
Page duplicate = client.pages().create(p -> 
    p.inPage(parentId)
     .template(TemplateParams.templateId(original.getId())));

// Wait for duplication to complete
BlockList content = TemplatePoller.awaitBlockCount(
    client,
    duplicate.getId(),
    expectedBlockCount,
    PollingConfig.ofTimeout(Duration.ofSeconds(10))
);
```

### Template from Data Source

```java
// Create page from data source template
Templates templates = client.dataSources().retrieveTemplates(dataSourceId);
String templateId = templates.getResults().get(0).getId();

Page page = client.pages().create(p -> 
    p.inDataSource(dataSourceId)
     .template(TemplateParams.templateId(templateId)));

// Wait for template properties and content
Page ready = TemplatePoller.awaitPage(
    client,
    page.getId(),
    p -> {
        // Check that all expected properties are populated
        return p.getProperties().size() >= expectedPropertyCount;
    },
    PollingConfig.of(Duration.ofSeconds(10), Duration.ofMillis(500))
);
```

### Batch Page Creation

```java
List<String> pageIds = new ArrayList<>();

// Create multiple pages with templates
for (int i = 0; i < 10; i++) {
    Page page = client.pages().create(p -> 
        p.inPage(parentId)
         .title("Task " + (i + 1))
         .template(TemplateParams.templateId(templateId)));
    pageIds.add(page.getId());
}

// Wait for all templates to apply
for (String pageId : pageIds) {
    try {
        TemplatePoller.awaitAnyBlocks(
            client, 
            pageId, 
            PollingConfig.ofTimeout(Duration.ofSeconds(5))
        );
    } catch (TemplatePollingException e) {
        System.err.println("Template failed for page " + pageId);
    }
}
```

## Related cookbook pages

- [Creating pages](creating-pages.md)
- [Reading page content](reading-content.md)
- [Page properties and pagination](page-properties.md)
- [End-to-end recipes](end-to-end-recipes.md)
- [Back to README](../../README.md#cookbook)

The full method list for `TemplatePoller` and `PollingConfig` is in their Javadoc.

