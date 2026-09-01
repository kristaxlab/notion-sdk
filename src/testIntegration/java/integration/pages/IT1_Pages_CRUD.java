package integration.pages;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.common.Cover;
import io.kristaxlab.notion.model.page.Page;
import org.junit.jupiter.api.*;
import testkit.WithEmptyTestPage;
import testkit.util.FileLoader;

public class IT1_Pages_CRUD extends WithEmptyTestPage {

  private static final String FIRST_COVER_PATH = "files/it-1/first-cover.jpg";
  private static final String FIRST_COVER_NAME = "first-cover.jpg";

  private static final String SECOND_COVER_PATH = "files/it-1/second-cover.jpg";
  private static final String SECOND_COVER_NAME = "second-cover.jpg";

  private static String firstCoverId;
  private static String secondCoverId;

  @BeforeEach
  public void setup() {
    firstCoverId = FileLoader.uploadFile(FIRST_COVER_PATH, FIRST_COVER_NAME, getSetupClient());
    secondCoverId = FileLoader.uploadFile(SECOND_COVER_PATH, SECOND_COVER_NAME, getSetupClient());
  }

  @Test
  @DisplayName("IT-1: Pages - Basic checks of Pages CRUD operations")
  public void testPagesCrudOperations() {

    // Step 1 = Create a page with a title column, a database endpoint is used to create a
    // page in one shot
    Page created =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inPage(getTestPageId())
                        .title("[IT-1] Initial Title")
                        .icon("🧪")
                        .cover(Cover.fileUpload(firstCoverId))
                        .children(blocks -> blocks.bullets("item1", "item2", "item3")));

    assertNotNull(created);
    assertNotNull(created.getId());
    assertEquals("page_id", created.getParent().getType(), "Parent type should be 'page_id'");

    assertEquals("[IT-1] Initial Title", created.getTitle(), "Page title mismatch");
    assertEquals("file", created.getCover().getType(), "Cover info should be of type 'file'");
    assertNotNull(created.getCover().getFile(), "Cover file info is missing");
    assertEquals("emoji", created.getIcon().getType(), "Icon info should be of type 'emoji'");
    assertEquals("🧪", created.getIcon().getEmoji(), "Icon emoji should match");

    // Step 2 = Retrieve a page
    Page retrieved = getNotionClient().pages().retrieve(created.getId());
    assertEquals(created.getId(), retrieved.getId());

    // Step 3 = Update page title, icon and cover
    Page updated =
        getNotionClient()
            .pages()
            .update(
                retrieved.getId(),
                page -> page.title("[IT-1] Updated title").icon("🟢").cover(secondCoverId));

    assertEquals("[IT-1] Updated title", updated.getTitle(), "Page title mismatch");
    assertEquals("file", updated.getCover().getType(), "Cover info should be of type 'file'");
    assertNotNull(updated.getCover().getFile(), "Cover file info is missing");
    assertNotEquals(
        created.getCover().getFile().getUrl(),
        updated.getCover().getFile().getUrl(),
        "Cover file URL should be updated");
    assertEquals("emoji", updated.getIcon().getType(), "Icon info should be of type 'emoji'");
    assertEquals("🟢", updated.getIcon().getEmoji(), "Icon emoji should match");

    // Step 4 = Move page to trash
    Page deleted = getNotionClient().pages().moveToTrash(updated.getId());
    assertTrue(deleted.getInTrash(), "Page should be deleted");

    // Step 5 = Restore a page
    Page restored = getNotionClient().pages().restore(deleted.getId());
    assertFalse(restored.getInTrash(), "Page should be restored");
  }

  @AfterAll
  public static void tearDown() {}
}
