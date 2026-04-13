package io.kristixlab.notion.api.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.base.client.ApiClientStub;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.page.CreatePageParams;
import io.kristixlab.notion.api.model.page.MovePageParams;
import io.kristixlab.notion.api.model.page.Page;
import io.kristixlab.notion.api.model.page.PageAsMarkdown;
import io.kristixlab.notion.api.model.page.UpdatePageParams;
import io.kristixlab.notion.api.model.page.markdown.UpdatePageAsMarkdownParams;
import io.kristixlab.notion.api.model.page.property.PageProperty;
import io.kristixlab.notion.api.model.page.property.UnknownProperty;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Pages endpoint behaviors")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PagesEndpointImplTest {

  private ApiClientStub client;
  private PagesEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    client = new ApiClientStub();
    endpoint = new PagesEndpointImpl(client);
  }

  @Nested
  @DisplayName("Create page")
  class Create {

    @Test
    @DisplayName("works for valid create page request")
    void create_withRequest_buildsPostRequest() {
      CreatePageParams request =
          CreatePageParams.builder().parent(Parent.pageParent("parent-1")).title("Roadmap").build();
      Page expected = new Page();
      client.setResponse(expected);

      Page result = endpoint.create(request);

      assertEquals("POST", client.getLastMethod());
      assertEquals("/pages", client.getLastUrlInfo().getUrl());
      assertSame(request, client.getLastBody());
      assertSame(expected, result);
    }

    @Test
    @DisplayName("works for valid create page consumer")
    void create_withConsumer_buildsPostRequest() {
      Page expected = new Page();
      client.setResponse(expected);

      Page result =
          endpoint.create(
              builder -> builder.parent(Parent.pageParent("parent-1")).title("Roadmap"));

      assertEquals("POST", client.getLastMethod());
      assertEquals("/pages", client.getLastUrlInfo().getUrl());
      assertInstanceOf(CreatePageParams.class, client.getLastBody());
      assertSame(expected, result);
    }

    @Test
    @DisplayName("rejects null consumer")
    void create_withConsumer_rejectsNullConsumer() {
      assertThrows(
          IllegalArgumentException.class,
          () -> endpoint.create((java.util.function.Consumer<CreatePageParams.Builder>) null));
    }

    @Test
    @DisplayName("rejects null request")
    void create_withRequest_rejectsNullRequest() {
      assertThrows(IllegalArgumentException.class, () -> endpoint.create((CreatePageParams) null));
    }
  }

  @Nested
  @DisplayName("Retrieve page")
  class Retrieve {

    @Test
    @DisplayName("works for valid page id")
    void retrieve_buildsGetRequest() {
      Page expected = new Page();
      client.setResponse(expected);

      Page result = endpoint.retrieve("page-id-1");

      assertEquals("GET", client.getLastMethod());
      assertEquals("/pages/{page_id}", client.getLastUrlInfo().getUrl());
      assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));
      assertNull(client.getLastBody());
      assertSame(expected, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null page id")
    void retrieve_rejectsBlankOrNullPageId(String pageId) {
      assertThrows(IllegalArgumentException.class, () -> endpoint.retrieve(pageId));
    }
  }

  @Nested
  @DisplayName("Retrieve page as markdown")
  class RetrieveAsMarkdown {

    @Test
    @DisplayName("works for valid page id without 'include transcript' flag")
    void retrieveAsMarkdown_withoutFlag_setsIncludeTranscriptFalse() {
      PageAsMarkdown expected = new PageAsMarkdown();
      client.setResponse(expected);

      PageAsMarkdown result = endpoint.retrieveAsMarkdown("page-id-1");

      assertEquals("GET", client.getLastMethod());
      assertEquals("/pages/{page_id}/markdown", client.getLastUrlInfo().getUrl());
      assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));
      assertEquals(
          List.of("false"), client.getLastUrlInfo().getQueryParams().get("include_transcript"));
      assertSame(expected, result);
    }

    @Test
    @DisplayName("works for valid page id with 'include transcript' flag set to true")
    void retrieveAsMarkdown_withFlag_setsIncludeTranscriptQueryParam() {
      endpoint.retrieveAsMarkdown("page-id-1", true);

      assertEquals("GET", client.getLastMethod());
      assertEquals("/pages/{page_id}/markdown", client.getLastUrlInfo().getUrl());
      assertEquals(
          List.of("true"), client.getLastUrlInfo().getQueryParams().get("include_transcript"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null page id")
    void retrieveAsMarkdown_rejectsBlankOrNullPageId(String pageId) {
      assertThrows(IllegalArgumentException.class, () -> endpoint.retrieveAsMarkdown(pageId));
    }
  }

  @Nested
  @DisplayName("Update page markdown")
  class UpdateAsMarkdown {

    @Test
    @DisplayName("works for valid page id and markdown update request")
    void updateAsMarkdown_buildsPatchRequest() {
      UpdatePageAsMarkdownParams request = UpdatePageAsMarkdownParams.replaceContent("new content");
      PageAsMarkdown expected = new PageAsMarkdown();
      client.setResponse(expected);

      PageAsMarkdown result = endpoint.updateAsMarkdown("page-id-1", request);

      assertEquals("PATCH", client.getLastMethod());
      assertEquals("/pages/{page_id}/markdown", client.getLastUrlInfo().getUrl());
      assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));
      assertSame(request, client.getLastBody());
      assertSame(expected, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null page id")
    void updateAsMarkdown_rejectsBlankOrNullPageId(String pageId) {
      assertThrows(
          IllegalArgumentException.class,
          () -> endpoint.updateAsMarkdown(pageId, UpdatePageAsMarkdownParams.replaceContent("x")));
    }

    @Test
    @DisplayName("rejects null markdown update request")
    void updateAsMarkdown_rejectsNullRequest() {
      assertThrows(
          IllegalArgumentException.class, () -> endpoint.updateAsMarkdown("page-id-1", null));
    }
  }

  @Nested
  @DisplayName("Retrieve page property")
  class RetrieveProperty {

    @Test
    @DisplayName("works for valid page id and property id")
    void retrieveProperty_buildsGetRequest() {
      PageProperty expected = new UnknownProperty();
      client.setResponse(expected);

      PageProperty result = endpoint.retrieveProperty("page-id-1", "title");

      assertEquals("GET", client.getLastMethod());
      assertEquals("/pages/{page_id}/properties/{property_id}", client.getLastUrlInfo().getUrl());
      assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));
      assertEquals("title", client.getLastUrlInfo().getPathParams().get("property_id"));
      assertTrue(client.getLastUrlInfo().getQueryParams().isEmpty());
      assertSame(expected, result);
    }

    @Test
    @DisplayName("works with pagination and adds query params")
    void retrieveProperty_withPagination_addsQueryParams() {
      endpoint.retrieveProperty("page-id-1", "title", "cursor-1", 20);

      assertEquals(
          List.of("cursor-1"), client.getLastUrlInfo().getQueryParams().get("start_cursor"));
      assertEquals(List.of("20"), client.getLastUrlInfo().getQueryParams().get("page_size"));
    }

    @Test
    @DisplayName("decodes property id before adding path param")
    void retrieveProperty_decodesPropertyIdBeforeAddingPathParam() {
      endpoint.retrieveProperty("page-id-1", "title%2Fname");

      assertEquals("title/name", client.getLastUrlInfo().getPathParams().get("property_id"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null page id")
    void retrieveProperty_rejectsBlankOrNullPageId(String pageId) {
      assertThrows(
          IllegalArgumentException.class, () -> endpoint.retrieveProperty(pageId, "title"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null property id")
    void retrieveProperty_rejectsBlankOrNullPropertyId(String propertyId) {
      assertThrows(
          IllegalArgumentException.class, () -> endpoint.retrieveProperty("page-id-1", propertyId));
    }
  }

  @Nested
  @DisplayName("Update page")
  class Update {

    @Test
    @DisplayName("works for valid page id and update request")
    void update_buildsPatchRequest() {
      UpdatePageParams request = UpdatePageParams.builder().title("Updated").build();
      Page expected = new Page();
      client.setResponse(expected);

      Page result = endpoint.update("page-id-1", request);

      assertEquals("PATCH", client.getLastMethod());
      assertEquals("/pages/{page_id}", client.getLastUrlInfo().getUrl());
      assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));
      assertSame(request, client.getLastBody());
      assertSame(expected, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null page id")
    void update_rejectsBlankOrNullPageId(String pageId) {
      assertThrows(
          IllegalArgumentException.class,
          () -> endpoint.update(pageId, UpdatePageParams.builder().title("x").build()));
    }

    @Test
    @DisplayName("rejects null update request")
    void update_rejectsNullRequest() {
      assertThrows(IllegalArgumentException.class, () -> endpoint.update("page-id-1", null));
    }
  }

  @Nested
  @DisplayName("Move page")
  class Move {

    @Test
    @DisplayName("works for valid page id and parent")
    void move_buildsPostRequestWithParent() {
      Parent newParent = Parent.pageParent("parent-2");
      Page expected = new Page();
      client.setResponse(expected);

      Page result = endpoint.move("page-id-1", newParent);

      assertEquals("POST", client.getLastMethod());
      assertEquals("/pages/{page_id}/move", client.getLastUrlInfo().getUrl());
      assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));
      MovePageParams body = assertInstanceOf(MovePageParams.class, client.getLastBody());
      assertSame(newParent, body.getParent());
      assertSame(expected, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null page id")
    void move_rejectsBlankOrNullPageId(String pageId) {
      assertThrows(
          IllegalArgumentException.class, () -> endpoint.move(pageId, Parent.pageParent("p")));
    }

    @Test
    @DisplayName("rejects null parent")
    void move_rejectsNullParent() {
      assertThrows(IllegalArgumentException.class, () -> endpoint.move("page-id-1", null));
    }
  }

  @Nested
  @DisplayName("Delete page")
  class Delete {

    @Test
    @DisplayName("works for valid page id and sets in trash to true")
    void delete_setsInTrashTrue() {
      Page expected = new Page();
      client.setResponse(expected);

      Page result = endpoint.delete("page-id-1");

      assertEquals("PATCH", client.getLastMethod());
      assertEquals("/pages/{page_id}", client.getLastUrlInfo().getUrl());
      assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));

      UpdatePageParams body = (UpdatePageParams) client.getLastBody();
      assertEquals(true, body.getInTrash());
      assertSame(expected, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null page id")
    void delete_rejectsBlankOrNullPageId(String pageId) {
      assertThrows(IllegalArgumentException.class, () -> endpoint.delete(pageId));
    }
  }

  @Nested
  @DisplayName("Restore page")
  class Restore {

    @Test
    @DisplayName("works for valid page id and sets in trash to false")
    void restore_setsInTrashFalse() {
      Page expected = new Page();
      client.setResponse(expected);

      Page result = endpoint.restore("page-id-1");

      assertEquals("PATCH", client.getLastMethod());
      assertEquals("/pages/{page_id}", client.getLastUrlInfo().getUrl());
      assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));

      UpdatePageParams body = (UpdatePageParams) client.getLastBody();
      assertEquals(false, body.getInTrash());
      assertSame(expected, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null page id")
    void restore_rejectsBlankOrNullPageId(String pageId) {
      assertThrows(IllegalArgumentException.class, () -> endpoint.restore(pageId));
    }
  }
}
