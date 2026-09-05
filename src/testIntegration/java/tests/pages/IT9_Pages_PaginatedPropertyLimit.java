package tests.pages;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.kristaxlab.notion.fluent.NotionText;
import io.kristaxlab.notion.http.error.ValidationException;
import io.kristaxlab.notion.model.common.richtext.RichText;
import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.page.Page;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT9_Pages_PaginatedPropertyLimit extends WithEmptyTestPage {

  private static final String NOTES_PROP = "Notes";
  private String dataSourceId;

  @BeforeEach
  public void setup() {
    dataSourceId =
        getSetupClient()
            .databases()
            .create(
                CreateDatabaseParams.builder()
                    .inPage(getTestPageId())
                    .title("Test Database")
                    .properties(p -> p.richText(NOTES_PROP).checkbox("Done"))
                    .build())
            .getDataSources()
            .get(0)
            .getId();
  }

  @Test
  @DisplayName("IT-9: Pages - Checks that there is a limit for rich text property")
  public void testLimit() {

    Page newPage =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inDataSource(dataSourceId)
                        .properties(
                            prop ->
                                prop.richText(NOTES_PROP, createRichTexts("text", 100))
                                    .checkbox("Done", true)));

    assertThrows(
        ValidationException.class,
        () ->
            getNotionClient()
                .pages()
                .create(
                    page ->
                        page.inDataSource(dataSourceId)
                            .properties(
                                prop ->
                                    prop.richText(NOTES_PROP, createRichTexts("text", 101))
                                        .checkbox("Done", true))));
  }

  private List<RichText> createRichTexts(String text, int times) {
    List<RichText> richTexts = new ArrayList<>();
    for (int i = 0; i < times; i++) {
      richTexts.add(NotionText.plainText(text));
    }
    return richTexts;
  }
}
