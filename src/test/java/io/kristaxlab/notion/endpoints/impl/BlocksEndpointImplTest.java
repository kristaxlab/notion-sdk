package io.kristaxlab.notion.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.http.base.client.ApiClientStub;
import io.kristaxlab.notion.model.block.AppendBlockChildrenParams;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.common.Position;
import io.kristaxlab.notion.model.helper.NotionBlocksBuilder;
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

@DisplayName("Blocks endpoint behaviors")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BlocksEndpointImplTest {

  private ApiClientStub client;
  private BlocksEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    client = new ApiClientStub();
    endpoint = new BlocksEndpointImpl(client);
  }

  @Nested
  @DisplayName("Retrieve block")
  class Retrieve {

    @Test
    @DisplayName("works for valid block id")
    void retrieve_buildsGetRequest() {
      Block expected = new Block();
      client.setResponse(expected);

      Block result = endpoint.retrieve("block-id-1");

      assertEquals("GET", client.getLastMethod());
      assertEquals("/blocks/{block_id}", client.getLastUrlInfo().getUrl());
      assertEquals("block-id-1", client.getLastUrlInfo().getPathParams().get("block_id"));
      assertNull(client.getLastBody());
      assertSame(expected, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null block id")
    void retrieve_rejectsBlankOrNullBlockId(String blockId) {
      assertThrows(IllegalArgumentException.class, () -> endpoint.retrieve(blockId));
    }
  }

  @Nested
  @DisplayName("Retrieve block children")
  class RetrieveChildren {

    @Test
    @DisplayName("works for valid parent block id")
    void retrieveChildren_buildsGetRequest() {
      BlockList expected = new BlockList();
      client.setResponse(expected);

      BlockList result = endpoint.retrieveChildren("parent-1");

      assertEquals("GET", client.getLastMethod());
      assertEquals("/blocks/{block_id}/children", client.getLastUrlInfo().getUrl());
      assertEquals("parent-1", client.getLastUrlInfo().getPathParams().get("block_id"));
      assertTrue(client.getLastUrlInfo().getQueryParams().isEmpty());
      assertNull(client.getLastBody());
      assertSame(expected, result);
    }

    @Test
    @DisplayName("works with pagination params")
    void retrieveChildren_withPagination_addsQueryParams() {
      endpoint.retrieveChildren("parent-1", "cursor-abc", 25);

      assertEquals("GET", client.getLastMethod());
      assertEquals("/blocks/{block_id}/children", client.getLastUrlInfo().getUrl());
      assertEquals("parent-1", client.getLastUrlInfo().getPathParams().get("block_id"));
      assertEquals(
          List.of("cursor-abc"), client.getLastUrlInfo().getQueryParams().get("start_cursor"));
      assertEquals(List.of("25"), client.getLastUrlInfo().getQueryParams().get("page_size"));
    }

    @Test
    @DisplayName("works with null pagination and omits query params")
    void retrieveChildren_withNullPagination_omitsQueryParams() {
      endpoint.retrieveChildren("parent-1", null, null);

      assertEquals("GET", client.getLastMethod());
      assertTrue(client.getLastUrlInfo().getQueryParams().isEmpty());
    }

    @Test
    @DisplayName("works with only start cursor")
    void retrieveChildren_withOnlyStartCursor_addsOnlyCursorParam() {
      endpoint.retrieveChildren("parent-1", "cursor-xyz", null);

      assertEquals(
          List.of("cursor-xyz"), client.getLastUrlInfo().getQueryParams().get("start_cursor"));
      assertNull(client.getLastUrlInfo().getQueryParams().get("page_size"));
    }

    @Test
    @DisplayName("works with only page size")
    void retrieveChildren_withOnlyPageSize_addsOnlyPageSizeParam() {
      endpoint.retrieveChildren("parent-1", null, 50);

      assertNull(client.getLastUrlInfo().getQueryParams().get("start_cursor"));
      assertEquals(List.of("50"), client.getLastUrlInfo().getQueryParams().get("page_size"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null block id")
    void retrieveChildren_rejectsBlankOrNullBlockId(String blockId) {
      assertThrows(IllegalArgumentException.class, () -> endpoint.retrieveChildren(blockId));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null block id when pagination is provided")
    void retrieveChildren_withPagination_rejectsBlankOrNullBlockId(String blockId) {
      assertThrows(
          IllegalArgumentException.class, () -> endpoint.retrieveChildren(blockId, "cursor", 10));
    }
  }

  @Nested
  @DisplayName("Append children")
  class AppendChildren {

    @Test
    @DisplayName("works for valid parent block id and children list")
    void appendChildren_withList_buildsPatchRequest() {
      List<Block> children = List.of(new Block(), new Block());
      BlockList expected = new BlockList();
      client.setResponse(expected);

      BlockList result = endpoint.appendChildren("block-id-42", children);

      assertEquals("PATCH", client.getLastMethod());
      assertEquals("/blocks/{block_id}/children", client.getLastUrlInfo().getUrl());
      assertEquals("block-id-42", client.getLastUrlInfo().getPathParams().get("block_id"));

      AppendBlockChildrenParams body = (AppendBlockChildrenParams) client.getLastBody();
      assertEquals(children, body.getChildren());
      assertNull(body.getPosition());
      assertSame(expected, result);
    }

    @Test
    @DisplayName("works for valid params with children and position")
    void appendChildren_withListAndPosition_buildsPatchRequest() {
      List<Block> children = List.of(new Block(), new Block());
      Position position = Position.afterBlock("after-block-id");
      BlockList expected = new BlockList();
      client.setResponse(expected);

      BlockList result =
          endpoint.appendChildren(
              "block-id-42",
              AppendBlockChildrenParams.builder().children(children).position(position).build());

      assertEquals("PATCH", client.getLastMethod());
      assertEquals("/blocks/{block_id}/children", client.getLastUrlInfo().getUrl());
      assertEquals("block-id-42", client.getLastUrlInfo().getPathParams().get("block_id"));

      AppendBlockChildrenParams body = (AppendBlockChildrenParams) client.getLastBody();
      assertEquals(children, body.getChildren());
      assertEquals(position, body.getPosition());
      assertSame(expected, result);
    }

    @Test
    @DisplayName("works for list append and leaves position unset")
    void appendChildren_withListAndNullPosition_setsNullPosition() {
      List<Block> children = List.of(new Block());

      endpoint.appendChildren("block-id-42", children);

      AppendBlockChildrenParams body = (AppendBlockChildrenParams) client.getLastBody();
      assertNull(body.getPosition());
    }

    @Test
    @DisplayName("works for valid parent block id and single child")
    void appendChildren_withSingleChild_noPosition_buildsPatchRequest() {
      Block child = new Block();
      BlockList expected = new BlockList();
      client.setResponse(expected);

      BlockList result = endpoint.appendChildren("block-id-42", child);

      assertEquals("PATCH", client.getLastMethod());
      assertEquals("/blocks/{block_id}/children", client.getLastUrlInfo().getUrl());
      assertEquals("block-id-42", client.getLastUrlInfo().getPathParams().get("block_id"));

      AppendBlockChildrenParams body = (AppendBlockChildrenParams) client.getLastBody();
      assertEquals(List.of(child), body.getChildren());
      assertNull(body.getPosition());
      assertSame(expected, result);
    }

    @Test
    @DisplayName("works for valid append children params")
    void appendChildren_withParams_buildsPatchRequest() {
      Block child = new Block();
      Position position = Position.pageStart();
      AppendBlockChildrenParams params =
          AppendBlockChildrenParams.builder().children(child).position(position).build();
      BlockList expected = new BlockList();
      client.setResponse(expected);

      BlockList result = endpoint.appendChildren("block-id-42", params);

      assertEquals("PATCH", client.getLastMethod());
      assertEquals("/blocks/{block_id}/children", client.getLastUrlInfo().getUrl());
      assertEquals("block-id-42", client.getLastUrlInfo().getPathParams().get("block_id"));
      assertSame(params, client.getLastBody());
      assertSame(expected, result);
    }

    @Test
    @DisplayName("rejects null append children params")
    void appendChildren_withParams_rejectsNullParams() {
      assertThrows(
          IllegalArgumentException.class,
          () -> endpoint.appendChildren("block-id-42", (AppendBlockChildrenParams) null));
    }

    @Test
    @DisplayName("works for valid parent block id and blocks builder consumer")
    void appendChildren_withConsumer_buildsPatchRequest() {
      BlockList expected = new BlockList();
      client.setResponse(expected);

      BlockList result =
          endpoint.appendChildren("block-id-42", builder -> builder.paragraph("hello"));

      assertEquals("PATCH", client.getLastMethod());
      assertEquals("/blocks/{block_id}/children", client.getLastUrlInfo().getUrl());
      assertEquals("block-id-42", client.getLastUrlInfo().getPathParams().get("block_id"));

      AppendBlockChildrenParams body = (AppendBlockChildrenParams) client.getLastBody();
      assertEquals(1, body.getChildren().size());
      assertNull(body.getPosition());
      assertSame(expected, result);
    }

    @Test
    @DisplayName("rejects null blocks builder consumer")
    void appendChildren_withConsumer_rejectsNullConsumer() {
      assertThrows(
          IllegalArgumentException.class,
          () ->
              endpoint.appendChildren(
                  "block-id-42", (java.util.function.Consumer<NotionBlocksBuilder>) null));
    }

    @Test
    @DisplayName("works for valid supplier and insertion position")
    void appendChildren_withSupplierAndPosition_buildsPatchRequest() {
      Block child = new Block();
      Position position = Position.afterBlock("after-block-id");
      BlockList expected = new BlockList();
      client.setResponse(expected);

      BlockList result = endpoint.appendChildren("block-id-42", () -> List.of(child), position);

      assertEquals("PATCH", client.getLastMethod());
      assertEquals("/blocks/{block_id}/children", client.getLastUrlInfo().getUrl());
      assertEquals("block-id-42", client.getLastUrlInfo().getPathParams().get("block_id"));

      AppendBlockChildrenParams body = (AppendBlockChildrenParams) client.getLastBody();
      assertEquals(List.of(child), body.getChildren());
      assertEquals(position, body.getPosition());
      assertSame(expected, result);
    }

    @Test
    @DisplayName("rejects null supplier")
    void appendChildren_withSupplierAndPosition_rejectsNullSupplier() {
      assertThrows(
          IllegalArgumentException.class,
          () ->
              endpoint.appendChildren(
                  "block-id-42", (java.util.function.Supplier<List<? extends Block>>) null, null));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null parent block id for single child append")
    void appendChildren_withSingleChild_rejectsBlankOrNullParentBlockId(String parentBlockId) {
      assertThrows(
          IllegalArgumentException.class,
          () -> endpoint.appendChildren(parentBlockId, new Block()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null parent block id for list append")
    void appendChildren_rejectsBlankOrNullParentBlockId(String parentBlockId) {
      assertThrows(
          IllegalArgumentException.class,
          () -> endpoint.appendChildren(parentBlockId, List.of(new Block())));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null parent block id for params append")
    void appendChildren_withParams_rejectsBlankOrNullParentBlockId(String parentBlockId) {
      AppendBlockChildrenParams params =
          AppendBlockChildrenParams.builder().children(new Block()).build();
      assertThrows(
          IllegalArgumentException.class, () -> endpoint.appendChildren(parentBlockId, params));
    }
  }

  @Nested
  @DisplayName("Update block")
  class Update {

    @Test
    @DisplayName("works for valid block id and update request")
    void update_buildsPatchRequest() {
      Block request = new Block();
      Block expected = new Block();
      client.setResponse(expected);

      Block result = endpoint.update("block-id-7", request);

      assertEquals("PATCH", client.getLastMethod());
      assertEquals("/blocks/{block_id}", client.getLastUrlInfo().getUrl());
      assertEquals("block-id-7", client.getLastUrlInfo().getPathParams().get("block_id"));
      assertSame(request, client.getLastBody());
      assertSame(expected, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null block id")
    void update_rejectsBlankOrNullBlockId(String blockId) {
      assertThrows(IllegalArgumentException.class, () -> endpoint.update(blockId, new Block()));
    }

    @Test
    @DisplayName("rejects null update request")
    void update_rejectsNullRequest() {
      assertThrows(IllegalArgumentException.class, () -> endpoint.update("block-id-7", null));
    }

    @Test
    @DisplayName("works for valid update request containing block id")
    void update_withRequestOnly_usesRequestIdAsPathParam() {
      Block request = new Block();
      request.setId("block-id-from-request");
      Block expected = new Block();
      client.setResponse(expected);

      Block result = endpoint.update(request);

      assertEquals("PATCH", client.getLastMethod());
      assertEquals("/blocks/{block_id}", client.getLastUrlInfo().getUrl());
      assertEquals(
          "block-id-from-request", client.getLastUrlInfo().getPathParams().get("block_id"));
      assertSame(request, client.getLastBody());
      assertSame(expected, result);
    }

    @Test
    @DisplayName("rejects request without block id")
    void update_withRequestOnly_rejectsRequestWithoutId() {
      Block request = new Block();
      assertThrows(IllegalArgumentException.class, () -> endpoint.update(request));
    }
  }

  @Nested
  @DisplayName("Delete block")
  class Delete {

    @Test
    @DisplayName("works for valid block id")
    void delete_buildsDeleteRequest() {
      Block expected = new Block();
      client.setResponse(expected);

      Block result = endpoint.delete("block-id-9");

      assertEquals("DELETE", client.getLastMethod());
      assertEquals("/blocks/{block_id}", client.getLastUrlInfo().getUrl());
      assertEquals("block-id-9", client.getLastUrlInfo().getPathParams().get("block_id"));
      assertNull(client.getLastBody());
      assertSame(expected, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null block id")
    void delete_rejectsBlankOrNullBlockId(String blockId) {
      assertThrows(IllegalArgumentException.class, () -> endpoint.delete(blockId));
    }
  }

  @Nested
  @DisplayName("Restore block")
  class Restore {

    @Test
    @DisplayName("works for valid block id and sets in trash to false")
    void restore_buildsPatchRequestWithInTrashFalse() {
      Block expected = new Block();
      client.setResponse(expected);

      Block result = endpoint.restore("block-id-11");

      assertEquals("PATCH", client.getLastMethod());
      assertEquals("/blocks/{block_id}", client.getLastUrlInfo().getUrl());
      assertEquals("block-id-11", client.getLastUrlInfo().getPathParams().get("block_id"));

      Block body = (Block) client.getLastBody();
      assertNotNull(body);
      assertEquals(false, body.getInTrash());
      assertSame(expected, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("rejects blank or null block id")
    void restore_rejectsBlankOrNullBlockId(String blockId) {
      assertThrows(IllegalArgumentException.class, () -> endpoint.restore(blockId));
    }
  }
}
