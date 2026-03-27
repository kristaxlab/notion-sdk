package io.kristixlab.notion.api.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.base.client.ApiClientStub;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.pages.CreatePageParams;
import io.kristixlab.notion.api.model.pages.MovePageParams;
import io.kristixlab.notion.api.model.pages.UpdatePageAsMarkdownParams;
import io.kristixlab.notion.api.model.pages.UpdatePageParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class PagesEndpointImplTest {

  private ApiClientStub client;
  private PagesEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    client = new ApiClientStub();
    endpoint = new PagesEndpointImpl(client);
  }

  @Test
  void create() {
    CreatePageParams request = new CreatePageParams();

    endpoint.create(request);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/pages", client.getLastUrlInfo().getUrl());
    assertSame(request, client.getLastBody());
  }

  @Test
  void create_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.create(null));
  }

  @Test
  void retrieveById() {
    endpoint.retrieve("page-id-1");

    assertEquals("GET", client.getLastMethod());
    assertEquals("/pages/{page_id}", client.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void retrieve_rejectsBlankOrNullPageId(String pageId) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.retrieve(pageId));
  }

  @Test
  void retrieveProperty() {
    endpoint.retrieveProperty("page-id-1", "prop-id-1");

    assertEquals("GET", client.getLastMethod());
    assertEquals("/pages/{page_id}/properties/{property_id}", client.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));
    assertEquals("prop-id-1", client.getLastUrlInfo().getPathParams().get("property_id"));
    assertTrue(client.getLastUrlInfo().getQueryParams().isEmpty());
  }

  @Test
  void retrieveProperty_withStartCursor() {
    endpoint.retrieveProperty("page-id-1", "prop-id-1", "cursor-abc", null);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/pages/{page_id}/properties/{property_id}", client.getLastUrlInfo().getUrl());
    assertEquals(
        java.util.List.of("cursor-abc"),
        client.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("page_size"));
  }

  @Test
  void retrieveProperty_withPageSize() {
    endpoint.retrieveProperty("page-id-1", "prop-id-1", null, 10);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/pages/{page_id}/properties/{property_id}", client.getLastUrlInfo().getUrl());
    assertEquals(
        java.util.List.of("10"), client.getLastUrlInfo().getQueryParams().get("page_size"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("start_cursor"));
  }

  @Test
  void retrieveProperty_withBothPaginationParams() {
    endpoint.retrieveProperty("page-id-1", "prop-id-1", "cursor-abc", 10);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/pages/{page_id}/properties/{property_id}", client.getLastUrlInfo().getUrl());
    assertEquals(
        java.util.List.of("cursor-abc"),
        client.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertEquals(
        java.util.List.of("10"), client.getLastUrlInfo().getQueryParams().get("page_size"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void retrieveProperty_rejectsBlankOrNullPageId(String pageId) {
    assertThrows(
        IllegalArgumentException.class,
        () -> endpoint.retrieveProperty(pageId, "prop-id-1", null, null));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void retrieveProperty_rejectsBlankOrNullPropertyId(String propertyId) {
    assertThrows(
        IllegalArgumentException.class,
        () -> endpoint.retrieveProperty("page-id-1", propertyId, null, null));
  }

  @Test
  void update() {
    UpdatePageParams request = new UpdatePageParams();

    endpoint.update("page-id-1", request);

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/pages/{page_id}", client.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));
    assertSame(request, client.getLastBody());
  }

  @Test
  void update_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.update("page-id-1", null));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void update_rejectsBlankOrNullPageId(String pageId) {
    assertThrows(
        IllegalArgumentException.class, () -> endpoint.update(pageId, new UpdatePageParams()));
  }

  @Test
  void delete() {
    endpoint.delete("page-id-1");

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/pages/{page_id}", client.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));
    assertTrue(((UpdatePageParams) client.getLastBody()).getInTrash());
  }

  @Test
  void restore() {
    endpoint.restore("page-id-1");

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/pages/{page_id}", client.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));
    assertFalse(((UpdatePageParams) client.getLastBody()).getInTrash());
  }

  // --- retrieveAsMarkdown ---

  @Test
  void retrieveAsMarkdown() {
    endpoint.retrieveAsMarkdown("page-id-1");

    assertEquals("GET", client.getLastMethod());
    assertEquals("/pages/{page_id}/markdown", client.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));
    assertEquals(
        java.util.List.of("false"),
        client.getLastUrlInfo().getQueryParams().get("include_transcript"));
    assertNull(client.getLastBody());
  }

  @Test
  void retrieveAsMarkdown_withIncludeTranscript() {
    endpoint.retrieveAsMarkdown("page-id-1", true);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/pages/{page_id}/markdown", client.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));
    assertEquals(
        java.util.List.of("true"),
        client.getLastUrlInfo().getQueryParams().get("include_transcript"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void retrieveAsMarkdown_rejectsBlankOrNullPageId(String pageId) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.retrieveAsMarkdown(pageId));
  }

  // --- updateAsMarkdown ---

  @Test
  void updateAsMarkdown() {
    UpdatePageAsMarkdownParams request = UpdatePageAsMarkdownParams.replaceContent("# Hello");

    endpoint.updateAsMarkdown("page-id-1", request);

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/pages/{page_id}/markdown", client.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));
    assertSame(request, client.getLastBody());
  }

  @Test
  void updateAsMarkdown_rejectsNullRequest() {
    assertThrows(
        IllegalArgumentException.class, () -> endpoint.updateAsMarkdown("page-id-1", null));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void updateAsMarkdown_rejectsBlankOrNullPageId(String pageId) {
    assertThrows(
        IllegalArgumentException.class,
        () -> endpoint.updateAsMarkdown(pageId, UpdatePageAsMarkdownParams.replaceContent("")));
  }

  // --- move ---

  @Test
  void move() {
    Parent newParent = Parent.pageParent("parent-page-id");

    endpoint.move("page-id-1", newParent);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/pages/{page_id}/move", client.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", client.getLastUrlInfo().getPathParams().get("page_id"));
    assertSame(newParent, ((MovePageParams) client.getLastBody()).getParent());
  }

  @Test
  void move_rejectsNullParent() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.move("page-id-1", null));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void move_rejectsBlankOrNullPageId(String pageId) {
    assertThrows(
        IllegalArgumentException.class,
        () -> endpoint.move(pageId, Parent.pageParent("parent-page-id")));
  }
}
