package tests.search;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.search.SearchFilter;
import io.kristaxlab.notion.model.search.SearchList;
import io.kristaxlab.notion.model.search.SearchParams;
import io.kristaxlab.notion.util.PollingConfig;
import io.kristaxlab.notion.util.SearchPoller;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT40_Search_ByTitle extends WithEmptyTestPage {

  private static final String TITLE = "IT-40 unique search title";

  private static final PollingConfig POLLING =
      PollingConfig.of(Duration.ofSeconds(30), Duration.ofMillis(2000));

  @Test
  @DisplayName("IT-40: Search - Find a newly created page by title")
  public void testSearchByTitle() {
    Page created =
        getNotionClient().pages().create(page -> page.inPage(getTestPageId()).title(TITLE));

    assertNotNull(created.getId());

    SearchParams params = SearchParams.builder().query(TITLE).filter(SearchFilter.pages()).build();

    SearchList found =
        SearchPoller.awaitPageId(getNotionClient(), params, created.getId(), POLLING);

    assertTrue(
        found.getResults().stream().anyMatch(object -> created.getId().equals(object.getId())),
        "Created page should appear in search results");
  }
}
