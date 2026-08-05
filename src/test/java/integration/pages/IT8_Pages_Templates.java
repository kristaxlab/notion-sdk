package integration.pages;

import static org.junit.jupiter.api.Assertions.*;

import integration.BaseIntegrationTest;
import integration.extension.NotionFixtureException;
import io.kristaxlab.notion.fluent.NotionBlocksViewer;
import io.kristaxlab.notion.fluent.NotionPageViewer;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.ChildDatabaseBlock;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.templates.Template;
import io.kristaxlab.notion.model.page.templates.TemplateParams;
import io.kristaxlab.notion.model.page.templates.Templates;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.junit.jupiter.api.*;

/**
 * Integration coverage for data source templates: listing (filter + pagination), creating a page
 * from a template, and applying a template to an existing page in append / replace modes.
 *
 * <p>Requires a pre-provisioned data source with at least two named templates (templates cannot be
 * created via the Notion API). Uses the shared "DB with templates" data source.
 */
@Tags({@Tag("advanced"), @Tag("long")})
public class IT8_Pages_Templates extends BaseIntegrationTest {

  private String dataSourceWithTemplates;

  private static final String TITLE_PROP = "Name";
  private static final String TODO_TEMPLATE_NAME = "To Do List";
  private static final String BULLET_TEMPLATE_NAME = "Bulleted List";

  private Template todoTemplate;
  private Template bulletTemplate;

  private int todoTemplateBlockCount;
  private int bulletTemplateBlockCount;

  @BeforeEach
  public void setup() {
    String testPageId = getTestPageId();
    BlockList blocks = getNotionClient().blocks().retrieveChildren(testPageId);
    ChildDatabaseBlock childDb =
        NotionBlocksViewer.of(blocks)
            .first(ChildDatabaseBlock.class)
            .orElseThrow(
                () ->
                    new NotionFixtureException(
                        "IT-8 test requires prerequisite page with a data source in it"));

    dataSourceWithTemplates =
        getSetupClient().databases().retrieve(childDb.getId()).getDataSources().get(0).getId();
    Templates templates = getSetupClient().dataSources().retrieveTemplates(dataSourceWithTemplates);

    assertNotNull(templates);
    assertNotNull(templates.getResults());
    assertEquals(
        2, templates.getResults().size(), "Prerequisite data source must expose 3 templates");

    todoTemplate = findTemplate(templates.getResults(), TODO_TEMPLATE_NAME);
    bulletTemplate = findTemplate(templates.getResults(), BULLET_TEMPLATE_NAME);

    todoTemplateBlockCount =
        getNotionClient().blocks().retrieveChildren(todoTemplate.getId()).getResults().size();
    bulletTemplateBlockCount =
        getNotionClient().blocks().retrieveChildren(bulletTemplate.getId()).getResults().size();
  }

  @Test
  @DisplayName("IT-8: Pages - Data source templates")
  public void testDataSourceTemplates() {
    // 1. List all templates
    checkRetrieveTemplates();

    // 2. List templates with name filter (documented case-insensitive substring match)
    checkRetrieveTemplatesFiltered();

    // 2b. Pagination with page_size
    checkRetrieveTemplatesPaginated();

    // 3. Create a page with default (bullet) template
    checkCreateWithDefaultTemplate();

    // 4. Create a page with a to do template
    checkCreateWithTemplateId();

    // 5. Complex scenario - create an page with paragraph block, then append bullet template,
    // then append to do template, then replace all with bullet template
    checkCreateAppendReplace();
  }

  private void checkCreateAppendReplace() {
    Page pageWithText =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inDataSource(dataSourceWithTemplates)
                        .title("Page with text")
                        .children(b -> b.paragraph("simple text")));

    BlockList children1 = getNotionClient().blocks().retrieveChildren(pageWithText.getId());
    assertTrue(containsBlockType(children1, "paragraph"));
    assertTrue(propertyValueEquals(pageWithText, "Type", null));

    // appending the bulleted list template (+2 blocks and selec property should be updated)
    Page bulletAppended =
        getNotionClient()
            .pages()
            .update(
                pageWithText.getId(),
                params -> params.template(TemplateParams.templateId(bulletTemplate.getId())));

    BlockList children2 = waitForChildren(bulletAppended.getId(), 1 + bulletTemplateBlockCount);
    assertTrue(containsBlockType(children2, "paragraph"));
    assertTrue(containsBlockType(children2, "bulleted_list_item"));
    bulletAppended = getNotionClient().pages().retrieve(bulletAppended.getId());
    assertTrue(propertyValueEquals(bulletAppended, "Type", "Bulleted List"));

