package io.kristixlab.notion.api.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.TransportStub;
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

  private TransportStub transport;
  private PagesEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    transport = new TransportStub();
    endpoint = new PagesEndpointImpl(transport);
  }

  @Test
  void create() {
    CreatePageParams request = new CreatePageParams();

    endpoint.create(request);

    assertEquals("POST", transport.getLastMethod());
    assertEquals("/pages", transport.getLastUrlInfo().getUrl());
    assertSame(request, transport.getLastBody());
  }

  @Test
  void create_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.create(null));
  }

  @Test
  void retrieveById() {
    endpoint.retrieve("page-id-1");

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/pages/{page_id}", transport.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", transport.getLastUrlInfo().getPathParams().get("page_id"));
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

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/pages/{page_id}/properties/{property_id}", transport.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", transport.getLastUrlInfo().getPathParams().get("page_id"));
    assertEquals("prop-id-1", transport.getLastUrlInfo().getPathParams().get("property_id"));
    assertTrue(transport.getLastUrlInfo().getQueryParams().isEmpty());
  }

  @Test
  void retrieveProperty_withStartCursor() {
    endpoint.retrieveProperty("page-id-1", "prop-id-1", "cursor-abc", null);

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/pages/{page_id}/properties/{property_id}", transport.getLastUrlInfo().getUrl());
    assertEquals(
        java.util.List.of("cursor-abc"),
        transport.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertFalse(transport.getLastUrlInfo().getQueryParams().containsKey("page_size"));
  }

  @Test
  void retrieveProperty_withPageSize() {
    endpoint.retrieveProperty("page-id-1", "prop-id-1", null, 10);

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/pages/{page_id}/properties/{property_id}", transport.getLastUrlInfo().getUrl());
    assertEquals(
        java.util.List.of("10"), transport.getLastUrlInfo().getQueryParams().get("page_size"));
    assertFalse(transport.getLastUrlInfo().getQueryParams().containsKey("start_cursor"));
  }

  @Test
  void retrieveProperty_withBothPaginationParams() {
    endpoint.retrieveProperty("page-id-1", "prop-id-1", "cursor-abc", 10);

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/pages/{page_id}/properties/{property_id}", transport.getLastUrlInfo().getUrl());
    assertEquals(
        java.util.List.of("cursor-abc"),
        transport.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertEquals(
        java.util.List.of("10"), transport.getLastUrlInfo().getQueryParams().get("page_size"));
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

    assertEquals("PATCH", transport.getLastMethod());
    assertEquals("/pages/{page_id}", transport.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", transport.getLastUrlInfo().getPathParams().get("page_id"));
    assertSame(request, transport.getLastBody());
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

    assertEquals("PATCH", transport.getLastMethod());
    assertEquals("/pages/{page_id}", transport.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", transport.getLastUrlInfo().getPathParams().get("page_id"));
    assertTrue(((UpdatePageParams) transport.getLastBody()).getInTrash());
  }

  @Test
  void restore() {
    endpoint.restore("page-id-1");

    assertEquals("PATCH", transport.getLastMethod());
    assertEquals("/pages/{page_id}", transport.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", transport.getLastUrlInfo().getPathParams().get("page_id"));
    assertFalse(((UpdatePageParams) transport.getLastBody()).getInTrash());
  }

  // --- retrieveAsMarkdown ---

  @Test
  void retrieveAsMarkdown() {
    endpoint.retrieveAsMarkdown("page-id-1");

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/pages/{page_id}/markdown", transport.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", transport.getLastUrlInfo().getPathParams().get("page_id"));
    assertEquals(
        java.util.List.of("false"),
        transport.getLastUrlInfo().getQueryParams().get("include_transcript"));
    assertNull(transport.getLastBody());
  }

  @Test
  void retrieveAsMarkdown_withIncludeTranscript() {
    endpoint.retrieveAsMarkdown("page-id-1", true);

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/pages/{page_id}/markdown", transport.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", transport.getLastUrlInfo().getPathParams().get("page_id"));
    assertEquals(
        java.util.List.of("true"),
        transport.getLastUrlInfo().getQueryParams().get("include_transcript"));
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

    assertEquals("PATCH", transport.getLastMethod());
    assertEquals("/pages/{page_id}/markdown", transport.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", transport.getLastUrlInfo().getPathParams().get("page_id"));
    assertSame(request, transport.getLastBody());
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

    assertEquals("POST", transport.getLastMethod());
    assertEquals("/pages/{page_id}/move", transport.getLastUrlInfo().getUrl());
    assertEquals("page-id-1", transport.getLastUrlInfo().getPathParams().get("page_id"));
    assertSame(newParent, ((MovePageParams) transport.getLastBody()).getParent());
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
