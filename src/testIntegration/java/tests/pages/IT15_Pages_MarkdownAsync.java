package tests.pages;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.asynctask.AsyncTask;
import io.kristaxlab.notion.model.asynctask.AsyncTaskStatus;
import io.kristaxlab.notion.model.page.PageAsMarkdown;
import io.kristaxlab.notion.model.page.markdown.UpdatePageAsMarkdownParams;
import io.kristaxlab.notion.util.AsyncTaskPoller;
import io.kristaxlab.notion.util.PollingConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

/**
 * Exercises markdown page create and update with {@code allow_async}, then polls via the async task
 * retrieve endpoint / {@link AsyncTaskPoller}.
 */
public class IT15_Pages_MarkdownAsync extends WithEmptyTestPage {

  private static final PollingConfig ASYNC_POLLING = PollingConfig.of(ofSeconds(60), ofMillis(500));

  @Test
  @DisplayName("IT-15: Pages - Markdown create and update asynchronously with async task polling")
  public void testMarkdownAsyncCreateAndUpdate() {
    AsyncTask createTask =
        getNotionClient()
            .pages()
            .createAsync(
                page ->
                    page.inPage(getTestPageId())
                        .title("Async markdown page")
                        .markdown("Async paragraph one.\n## Async section\nAsync paragraph two."));

    assertNotNull(createTask);
    assertNotNull(createTask.getId());
    assertNotNull(createTask.getStatus());
    assertFalse(
        AsyncTaskStatus.FAILED.matches(createTask.getStatus()),
        "createAsync must not return a failed task immediately");

    AsyncTask createDone =
        AsyncTaskPoller.awaitTerminal(getNotionClient(), createTask.getId(), ASYNC_POLLING);

    assertTrue(
        AsyncTaskStatus.SUCCEEDED.matches(createDone.getStatus()),
        "create async task should succeed");
    assertNotNull(createDone.getResult(), "succeeded create task should include a result");
    String pageId = createDone.getResult().path("id").asText(null);
    assertNotNull(pageId, "create async result should include a page id");
    assertFalse(pageId.isBlank());

    PageAsMarkdown createdMarkdown = getNotionClient().pages().retrieveAsMarkdown(pageId);
    assertNotNull(createdMarkdown.getMarkdown());
    assertTrue(
        createdMarkdown.getMarkdown().contains("Async paragraph one."),
        "created page markdown should include original body");
    assertTrue(
        createdMarkdown.getMarkdown().contains("Async section"),
        "created page markdown should include original heading");

    AsyncTask updateTask =
        getNotionClient()
            .pages()
            .updateAsMarkdownAsync(
                pageId,
                UpdatePageAsMarkdownParams.replaceContent(
                    "# Replaced async section\nReplaced async paragraph."));

    assertNotNull(updateTask);
    assertNotNull(updateTask.getId());
    assertFalse(
        AsyncTaskStatus.FAILED.matches(updateTask.getStatus()),
        "updateAsMarkdownAsync must not return a failed task immediately");

    PageAsMarkdown updated =
        AsyncTaskPoller.awaitPageAsMarkdown(getNotionClient(), updateTask.getId(), ASYNC_POLLING);

    assertNotNull(updated.getMarkdown());
    assertTrue(
        updated.getMarkdown().contains("Replaced async section"),
        "after async replace, new content should be present");
    assertTrue(
        updated.getMarkdown().contains("Replaced async paragraph."),
        "after async replace, new content should be present");
    assertFalse(
        updated.getMarkdown().contains("Async paragraph one."),
        "after async replace, original body should be gone");
  }
}
