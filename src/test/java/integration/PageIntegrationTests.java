package integration;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.NotionApiClient;
import io.kristixlab.notion.api.http.exception.ValidationException;
import io.kristixlab.notion.api.model.common.ExternalFile;
import io.kristixlab.notion.api.model.common.FileData;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.pages.CoverParams;
import io.kristixlab.notion.api.model.pages.CreatePageParams;
import io.kristixlab.notion.api.model.pages.Page;
import io.kristixlab.notion.api.model.pages.properties.TitleProperty;
import io.kristixlab.notion.api.util.PagePropertyUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PageIntegrationTests {

  private static NotionApiClient NOTION;
  private static IntegrationTestsSettings SETTINGS;
  private static String PAGE_PARENT_ID = "pages.endpoint.create-page.parent-page";
  private static String EXTERNAL_ICON_URL = "common-media.images.icon-external";

  @BeforeAll
  public static void initClient() {
    NOTION = NotionClientProvider.internalTestingClient();
    SETTINGS = IntegrationTestsSettings.getInstance();

    assertNotNull(
        SETTINGS.getString(PAGE_PARENT_ID),
        "Parent page ID for creating pages is not set in the settings");
  }

  /** INT-1: Standalone pages: create empty page nested into another */
  @Test
  public void createStandalonePageInt1() {
    String title = "INT-1 page " + System.currentTimeMillis();
    CreatePageParams newPage = createPageParams(title);
    newPage.setParent(Parent.pageParent(SETTINGS.getString(PAGE_PARENT_ID)));
    Page page = NOTION.pages().create(newPage);

    // common fields that should present for a newly created page
    assertNotNull(page);
    assertNotNull(page.getId());
    assertNotNull(page.getUrl());
    assertNotNull(page.getCreatedTime());
    assertNotNull(page.getCreatedBy());
    assertNotNull(page.getLastEditedTime());
    assertNotNull(page.getLastEditedBy());

    // checking title property
    assertNotNull(page.getProperties());
    assertEquals(1, page.getProperties().size());
    assertNotNull(page.getProperties().get("title"));
    assertEquals(
        title,
        PagePropertyUtil.asTitle(page.getProperties().get("title"))
            .getTitle()
            .get(0)
            .getText()
            .getContent());
  }

  /** INT-13: Standalone pages: create empty page nested into another */
  @Test
  public void createStandalonePageInt13() {
    String title = "INT-13 page " + System.currentTimeMillis();
    CreatePageParams newPage = createPageParams(title, Parent.pageParent(SETTINGS.getString(PAGE_PARENT_ID)));
    Page page = NOTION.pages().create(newPage);

    // common fields that should present for a newly created page
    assertNotNull(page);
    assertNotNull(page.getId());
    assertNotNull(page.getUrl());
    assertNotNull(page.getCreatedTime());
    assertNotNull(page.getCreatedBy());
    assertNotNull(page.getLastEditedTime());
    assertNotNull(page.getLastEditedBy());

    // checking title property
    assertNotNull(page.getProperties());
    assertEquals(1, page.getProperties().size());
    assertNotNull(page.getProperties().get("title"));
    assertEquals(
            title,
            PagePropertyUtil.asTitle(page.getProperties().get("title"))
                    .getTitle()
                    .get(0)
                    .getText()
                    .getContent());
  }

  @Test
  public void createPageWithoutParent() {
    String title = "Dummy page " + System.currentTimeMillis();
    assertThrows(
        ValidationException.class,
        () -> {
          NOTION.pages().create(createPageParams(title));
        });
  }

  public CreatePageParams createPageParams(String title, Parent parent) {
    CreatePageParams newPage = new CreatePageParams();
    newPage.getProperties().put("title", TitleProperty.of(title));
    newPage.setParent(parent);
    newPage.setIcon(Icon.fromExternalUrl(SETTINGS.getString(EXTERNAL_ICON_URL)));
    newPage.setCover(CoverParams.fromExternalUrl(SETTINGS.getString(EXTERNAL_ICON_URL)));
    return newPage;
  }

  public CreatePageParams createPageParams(String title) {
    CreatePageParams newPage = new CreatePageParams();
    newPage.getProperties().put("title", TitleProperty.of(title));
    return newPage;
  }

  // notes
  // isProp / asProp - for all the prop types, it hides the specific fields
  // move to util class to

}
