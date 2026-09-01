package tests.pages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.UpdatePageParams;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT40_Pages_Lock extends WithEmptyTestPage {

  @Test
  @DisplayName("IT-40: Pages - Lock and unlock a page")
  public void testLockAndUnlockPage() {
    Page locked =
        getNotionClient()
            .pages()
            .update(getTestPageId(), UpdatePageParams.builder().locked(true).build());
    assertEquals(Boolean.TRUE, locked.getIsLocked());

    Page unlocked =
        getNotionClient()
            .pages()
            .update(getTestPageId(), UpdatePageParams.builder().locked(false).build());
    assertEquals(Boolean.FALSE, unlocked.getIsLocked());
  }

  @AfterAll
  public static void tearDown() {}
}
