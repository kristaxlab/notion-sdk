package tests.pages;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.templates.TemplateParams;
import io.kristaxlab.notion.util.PollingConfig;
import io.kristaxlab.notion.util.TemplatePoller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT7_Pages_Duplicate extends WithEmptyTestPage {

  @Test
  @DisplayName("IT-7: Pages - Duplicate page using its id as template id")
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

    // Wait for template to be applied using TemplatePoller
    BlockList content =
        TemplatePoller.awaitBlockCount(
            getNotionClient(),
            duplicated.getId(),
            2,
            PollingConfig.of(ofSeconds(10), ofMillis(500)));
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

    // Wait for template to be applied using TemplatePoller
    BlockList content2 =
        TemplatePoller.awaitBlockCount(
            getNotionClient(),
            duplicatedWithCustomTitle.getId(),
            2,
            PollingConfig.of(ofSeconds(10), ofMillis(500)));
    // refreshing page state after template is fully applied
    duplicatedWithCustomTitle =
        getNotionClient().pages().retrieve(duplicatedWithCustomTitle.getId());

    assertEquals("Duplicated page", duplicatedWithCustomTitle.getTitle());
    assertEquals(2, content2.getResults().size());
    assertEquals("paragraph", content2.getResults().get(0).getType());
    assertEquals("to_do", content2.getResults().get(1).getType());
  }
}
