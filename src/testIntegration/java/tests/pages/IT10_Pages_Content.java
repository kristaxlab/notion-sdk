package tests.pages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.page.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT10_Pages_Content extends WithEmptyTestPage {

  @Test
  @DisplayName("IT-10: Pages - Create page with content and then append more content")
  public void testCreatePageAndAppendContent() {
    Page created =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inPage(getTestPageId())
                        .title("Growing page")
                        .children(c -> c.paragraph("Initial paragraph")));

    BlockList beforeAppend = getNotionClient().blocks().retrieveChildren(created.getId());
    assertEquals(1, beforeAppend.getResults().size());

    getNotionClient()
        .blocks()
        .appendChildren(
            created.getId(),
            b ->
                b.heading2("Added section")
                    .paragraph("Appended paragraph")
                    .bullet("Appended bullet")
                    .build());

    BlockList afterAppend = getNotionClient().blocks().retrieveChildren(created.getId());
    assertEquals(4, afterAppend.getResults().size());
    assertEquals("paragraph", afterAppend.getResults().get(0).getType());
    assertEquals("heading_2", afterAppend.getResults().get(1).getType());
    assertEquals("paragraph", afterAppend.getResults().get(2).getType());
    assertEquals("bulleted_list_item", afterAppend.getResults().get(3).getType());

    getNotionClient().blocks().moveToTrash(afterAppend.getResults().get(1).getId());

    BlockList afterDelete = getNotionClient().blocks().retrieveChildren(created.getId());
    assertEquals(3, afterDelete.getResults().size());
    assertEquals("paragraph", afterDelete.getResults().get(0).getType());
    assertEquals("paragraph", afterDelete.getResults().get(1).getType());
    assertEquals("bulleted_list_item", afterDelete.getResults().get(2).getType());
  }
}
