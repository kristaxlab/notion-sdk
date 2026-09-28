package tests.search;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.search.SearchFilter;
import io.kristaxlab.notion.model.search.SearchList;
import io.kristaxlab.notion.model.search.SearchParams;
import io.kristaxlab.notion.model.search.SearchSort;
import io.kristaxlab.notion.util.PollingConfig;
import io.kristaxlab.notion.util.SearchPoller;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT41_Search_InTrash extends WithEmptyTestPage {

  private static final String TITLE = "IT-41 unique trashed search title";

  private static final PollingConfig POLLING =
      PollingConfig.of(Duration.ofSeconds(30), Duration.ofMillis(3000));

  @Test
  @DisplayName("IT-41: Search - Find a trashed page by title with in_trash filter")
  public void testSearchInTrash() {
    Page created =
        getNotionClient().pages().create(page -> page.inPage(getTestPageId()).title(TITLE));

    assertNotNull(created.getId());

    Page trashed = getNotionClient().pages().delete(created.getId());
    assertTrue(Boolean.TRUE.equals(trashed.getInTrash()), "Page should be in trash");

    SearchParams params =
        SearchParams.builder()
            .query(TITLE)
            .filter(SearchFilter.pages().withInTrash(true))
            .sort(SearchSort.byLastEditedTimeDescending())
            .build();

    SearchList found =
        SearchPoller.awaitPageId(getNotionClient(), params, created.getId(), POLLING);

    assertTrue(
        found.getResults().stream().anyMatch(object -> created.getId().equals(object.getId())),
        "Trashed page should appear in in_trash search results");
  }
}
