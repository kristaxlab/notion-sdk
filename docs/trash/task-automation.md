# Recipe: Task Automation

<!-- TODO: This is aspiration guide, should be revised after Notion SDK api is finalized -->

Automate project management workflows by creating pages, updating statuses, and moving items between databases in response to external events.

## Use Cases

- Create Notion tasks from Slack messages or form submissions
- Auto-update task status when a pull request is merged
- Move items between "Backlog", "Sprint", and "Done" databases
- Send reminders for overdue tasks
- Auto-assign tasks based on workload or rotation

## Example: Create Tasks from Form Submissions

> **Status: Planned** -- Uses the aspirational high-level API.

```java
NotionClient client = NotionClient.forToken(System.getenv("NOTION_TOKEN"));
String taskDbId = "your-task-database-id";

void handleFormSubmission(FormData form) {
    client.pages().create(
        PageRequest.builder()
            .parentDatabase(taskDbId)
            .property("Name", TitleProperty.of(form.getSubject()))
            .property("Description", RichTextProperty.of(form.getMessage()))
            .property("Priority", SelectProperty.of(form.getPriority()))
            .property("Status", SelectProperty.of("To Do"))
            .property("Source", SelectProperty.of("Contact Form"))
            .property("Submitted", DateProperty.of(Instant.now()))
            .build()
    );
}
```

## Example: Auto-Update Status from CI/CD

```java
void onPullRequestMerged(PullRequest pr) {
    // Find the task linked to this PR
    PaginatedList<Page> tasks = client.databases().query(taskDbId,
        DatabaseQuery.builder()
            .filter(Filter.property("PR Number").number().equals(pr.getNumber()))
            .build()
    );

    for (Page task : tasks) {
        client.pages().update(task.getId(),
            PageUpdate.builder()
                .property("Status", SelectProperty.of("Done"))
                .property("Completed", DateProperty.of(Instant.now()))
                .build()
        );

        // Add a comment
        client.comments().create(
            CommentCreate.builder()
                .parentPage(task.getId())
                .richText(RichText.of("PR #" + pr.getNumber() + " merged by "
                    + pr.getMergedBy()))
                .build()
        );
    }
}
```

## Example: Daily Overdue Task Report

```java
void checkOverdueTasks() {
    PaginatedList<Page> overdue = client.databases().query(taskDbId,
        DatabaseQuery.builder()
            .filter(Filter.and(
                Filter.property("Due Date").date().before(LocalDate.now().toString()),
                Filter.property("Status").select().doesNotEqual("Done")
            ))
            .sort(Sort.property("Due Date").ascending())
            .build()
    );

    for (Page task : overdue.collectAll()) {
        String title = task.getTitle();
        String dueDate = task.getProperty("Due Date").getDate().toString();
        String assignee = task.getProperty("Assignee").getPeople().get(0).getName();

        sendReminder(assignee, title, dueDate);
    }
}
```

## Example: Round-Robin Task Assignment

```java
List<String> teamMembers = List.of("user-id-1", "user-id-2", "user-id-3");
AtomicInteger counter = new AtomicInteger(0);

void autoAssign(String pageId) {
    int index = counter.getAndIncrement() % teamMembers.size();
    String assignee = teamMembers.get(index);

    client.pages().update(pageId,
        PageUpdate.builder()
            .property("Assignee", PeopleProperty.of(assignee))
            .build()
    );
}
```

## Scheduling

Combine the SDK with a scheduler for recurring automations:

```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

// Check for overdue tasks every morning at 9 AM
scheduler.scheduleAtFixedRate(
    () -> checkOverdueTasks(),
    initialDelayUntil9AM(),
    TimeUnit.DAYS.toSeconds(1),
    TimeUnit.SECONDS
);
```

For production use, consider:
- **Spring @Scheduled** for Spring Boot applications
- **Quartz Scheduler** for complex cron-like schedules
- **Cloud functions** (AWS Lambda, Google Cloud Functions) triggered by CloudWatch/Cloud Scheduler

## Tips

- **Idempotency** -- check if a task already exists before creating duplicates (use a unique external ID property).
- **Rate limits** -- batch operations and add backoff for `TooManyRequestsException`.
- **Error recovery** -- wrap automations in try/catch and log failures for retry.
- **Audit trail** -- use comments to record why a task was created or updated.

## See Also

- [Pages](../guides/pages.md) -- creating and updating pages
- [Databases](../guides/databases.md) -- querying for matching tasks
- [Comments](../guides/comments.md) -- adding context to tasks
- [Error Handling](../advanced/error-handling.md) -- handling rate limits and failures
