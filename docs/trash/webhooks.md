# Recipe: Webhooks

<!-- TODO: This is aspiration guide, should be revised after Notion SDK api is finalized -->

React to changes in Notion in real-time by receiving webhook notifications when pages, databases, or comments are created or updated.

> **Note:** Notion's webhook support is evolving. Check the [official Notion API changelog](https://developers.notion.com/changelog) for the latest status on webhook availability and event types.

## Use Cases

- Trigger a deployment when a "Status" property changes to "Published"
- Send a Slack notification when a new page is created in a database
- Sync Notion changes to an external system in real-time
- Audit trail -- log all modifications to a database

## Architecture

```
  Notion API ──► Webhook endpoint (your server) ──► Process event ──► SDK calls
```

Your server receives webhook events from Notion and uses the SDK to fetch additional data or perform follow-up actions.

## Setting Up a Webhook Endpoint

> **Status: Planned** -- Webhook registration via the SDK is not yet implemented.
> Currently, webhooks are configured through the Notion UI or API directly.

### 1. Create a web endpoint

```java
// Example using Spring Boot
@RestController
public class NotionWebhookController {

    private final NotionClient client =
        NotionClient.forToken(System.getenv("NOTION_TOKEN"));

    @PostMapping("/webhooks/notion")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Notion-Signature") String signature) {

        // Verify webhook signature
        if (!verifySignature(payload, signature)) {
            return ResponseEntity.status(401).body("Invalid signature");
        }

        // Parse the event
        WebhookEvent event = parseEvent(payload);
        processEvent(event);

        return ResponseEntity.ok("OK");
    }
}
```

### 2. Verify the webhook signature

```java
boolean verifySignature(String payload, String signature) {
    String secret = System.getenv("NOTION_WEBHOOK_SECRET");
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));
    String expected = Hex.encodeHexString(mac.doFinal(payload.getBytes()));
    return MessageDigest.isEqual(expected.getBytes(), signature.getBytes());
}
```

### 3. Process events

```java
void processEvent(WebhookEvent event) {
    switch (event.getType()) {
        case "page.created" -> handlePageCreated(event);
        case "page.updated" -> handlePageUpdated(event);
        case "comment.created" -> handleCommentCreated(event);
        default -> log.info("Unhandled event type: {}", event.getType());
    }
}

void handlePageCreated(WebhookEvent event) {
    String pageId = event.getPageId();

    // Fetch the full page using the SDK
    Page page = client.pages().get(pageId);

    // React to the new page
    sendSlackNotification("New page created: " + page.getTitle());
}

void handlePageUpdated(WebhookEvent event) {
    String pageId = event.getPageId();
    Page page = client.pages().get(pageId);

    String status = page.getProperty("Status").getSelect().getName();
    if ("Published".equals(status)) {
        triggerDeployment(page);
    }
}
```

## Polling as an Alternative

If webhooks are not available, poll for changes:

```java
Instant lastCheck = Instant.now();

void pollForChanges() {
    PaginatedList<Page> recentlyEdited = client.databases().query(databaseId,
        DatabaseQuery.builder()
            .filter(Filter.property("Last Edited")
                .date().after(lastCheck.toString()))
            .sort(Sort.lastEditedTime().descending())
            .build()
    );

    for (Page page : recentlyEdited) {
        processChange(page);
    }

    lastCheck = Instant.now();
}

// Poll every 30 seconds
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
scheduler.scheduleAtFixedRate(() -> pollForChanges(), 0, 30, TimeUnit.SECONDS);
```

## Tips

- **Idempotency** -- webhook events may be delivered more than once. Use the event ID or page `last_edited_time` to detect duplicates.
- **Quick response** -- return `200 OK` immediately and process the event asynchronously. Webhook providers typically have short timeouts.
- **Retry handling** -- Notion may retry failed webhook deliveries. Design your handler to tolerate duplicates.
- **Security** -- always verify the webhook signature before processing.
- **Polling fallback** -- if webhooks are unavailable or unreliable, polling with a `last_edited_time` filter is a robust alternative.

## See Also

- [Pages](../guides/pages.md) -- fetching page details after receiving an event
- [Databases](../guides/databases.md) -- querying for recently changed pages
- [Task Automation](task-automation.md) -- event-driven workflows
- [Error Handling](../advanced/error-handling.md) -- handling API errors in event handlers
