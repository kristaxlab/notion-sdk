package io.kristaxlab.notion.endpoints.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.http.base.client.ApiClientStub;
import io.kristaxlab.notion.model.comment.CommentCreateParams;
import io.kristaxlab.notion.model.comment.CommentUpdateParams;
import io.kristaxlab.notion.model.common.Parent;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Comments endpoint behaviors")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CommentsEndpointImplTest {

  private ApiClientStub client;
  private CommentsEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    client = new ApiClientStub();
    endpoint = new CommentsEndpointImpl(client);
  }

  @Test
  @DisplayName("works for valid create comment on a page")
  void create_onPage() {
    CommentCreateParams request =
        CommentCreateParams.builder().inPage("page-id-1").richText("Hello").build();

    endpoint.create(request);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/comments", client.getLastUrlInfo().getUrl());
    assertSame(request, client.getLastBody());
  }

  @Test
  @DisplayName("works for valid create comment on a block")
  void create_onBlock() {
    CommentCreateParams request =
        CommentCreateParams.builder().inBlock("block-id-1").markdown("Hello").build();

    endpoint.create(request);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/comments", client.getLastUrlInfo().getUrl());
    assertSame(request, client.getLastBody());
  }

  @Test
  @DisplayName("works for valid create comment with discussion id")
  void create_withDiscussionId() {
    CommentCreateParams request =
        CommentCreateParams.builder().discussionId("discussion-id-1").richText("Reply").build();

    endpoint.create(request);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/comments", client.getLastUrlInfo().getUrl());
    assertSame(request, client.getLastBody());
  }

  @Test
  @DisplayName("rejects null create comment request")
  void create_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.create(null));
  }

  @Test
  @DisplayName("rejects create comment request without a target")
  void create_rejectsMissingTarget() {
    CommentCreateParams request = CommentCreateParams.builder().richText("Hello").build();

    assertThrows(IllegalArgumentException.class, () -> endpoint.create(request));
  }

  @Test
  @DisplayName("rejects create comment request with both parent and discussion id")
  void create_rejectsParentAndDiscussionId() {
    CommentCreateParams request =
        CommentCreateParams.builder()
            .inPage("page-id-1")
            .discussionId("discussion-id-1")
            .richText("Hello")
            .build();

    assertThrows(IllegalArgumentException.class, () -> endpoint.create(request));
  }

  @Test
  @DisplayName("rejects create comment request with both page and block parent")
  void create_rejectsPageAndBlockParent() {
    Parent parent = new Parent();
    parent.setPageId("page-id-1");
    parent.setBlockId("block-id-1");
    CommentCreateParams request =
        CommentCreateParams.builder().parent(parent).richText("Hello").build();

    assertThrows(IllegalArgumentException.class, () -> endpoint.create(request));
  }

  @Test
  @DisplayName("rejects create comment request without a body")
  void create_rejectsMissingBody() {
    CommentCreateParams request = CommentCreateParams.builder().inPage("page-id-1").build();

    assertThrows(IllegalArgumentException.class, () -> endpoint.create(request));
  }

  @Test
  @DisplayName("rejects create comment request with both rich text and markdown")
  void create_rejectsBothBodies() {
    CommentCreateParams request =
        CommentCreateParams.builder()
            .inPage("page-id-1")
            .richText("Hello")
            .markdown("Hello")
            .build();

    assertThrows(IllegalArgumentException.class, () -> endpoint.create(request));
  }

  @Test
  @DisplayName("works for valid retrieve comment id")
  void retrieve() {
    endpoint.retrieve("comment-id-1");

    assertEquals("GET", client.getLastMethod());
    assertEquals("/comments/{comment_id}", client.getLastUrlInfo().getUrl());
    assertEquals("comment-id-1", client.getLastUrlInfo().getPathParams().get("comment_id"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  @DisplayName("rejects blank or null retrieve comment id")
  void retrieve_rejectsBlankOrNullId(String commentId) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.retrieve(commentId));
  }

  @Test
  @DisplayName("works for valid update comment request")
  void update() {
    CommentUpdateParams request = CommentUpdateParams.builder().richText("Updated").build();

    endpoint.update("comment-id-1", request);

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/comments/{comment_id}", client.getLastUrlInfo().getUrl());
    assertEquals("comment-id-1", client.getLastUrlInfo().getPathParams().get("comment_id"));
    assertSame(request, client.getLastBody());
  }

  @Test
  @DisplayName("works for valid update comment request with markdown")
  void update_withMarkdown() {
    CommentUpdateParams request = CommentUpdateParams.builder().markdown("Updated").build();

    endpoint.update("comment-id-1", request);

    assertEquals("PATCH", client.getLastMethod());
    assertSame(request, client.getLastBody());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  @DisplayName("rejects blank or null update comment id")
  void update_rejectsBlankOrNullId(String commentId) {
    CommentUpdateParams request = CommentUpdateParams.builder().richText("Updated").build();

    assertThrows(IllegalArgumentException.class, () -> endpoint.update(commentId, request));
  }

  @Test
  @DisplayName("rejects null update comment request")
  void update_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.update("comment-id-1", null));
  }

  @Test
  @DisplayName("rejects update comment request without a body")
  void update_rejectsMissingBody() {
    CommentUpdateParams request = CommentUpdateParams.builder().build();

    assertThrows(IllegalArgumentException.class, () -> endpoint.update("comment-id-1", request));
  }

  @Test
  @DisplayName("rejects update comment request with both rich text and markdown")
  void update_rejectsBothBodies() {
    CommentUpdateParams request =
        CommentUpdateParams.builder().richText("Updated").markdown("Updated").build();

    assertThrows(IllegalArgumentException.class, () -> endpoint.update("comment-id-1", request));
  }

  @Test
  @DisplayName("works for valid delete comment id")
  void delete() {
    endpoint.delete("comment-id-1");

    assertEquals("DELETE", client.getLastMethod());
    assertEquals("/comments/{comment_id}", client.getLastUrlInfo().getUrl());
    assertEquals("comment-id-1", client.getLastUrlInfo().getPathParams().get("comment_id"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  @DisplayName("rejects blank or null delete comment id")
  void delete_rejectsBlankOrNullId(String commentId) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.delete(commentId));
  }

  @Test
  @DisplayName("works for default list comments request")
  void listComments() {
    endpoint.listComments("block-id-1");

    assertEquals("GET", client.getLastMethod());
    assertEquals("/comments", client.getLastUrlInfo().getUrl());
    assertEquals(List.of("block-id-1"), client.getLastUrlInfo().getQueryParams().get("block_id"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("start_cursor"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("page_size"));
  }

  @Test
  @DisplayName("works with start cursor and no page size")
  void listComments_withStartCursor() {
    endpoint.listComments("block-id-1", "cursor-xyz", null);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/comments", client.getLastUrlInfo().getUrl());
    assertEquals(List.of("block-id-1"), client.getLastUrlInfo().getQueryParams().get("block_id"));
    assertEquals(
        List.of("cursor-xyz"), client.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("page_size"));
  }

  @Test
  @DisplayName("works with page size and no start cursor")
  void listComments_withPageSize() {
    endpoint.listComments("block-id-1", null, 50);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/comments", client.getLastUrlInfo().getUrl());
    assertEquals(List.of("block-id-1"), client.getLastUrlInfo().getQueryParams().get("block_id"));
    assertEquals(List.of("50"), client.getLastUrlInfo().getQueryParams().get("page_size"));
    assertFalse(client.getLastUrlInfo().getQueryParams().containsKey("start_cursor"));
  }

  @Test
  @DisplayName("works with both pagination params")
  void listComments_withBothPaginationParams() {
    endpoint.listComments("block-id-1", "cursor-abc", 25);

    assertEquals("GET", client.getLastMethod());
    assertEquals("/comments", client.getLastUrlInfo().getUrl());
    assertEquals(List.of("block-id-1"), client.getLastUrlInfo().getQueryParams().get("block_id"));
    assertEquals(
        List.of("cursor-abc"), client.getLastUrlInfo().getQueryParams().get("start_cursor"));
    assertEquals(List.of("25"), client.getLastUrlInfo().getQueryParams().get("page_size"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  @DisplayName("rejects blank or null list comments block id")
  void listComments_rejectsBlankOrNullBlockId(String blockId) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.listComments(blockId));
  }

  @Test
  @DisplayName("create with blank discussion id and no parent is rejected")
  void create_rejectsBlankDiscussionId() {
    CommentCreateParams request =
        CommentCreateParams.builder().discussionId("   ").richText("Hello").build();

    assertThrows(IllegalArgumentException.class, () -> endpoint.create(request));
  }

  @Test
  @DisplayName("create with blank markdown is rejected")
  void create_rejectsBlankMarkdown() {
    CommentCreateParams request =
        CommentCreateParams.builder().inPage("page-id-1").markdown("   ").build();

    assertThrows(IllegalArgumentException.class, () -> endpoint.create(request));
  }

  @Test
  @DisplayName("list comments always includes block id among query params")
  void listComments_includesBlockIdWithPagination() {
    endpoint.listComments("page-id-9", "cursor-1", 10);

    assertTrue(client.getLastUrlInfo().getQueryParams().containsKey("block_id"));
    assertTrue(client.getLastUrlInfo().getQueryParams().containsKey("start_cursor"));
    assertTrue(client.getLastUrlInfo().getQueryParams().containsKey("page_size"));
  }
}