    // appending the to do  list template (+2 more blocks and selec property should NOT be updated
    // as it already has value)
    Page bulletAndTodoAppended =
        getNotionClient()
            .pages()
            .update(
                pageWithText.getId(),
                params -> params.template(TemplateParams.templateId(todoTemplate.getId())));

    BlockList children3 =
        waitForChildren(
            bulletAndTodoAppended.getId(), 1 + bulletTemplateBlockCount + todoTemplateBlockCount);
    assertTrue(containsBlockType(children3, "paragraph"));
    assertTrue(containsBlockType(children3, "bulleted_list_item"));
    assertTrue(containsBlockType(children3, "to_do"));
    bulletAndTodoAppended = getNotionClient().pages().retrieve(bulletAndTodoAppended.getId());
    // applied template does not change properties that already have values
    assertFalse(propertyValueEquals(bulletAndTodoAppended, "Type", "To Do List"));

    // apply a to do template in replace mode
    getNotionClient()
        .pages()
        .update(
            bulletAndTodoAppended.getId(),
            params ->
                params
                    .template(TemplateParams.templateId(todoTemplate.getId()))
                    .eraseContent(true));

    // Replace is async: wait until old content is gone and the new template is present.
    // A plain min-count wait is insufficient because the previous append already had enough blocks.
    BlockList replaced = waitForChildren(bulletAndTodoAppended.getId(), todoTemplateBlockCount);
    assertTrue(containsBlockType(replaced, "to_do"));
    assertFalse(containsBlockType(replaced, "paragraph"));
    assertFalse(containsBlockType(replaced, "bulleted_list_item"));
  }

  private void checkCreateWithTemplateId() {
    Page fromToDoTemplate =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inDataSource(dataSourceWithTemplates)
                        .template(TemplateParams.templateId(todoTemplate.getId())));

    BlockList fromTemplateChildren =
        waitForChildren(fromToDoTemplate.getId(), todoTemplateBlockCount);
    assertTrue(containsBlockType(fromTemplateChildren, "to_do"));
  }

  private void checkCreateWithDefaultTemplate() {
    Page fromDefaultBulletTemplate =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inDataSource(dataSourceWithTemplates)
                        .template(TemplateParams.defaultTemplate()));

    BlockList fromDefaultBulletTemplateChildren =
        waitForChildren(fromDefaultBulletTemplate.getId(), bulletTemplateBlockCount);
    assertTrue(containsBlockType(fromDefaultBulletTemplateChildren, "bulleted_list_item"));
  }

  private void checkRetrieveTemplatesPaginated() {
    Templates firstPage =
        getNotionClient().dataSources().retrieveTemplates(dataSourceWithTemplates, null, 1);
    assertEquals(1, firstPage.getResults().size());
    assertEquals(Boolean.TRUE, firstPage.getHasMore());
    assertNotNull(firstPage.getNextCursor());

    Templates secondPage =
        getNotionClient()
            .dataSources()
            .retrieveTemplates(dataSourceWithTemplates, firstPage.getNextCursor(), 1);
    assertEquals(1, secondPage.getResults().size());
    assertNotEquals(firstPage.getResults().get(0).getId(), secondPage.getResults().get(0).getId());
  }

  private void checkRetrieveTemplatesFiltered() {
    Templates filtered =
        getNotionClient()
            .dataSources()
            .retrieveTemplates(dataSourceWithTemplates, "bullet", null, null);

    assertEquals(1, filtered.getResults().size());
    assertEquals(BULLET_TEMPLATE_NAME, filtered.getResults().get(0).getName());
    assertEquals(bulletTemplate.getId(), filtered.getResults().get(0).getId());
  }

  private void checkRetrieveTemplates() {
    Templates all = getNotionClient().dataSources().retrieveTemplates(dataSourceWithTemplates);
    assertNotNull(all.getResults());
    assertEquals(Boolean.FALSE, all.getHasMore());
    assertNull(all.getNextCursor());

    Set<String> names =
        all.getResults().stream().map(Template::getName).collect(Collectors.toSet());
    assertTrue(names.contains(TODO_TEMPLATE_NAME));
    assertTrue(names.contains(BULLET_TEMPLATE_NAME));
  }

  @AfterAll
  public static void tearDown() {}

  private static Template findTemplate(List<Template> templates, String name) {
    return templates.stream()
        .filter(t -> name.equals(t.getName()))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalStateException(
                    "Template '" + name + "' is missing from prerequisite data source"));
  }

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

  private static boolean containsBlockType(BlockList blocks, String type) {
    return blocks.getResults().stream().anyMatch(b -> type.equals(b.getType()));
  }

  private static boolean propertyValueEquals(Page page, String propertyName, String value) {
    if (value == null) {
      return NotionPageViewer.of(page).select("Type") == null;
    }
    return value.equals(NotionPageViewer.of(page).select("Type"));
  }
}
