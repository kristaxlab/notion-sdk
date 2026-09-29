package tests.pages;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.PageAsMarkdown;
import io.kristaxlab.notion.model.page.markdown.UpdatePageAsMarkdownParams;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT5_Pages_Markdown extends WithEmptyTestPage {

  @Test
  @DisplayName("IT-5: Pages - Markdown - create, retrieve, and update page as markdown (all modes)")
  public void testMarkdownCRUD() {
    // 1. Create page with markdown content
    Page created =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inPage(getTestPageId())
                        .title("Page as markdown")
                        .markdown("Paragraph one.\n## Second section\nParagraph two."));

    assertNotNull(created);
    assertNotNull(created.getId());

    // 2. Retrieve markdown content
    PageAsMarkdown markdown = getNotionClient().pages().retrieveAsMarkdown(created.getId());
    assertNotNull(markdown);
    assertEquals(created.getId().replace("-", ""), markdown.getId().replace("-", ""));
    assertNotNull(markdown.getMarkdown());
    // '# Page as markdow\n' goes to title when creating a page with markdown content
    assertEquals("Paragraph one.\n## Second section\nParagraph two.", markdown.getMarkdown());

    // 3. Update content using replace mode (replace entire content)
    PageAsMarkdown replaced =
        getNotionClient()
            .pages()
            .updateAsMarkdown(
                created.getId(),
                "# Updated section\nUpdated paragraph.\n## Another section\nAnother paragraph.");

    assertNotNull(replaced);
    assertNotNull(replaced.getMarkdown());
    assertTrue(
        replaced.getMarkdown().contains("Updated section"),
        "After replace, new content should be present");
    assertFalse(
        replaced.getMarkdown().contains("First section"),
        "After replace, original content should be gone");

    // 4. Update content using update mode (search and replace) - using builder
    PageAsMarkdown updated =
        getNotionClient()
            .pages()
            .updateAsMarkdown(
                created.getId(),
                builder ->
                    builder
                        .updateContent("Updated section", "Modified section", false)
                        .updateContent("Updated paragraph.", "Modified paragraph.", false));

    assertNotNull(updated);
    assertNotNull(updated.getMarkdown());
    assertTrue(
        updated.getMarkdown().contains("Modified section"),
        "After update, replaced text should be present");
    assertTrue(
        updated.getMarkdown().contains("Modified paragraph."),
        "After update, replaced text should be present");
    assertFalse(
        updated.getMarkdown().contains("Updated section"), "After update, old text should be gone");
    assertFalse(
        updated.getMarkdown().contains("Updated paragraph."),
        "After update, old text should be gone");
    assertTrue(
        updated.getMarkdown().contains("Another section"),
        "After update, unchanged content should remain");

    // 5. Update content using replace mode with allowDeletingContent flag
    PageAsMarkdown finalReplace =
        getNotionClient()
            .pages()
            .updateAsMarkdown(
                created.getId(),
                UpdatePageAsMarkdownParams.replaceContent("# Final section\nFinal content.", true));

    assertNotNull(finalReplace);
    assertNotNull(finalReplace.getMarkdown());
    assertTrue(
        finalReplace.getMarkdown().contains("Final section"),
        "After final replace, new content should be present");
    assertTrue(
        finalReplace.getMarkdown().contains("Final content."),
        "After final replace, new content should be present");
    assertFalse(
        finalReplace.getMarkdown().contains("Modified section"),
        "After final replace, previous content should be gone");
  }

  @AfterAll
  public static void tearDown() {}
}
