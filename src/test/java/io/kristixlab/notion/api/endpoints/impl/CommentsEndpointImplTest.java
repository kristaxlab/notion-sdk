package io.kristixlab.notion.api.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.TransportStub;
import io.kristixlab.notion.api.model.comments.CreateCommentParams;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CommentsEndpointImplTest {

  private TransportStub transport;
  private CommentsEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    transport = new TransportStub();
    endpoint = new CommentsEndpointImpl(transport);
  }

  @Test
  void create() {
    CreateCommentParams request = new CreateCommentParams();

    endpoint.create(request);

    assertEquals("POST", transport.getLastMethod());
    assertEquals("/comments", transport.getLastUrlInfo().getUrl());
    assertSame(request, transport.getLastBody());
  }

  @Test
  void create_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.create(null));
  }

  @Test
  void retrieveById() {
    endpoint.retrieve("comment-id-1");

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/comments/{comment_id}", transport.getLastUrlInfo().getUrl());
    assertEquals("comment-id-1", transport.getLastUrlInfo().getPathParams().get("comment_id"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void retrieve_rejectsBlankOrNullCommentId(String id) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.retrieve(id));
  }

  @Test
  void listComments() {
    endpoint.listComments("block-id-1");

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/comments", transport.getLastUrlInfo().getUrl());
    assertTrue(transport.getLastUrlInfo().getQueryParams().containsKey("block_id"));
    assertFalse(transport.getLastUrlInfo().getQueryParams().containsKey("start_cursor"));
    assertFalse(transport.getLastUrlInfo().getQueryParams().containsKey("page_size"));
  }

  @Test
  void listComments_withStartCursor() {
    endpoint.listComments("block-id-1", "cursor-abc", null);

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/comments", transport.getLastUrlInfo().getUrl());
    assertTrue(transport.getLastUrlInfo().getQueryParams().containsKey("block_id"));
    assertEquals(
        List.of("cursor-abc"), transport.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertFalse(transport.getLastUrlInfo().getQueryParams().containsKey("page_size"));
  }

  @Test
  void listComments_withPageSize() {
    endpoint.listComments("block-id-1", null, 25);

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/comments", transport.getLastUrlInfo().getUrl());
    assertTrue(transport.getLastUrlInfo().getQueryParams().containsKey("block_id"));
    assertEquals(List.of("25"), transport.getLastUrlInfo().getQueryParams().get("page_size"));
    assertFalse(transport.getLastUrlInfo().getQueryParams().containsKey("start_cursor"));
  }

  @Test
  void listComments_withBothPaginationParams() {
    endpoint.listComments("block-id-1", "cursor-abc", 25);

    assertEquals("GET", transport.getLastMethod());
    assertEquals("/comments", transport.getLastUrlInfo().getUrl());
    assertTrue(transport.getLastUrlInfo().getQueryParams().containsKey("block_id"));
    assertEquals(
        List.of("cursor-abc"), transport.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertEquals(List.of("25"), transport.getLastUrlInfo().getQueryParams().get("page_size"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void listComments_rejectsBlankOrNullBlockId(String blockId) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.listComments(blockId));
  }
}
