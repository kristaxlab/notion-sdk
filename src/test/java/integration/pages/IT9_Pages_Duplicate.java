package integration.pages;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.templates.TemplateParams;
import java.util.function.Predicate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT9_Pages_Duplicate extends WithEmptyTestPage {

  @Test
  @DisplayName("IT-9: Pages - Duplicate page using its id as template id")
  public void testDuplicatePageUsingTemplateId() {
    // 1. Creating a source page with title and 2 blocks
    Page source =
        getNotionClient()
            .pages()
            .create(
                p ->
                    p.inPage(getTestPageId())
                        .title("Page for testing duplication")
                        .children(c -> c.paragraph("Simple texte").todo("To do")));

    // 2. Duplicating a source page
    Page duplicated =
        getNotionClient()
            .pages()
            .create(
                p -> p.inPage(getTestPageId()).template(TemplateParams.templateId(source.getId())));

    BlockList content = waitForChildren(duplicated.getId(), 2);
    // refreshing page state after template is fully applied
    duplicated = getNotionClient().pages().retrieve(duplicated.getId());

    assertEquals(source.getTitle(), duplicated.getTitle());
    assertEquals(2, content.getResults().size());
    assertEquals("paragraph", content.getResults().get(0).getType());
    assertEquals("to_do", content.getResults().get(1).getType());

    // 3. Duplicating a source page but setting a custom title
    Page duplicatedWithCustomTitle =
        getNotionClient()
            .pages()
            .create(
                p ->
                    p.inPage(getTestPageId())
                        .title("Duplicated page")
                        .template(TemplateParams.templateId(source.getId())));

    BlockList content2 = waitForChildren(duplicatedWithCustomTitle.getId(), 2);
    // refreshing page state after template is fully applied
    duplicatedWithCustomTitle =
        getNotionClient().pages().retrieve(duplicatedWithCustomTitle.getId());

    assertEquals("Duplicated page", duplicatedWithCustomTitle.getTitle());
    assertEquals(2, content2.getResults().size());
    assertEquals("paragraph", content2.getResults().get(0).getType());
    assertEquals("to_do", content2.getResults().get(1).getType());
  }

  // applying template works asynchronously
  private BlockList waitForChildren(String pageId, int bloeckCount) {
    return waitForChildren(
        pageId, blocks -> blocks.getResults() != null && blocks.getResults().size() == bloeckCount);
  }

  private BlockList waitForChildren(String pageId, Predicate<BlockList> ready) {
    BlockList last = null;
    for (int attempt = 0; attempt < 20; attempt++) {
      last = getNotionClient().blocks().retrieveChildren(pageId);
      if (last != null && ready.test(last)) {
        return last;
      }
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        fail("Interrupted while waiting for template content on page " + pageId);
      }
    }
    fail(
        "Timed out waiting for template content on page "
            + pageId
            + "; last count="
            + (last == null || last.getResults() == null ? 0 : last.getResults().size()));
    return last;
  }
}
