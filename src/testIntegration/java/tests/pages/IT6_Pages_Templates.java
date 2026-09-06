package tests.pages;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.fluent.NotionBlocksViewer;
import io.kristaxlab.notion.fluent.NotionPageViewer;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.ChildDatabaseBlock;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.templates.Template;
import io.kristaxlab.notion.model.page.templates.TemplateParams;
import io.kristaxlab.notion.model.page.templates.Templates;
import io.kristaxlab.notion.util.PollingConfig;
import io.kristaxlab.notion.util.TemplatePoller;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.*;
import testkit.WithTestPageFixture;
import testkit.ext.NotionWorkspaseException;

/**
 * Integration coverage for data source templates: listing (filter + pagination), creating a page
 * from a template, and applying a template to an existing page in append / replace modes.
 *
 * <p>Requires a pre-provisioned data source with at least two named templates (templates cannot be
 * created via the Notion API). Uses the shared "DB with templates" data source.
 */
@Tag("heavy")
public class IT6_Pages_Templates extends WithTestPageFixture {

  private String dataSourceWithTemplates;

  private static final String TITLE_PROP = "Name";
  private static final String TODO_TEMPLATE_NAME = "To Do List";
  private static final String BULLET_TEMPLATE_NAME = "Bulleted List";
  private static final PollingConfig TEMPLATE_POLLING =
      PollingConfig.of(ofSeconds(10), ofMillis(500));

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
                    new NotionWorkspaseException(
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
  @DisplayName("IT-6: Pages - Data source templates")
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

    BlockList children2 =
        TemplatePoller.awaitBlockCount(
            getNotionClient(),
            bulletAppended.getId(),
            1 + bulletTemplateBlockCount,
            TEMPLATE_POLLING);
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
        TemplatePoller.awaitBlockCount(
            getNotionClient(),
            bulletAndTodoAppended.getId(),
            1 + bulletTemplateBlockCount + todoTemplateBlockCount,
            TEMPLATE_POLLING);
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
    BlockList replaced =
        TemplatePoller.awaitBlocks(
            getNotionClient(),
            bulletAndTodoAppended.getId(),
            blocks ->
                blocks.getResults() != null
                    && blocks.getResults().size() == todoTemplateBlockCount
                    && containsBlockType(blocks, "to_do")
                    && !containsBlockType(blocks, "paragraph")
                    && !containsBlockType(blocks, "bulleted_list_item"),
            TEMPLATE_POLLING);
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
        TemplatePoller.awaitBlockCount(
            getNotionClient(), fromToDoTemplate.getId(), todoTemplateBlockCount, TEMPLATE_POLLING);
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
        TemplatePoller.awaitBlockCount(
            getNotionClient(),
            fromDefaultBulletTemplate.getId(),
            bulletTemplateBlockCount,
            TEMPLATE_POLLING);
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
