package io.kristixlab.notion.api.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.ApiClientStub;
import io.kristixlab.notion.api.model.blocks.AppendBlockChildrenParams;
import io.kristixlab.notion.api.model.blocks.Block;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class BlocksEndpointImplTest {

  private ApiClientStub client;
  private BlocksEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    client = new ApiClientStub();
    endpoint = new BlocksEndpointImpl(client);
  }

  @Test
  void retrieveById() {
    endpoint.retrieve("block-id-1");

    assertEquals("GET", client.getLastMethod());
    assertEquals("/blocks/{block_id}", client.getLastUrlInfo().getUrl());
    assertEquals("block-id-1", client.getLastUrlInfo().getPathParams().get("block_id"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void retrieve_rejectsBlankOrNullBlockId(String id) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.retrieve(id));
  }

  @Test
  void retrieveChildren() {
    endpoint.retrieveChildren("block-id-1");

    assertEquals("GET", client.getLastMethod());
    assertEquals("/blocks/{block_id}/children", client.getLastUrlInfo().getUrl());
    assertEquals("block-id-1", client.getLastUrlInfo().getPathParams().get("block_id"));
    assertTrue(client.getLastUrlInfo().getQueryParams().isEmpty());
  }

  @Test
  void retrieveChildren_withStartCursor() {
    endpoint.retrieveChildren("block-id-1", "cursor-abc", null);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/blocks/{block_id}/children", client.getLastUrlInfo().getUrl());
    assertEquals("block-id-1", client.getLastUrlInfo().getPathParams().get("block_id"));
    assertEquals(
        List.of("cursor-abc"), client.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("page_size"));
  }

  @Test
  void retrieveChildren_withPageSize() {
    endpoint.retrieveChildren("block-id-1", null, 25);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/blocks/{block_id}/children", client.getLastUrlInfo().getUrl());
    assertEquals("block-id-1", client.getLastUrlInfo().getPathParams().get("block_id"));
    assertEquals(List.of("25"), client.getLastUrlInfo().getQueryParams().get("page_size"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("start_cursor"));
  }

  @Test
  void retrieveChildren_withBothPaginationParams() {
    endpoint.retrieveChildren("block-id-1", "cursor-abc", 25);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/blocks/{block_id}/children", client.getLastUrlInfo().getUrl());
    assertEquals("block-id-1", client.getLastUrlInfo().getPathParams().get("block_id"));
    assertEquals(
        List.of("cursor-abc"), client.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertEquals(List.of("25"), client.getLastUrlInfo().getQueryParams().get("page_size"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void retrieveChildren_rejectsBlankOrNullBlockId(String id) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.retrieveChildren(id));
  }

  @Test
  void appendChildren() {
    AppendBlockChildrenParams request = new AppendBlockChildrenParams();

    endpoint.appendChildren("block-id-1", request);

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/blocks/{block_id}/children", client.getLastUrlInfo().getUrl());
    assertEquals("block-id-1", client.getLastUrlInfo().getPathParams().get("block_id"));
    assertSame(request, client.getLastBody());
  }

  @Test
  void appendChildren_withSingleBlock() {
    Block child = new Block();

    endpoint.appendChildren("block-id-1", child);

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/blocks/{block_id}/children", client.getLastUrlInfo().getUrl());
    assertEquals("block-id-1", client.getLastUrlInfo().getPathParams().get("block_id"));
    AppendBlockChildrenParams body = (AppendBlockChildrenParams) client.getLastBody();
    assertEquals(1, body.getChildren().size());
    assertSame(child, body.getChildren().get(0));
  }

  @Test
  void appendChildren_rejectsNullRequest() {
    assertThrows(
        IllegalArgumentException.class,
        () -> endpoint.appendChildren("block-id-1", (AppendBlockChildrenParams) null));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void appendChildren_rejectsBlankOrNullBlockId(String id) {
    assertThrows(
        IllegalArgumentException.class,
        () -> endpoint.appendChildren(id, new AppendBlockChildrenParams()));
  }

  @Test
  void update() {
    Block request = new Block();

    endpoint.update("block-id-1", request);

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/blocks/{block_id}", client.getLastUrlInfo().getUrl());
    assertEquals("block-id-1", client.getLastUrlInfo().getPathParams().get("block_id"));
    assertSame(request, client.getLastBody());
  }

  @Test
  void update_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.update("block-id-1", null));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void update_rejectsBlankOrNullBlockId(String id) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.update(id, new Block()));
  }

  @Test
  void delete() {
    endpoint.delete("block-id-1");

    assertEquals("DELETE", client.getLastMethod());
    assertEquals("/blocks/{block_id}", client.getLastUrlInfo().getUrl());
    assertEquals("block-id-1", client.getLastUrlInfo().getPathParams().get("block_id"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void delete_rejectsBlankOrNullBlockId(String id) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.delete(id));
  }

  @Test
  void restore() {
    endpoint.restore("block-id-1");

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/blocks/{block_id}", client.getLastUrlInfo().getUrl());
    assertEquals("block-id-1", client.getLastUrlInfo().getPathParams().get("block_id"));
    assertFalse(((Block) client.getLastBody()).getInTrash());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void restore_rejectsBlankOrNullBlockId(String id) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.restore(id));
  }
}
