package integration;

import static org.junit.jupiter.api.Assertions.*;

import integration.extension.NotionTestPage;
import io.kristaxlab.notion.fluent.NotionBlocks;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.UpdatePageParams;
import io.kristaxlab.notion.model.page.property.*;
import io.kristaxlab.notion.model.page.templates.TemplateParams;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PagesIT extends BaseIntegrationTest {

  @NotionTestPage private String pagesTestRootId;

  @Test
  @DisplayName("[IT-35]: Pages - Duplicate a page using its own id as template_id")
  public void testDuplicatePageViaTemplateId() {
    Page original =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inPage(pagesTestRootId)
                        .title("[IT-35] Original")
                        .children(c -> c.heading2("Heading").paragraph("Some content")));

    Page duplicate =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inPage(pagesTestRootId)
                        .title("[IT-35] Duplicate")
                        .template(TemplateParams.templateId(original.getId())));

    assertNotNull(duplicate.getId());
    assertNotEquals(original.getId(), duplicate.getId());

    // Notion copies the source content into the duplicate; we only assert that something was
    // copied because Notion sometimes wraps the content asynchronously.
    BlockList children = getNotionClient().blocks().retrieveChildren(duplicate.getId());
    assertNotNull(children);
    assertNotNull(children.getResults());
    assertFalse(
        children.getResults().isEmpty(), "Duplicate page should inherit content from the template");
  }

  @Test
  @DisplayName("[IT-37]: Pages - Apply a template to an existing page (with erase content)")
  public void testApplyTemplateToExistingPage() {
    Page templateSource =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inPage(pagesTestRootId)
                        .title("[IT-37] Template source")
                        .children(
                            c -> c.heading2("Template heading").paragraph("Template content")));

    Page target =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inPage(pagesTestRootId)
                        .title("[IT-37] Target")
                        .children(c -> c.paragraph("Original target content")));

    UpdatePageParams params = new UpdatePageParams();
    params.setTemplate(TemplateParams.templateId(templateSource.getId()));
    params.setEraseContent(true);

    Page updated = getNotionClient().pages().update(target.getId(), params);

    assertEquals(target.getId(), updated.getId());

    BlockList children = getNotionClient().blocks().retrieveChildren(target.getId());
    assertNotNull(children.getResults());
    assertFalse(
        children.getResults().isEmpty(),
        "Target page must contain content copied from the template after the update");
  }

  @Test
  @DisplayName("[IT-39]: Pages - Create page with content and then append more content")
  public void testCreatePageAndAppendContent() {
    Page created =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inPage(pagesTestRootId)
                        .title("[IT-39] Growing page")
                        .children(c -> c.paragraph("Initial paragraph")));

    BlockList beforeAppend = getNotionClient().blocks().retrieveChildren(created.getId());
    assertEquals(1, beforeAppend.getResults().size());

    BlockList afterAppend =
        getNotionClient()
            .blocks()
            .appendChildren(
                created.getId(),
                NotionBlocks.blocksBuilder()
                    .heading2("Added section")
                    .paragraph("Appended paragraph")
                    .bullet("Appended bullet")
                    .build());

    assertEquals(3, afterAppend.getResults().size());

    BlockList all = getNotionClient().blocks().retrieveChildren(created.getId());
    assertEquals(4, all.getResults().size());
    assertEquals("paragraph", all.getResults().get(0).getType());
    assertEquals("heading_2", all.getResults().get(1).getType());
    assertEquals("paragraph", all.getResults().get(2).getType());
    assertEquals("bulleted_list_item", all.getResults().get(3).getType());
  }
}
