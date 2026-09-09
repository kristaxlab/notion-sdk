package examples;

import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.templates.TemplateParams;
import io.kristaxlab.notion.util.PollingConfig;
import io.kristaxlab.notion.util.TemplatePoller;
import io.kristaxlab.notion.util.TemplatePollingException;
import java.time.Duration;

/**
 * Examples demonstrating template polling functionality.
 *
 * <p>When creating pages with templates, Notion applies the template asynchronously. Use {@link
 * TemplatePoller} to wait for template content to be fully applied.
 */
public class TemplatePolliningExamples {

  private static final String NOTION_TOKEN = System.getenv("NOTION_TOKEN");

  public static void main(String[] args) {
    NotionClient client = NotionClient.forToken(NOTION_TOKEN);

    // Example 1: Wait for specific number of blocks
    waitForBlockCount(client);

    // Example 2: Wait for any blocks to appear
    waitForAnyBlocks(client);

    // Example 3: Custom readiness check
    customReadinessCheck(client);

    // Example 4: Handle timeout exception
    handleTimeout(client);
  }

  /**
   * Example 1: Wait for a specific number of blocks to appear after template application.
   */
  static void waitForBlockCount(NotionClient client) {
    String parentId = "your-parent-page-id";
    String templateId = "your-template-page-id";

    // Create a page using another page as a template
    Page page =
        client
            .pages()
            .create(
                p ->
                    p.inPage(parentId)
                        .title("Duplicated Page")
                        .template(TemplateParams.templateId(templateId)));

    // Wait for 5 blocks to appear (timeout: 10s, interval: 500ms)
    try {
      BlockList blocks =
          TemplatePoller.awaitBlockCount(
              client,
              page.getId(),
              5, // expected block count
              PollingConfig.of(Duration.ofSeconds(10), Duration.ofMillis(500)));

      System.out.println("Template applied! Found " + blocks.getResults().size() + " blocks");

      // Refresh page to get updated properties
      Page updatedPage = client.pages().retrieve(page.getId());
      System.out.println("Page title: " + updatedPage.getTitle());

    } catch (TemplatePollingException e) {
      System.err.println("Template didn't apply in time: " + e.getMessage());
    }
  }

  /**
   * Example 2: Wait for any blocks to appear (at least 1).
   */
  static void waitForAnyBlocks(NotionClient client) {
    String parentId = "your-parent-page-id";
    String templateId = "your-template-page-id";

    Page page =
        client
            .pages()
            .create(p -> p.inPage(parentId).template(TemplateParams.templateId(templateId)));

    // Wait for at least one block (using attempt-based config)
    BlockList blocks =
        TemplatePoller.awaitAnyBlocks(
            client, page.getId(), PollingConfig.ofAttempts(20)); // 20 attempts max

    System.out.println("Found " + blocks.getResults().size() + " blocks");
  }

  /**
   * Example 3: Custom readiness check - wait for specific page properties.
   */
  static void customReadinessCheck(NotionClient client) {
    String parentId = "your-parent-page-id";
    String templateId = "your-template-page-id";

    Page page =
        client
            .pages()
            .create(p -> p.inPage(parentId).template(TemplateParams.templateId(templateId)));

    // Wait for the page title to be populated
    Page readyPage =
        TemplatePoller.awaitPage(
            client,
            page.getId(),
            p -> {
              String title = p.getTitle();
              return title != null && !title.isEmpty();
            },
            PollingConfig.builder()
                .timeout(Duration.ofSeconds(15))
                .pollingInterval(Duration.ofMillis(400))
                .build());

    System.out.println("Page ready with title: " + readyPage.getTitle());
  }

  /**
   * Example 4: Handling timeout exceptions.
   */
  static void handleTimeout(NotionClient client) {
    String parentId = "your-parent-page-id";
    String templateId = "your-template-page-id";

    Page page =
        client
            .pages()
            .create(p -> p.inPage(parentId).template(TemplateParams.templateId(templateId)));

    try {
      // Very short timeout to demonstrate exception
      TemplatePoller.awaitBlockCount(
          client,
          page.getId(),
          100, // unrealistic expectation
          PollingConfig.of(Duration.ofSeconds(2), Duration.ofMillis(500)));

    } catch (TemplatePollingException e) {
      // Handle timeout gracefully
      System.err.println("Polling failed: " + e.getMessage());

      // Fallback: retrieve current state
      Page currentPage = client.pages().retrieve(page.getId());
      BlockList currentBlocks = client.blocks().retrieveChildren(page.getId());

      System.out.println(
          "Current state: "
              + currentBlocks.getResults().size()
              + " blocks (expected 100, got less)");
    }
  }

  /**
   * Example 5: Wait for specific block types to appear.
   */
  static void waitForBlockTypes(NotionClient client) {
    String parentId = "your-parent-page-id";
    String templateId = "your-template-page-id";

    Page page =
        client
            .pages()
            .create(p -> p.inPage(parentId).template(TemplateParams.templateId(templateId)));

    // Wait for both a heading and a paragraph block
    BlockList blocks =
        TemplatePoller.awaitBlocks(
            client,
            page.getId(),
            blockList -> {
              if (blockList.getResults() == null) return false;

              boolean hasHeading =
                  blockList.getResults().stream()
                      .anyMatch(b -> b.getType().startsWith("heading_"));
              boolean hasParagraph =
                  blockList.getResults().stream().anyMatch(b -> "paragraph".equals(b.getType()));

              return hasHeading && hasParagraph;
            },
            PollingConfig.ofTimeout(Duration.ofSeconds(10)));

    System.out.println("Found required block types!");
  }
}

