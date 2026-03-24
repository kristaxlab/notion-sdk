package io.kristixlab.notion.api.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.ApiClientStub;
import io.kristixlab.notion.api.model.comments.CreateCommentParams;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CommentsEndpointImplTest {

  private ApiClientStub client;
  private CommentsEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    client = new ApiClientStub();
    endpoint = new CommentsEndpointImpl(client);
  }

  @Test
  void create() {
    CreateCommentParams request = new CreateCommentParams();

    endpoint.create(request);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/comments", client.getLastUrlInfo().getUrl());
    assertSame(request, client.getLastBody());
  }

  @Test
  void create_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.create(null));
  }

  @Test
  void retrieveById() {
    endpoint.retrieve("comment-id-1");

    assertEquals("GET", client.getLastMethod());
    assertEquals("/comments/{comment_id}", client.getLastUrlInfo().getUrl());
    assertEquals("comment-id-1", client.getLastUrlInfo().getPathParams().get("comment_id"));
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

    assertEquals("GET", client.getLastMethod());
    assertEquals("/comments", client.getLastUrlInfo().getUrl());
    assertTrue(client.getLastUrlInfo().getQueryParams().containsKey("block_id"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("start_cursor"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("page_size"));
  }

  @Test
  void listComments_withStartCursor() {
    endpoint.listComments("block-id-1", "cursor-abc", null);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/comments", client.getLastUrlInfo().getUrl());
    assertTrue(client.getLastUrlInfo().getQueryParams().containsKey("block_id"));
    assertEquals(
        List.of("cursor-abc"), client.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("page_size"));
  }

  @Test
  void listComments_withPageSize() {
    endpoint.listComments("block-id-1", null, 25);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/comments", client.getLastUrlInfo().getUrl());
    assertTrue(client.getLastUrlInfo().getQueryParams().containsKey("block_id"));
    assertEquals(List.of("25"), client.getLastUrlInfo().getQueryParams().get("page_size"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("start_cursor"));
  }

  @Test
  void listComments_withBothPaginationParams() {
    endpoint.listComments("block-id-1", "cursor-abc", 25);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/comments", client.getLastUrlInfo().getUrl());
    assertTrue(client.getLastUrlInfo().getQueryParams().containsKey("block_id"));
    assertEquals(
        List.of("cursor-abc"), client.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertEquals(List.of("25"), client.getLastUrlInfo().getQueryParams().get("page_size"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void listComments_rejectsBlankOrNullBlockId(String blockId) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.listComments(blockId));
  }
}
