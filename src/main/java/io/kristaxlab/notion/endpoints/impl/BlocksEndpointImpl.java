package io.kristaxlab.notion.endpoints.impl;

import static io.kristaxlab.notion.endpoints.util.Validator.checkNotNull;
import static io.kristaxlab.notion.endpoints.util.Validator.checkNotNullOrEmpty;

import io.kristaxlab.notion.endpoints.BlocksEndpoint;
import io.kristaxlab.notion.endpoints.util.Validator;
import io.kristaxlab.notion.fluent.NotionBlocks;
import io.kristaxlab.notion.fluent.NotionBlocksBuilder;
import io.kristaxlab.notion.http.base.client.ApiClient;
import io.kristaxlab.notion.http.base.request.ApiPath;
import io.kristaxlab.notion.model.block.AppendBlockChildrenParams;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.common.Position;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * API for interacting with Notion Blocks endpoints. Provides methods to retrieve, create, update,
 * and delete blocks.
 */
public class BlocksEndpointImpl extends BaseEndpointImpl implements BlocksEndpoint {

  public BlocksEndpointImpl(ApiClient client) {
    super(client);
  }

  /**
   * Retrieve a block by its ID.
   *
   * @param blockId The ID of the block to retrieve
   * @return The block object
   */
  public Block retrieve(String blockId) {
    checkNotNullOrEmpty(blockId, "blockId");

    ApiPath urlInfo = ApiPath.builder("/blocks/{block_id}").pathParam("block_id", blockId).build();
    return getClient().call("GET", urlInfo, Block.class);
  }

  /**
   * Retrieve block children (children of a block).
   *
   * @param blockId The ID of the parent block
   * @return BlocksResponse containing the child blocks
   */
  public BlockList retrieveChildren(String blockId) {
    return retrieveChildren(blockId, null, null);
  }

  /**
   * Retrieve block children with pagination.
   *
   * @param blockId The ID of the parent block
   * @param startCursor Cursor for pagination (optional)
   * @param pageSize Number of items to return (optional, max 100)
   * @return BlocksResponse containing the child blocks
   */
  public BlockList retrieveChildren(String blockId, String startCursor, Integer pageSize) {
    checkNotNullOrEmpty(blockId, "blockId");

    ApiPath.Builder urlInfo =
        paginatedPath("/blocks/{block_id}/children", startCursor, pageSize)
            .pathParam("block_id", blockId);

    return getClient().call("GET", urlInfo.build(), BlockList.class);
  }

  /**
   * Append block children to a parent block.
   *
   * @param parentBlockId The ID of the parent block
   * @param child child block to append
   * @return BlocksResponse containing the appended blocks
   */
  public BlockList appendChildren(String parentBlockId, Block child) {
    return appendChildren(parentBlockId, child, null);
  }

  /**
   * Append a single block child to a parent block at a specific position.
   *
   * @param parentBlockId The ID of the parent block
   * @param child child block to append
   * @param position insertion position relative to the existing children
   * @return BlocksResponse containing the appended blocks
   */
  private BlockList appendChildren(String parentBlockId, Block child, Position position) {
    AppendBlockChildrenParams request =
        AppendBlockChildrenParams.builder().children(child).position(position).build();
    return appendChildren(parentBlockId, request);
  }

  /**
   * Append block children to a parent block.
   *
   * @param parentBlockId The ID of the parent block
   * @param children child blocks to append
   * @return BlocksResponse containing the appended blocks
   */
  @Override
  public BlockList appendChildren(String parentBlockId, List<? extends Block> children) {
    return appendChildren(parentBlockId, children, null);
  }

  @Override
  public BlockList appendChildren(String parentBlockId, Consumer<NotionBlocksBuilder> consumer) {

    return appendChildren(parentBlockId, consumer, null);
  }

  private BlockList appendChildren(
      String parentBlockId, Consumer<NotionBlocksBuilder> consumer, Position position) {
    checkNotNull(consumer, "consumer");
    NotionBlocksBuilder builder = NotionBlocks.blocksBuilder();
    consumer.accept(builder);
    return appendChildren(parentBlockId, builder.build(), position);
  }

  private BlockList appendChildren(String parentBlockId, Supplier<List<? extends Block>> supplier) {
    return appendChildren(parentBlockId, supplier, null);
  }

  @Override
  public BlockList appendChildren(
      String parentBlockId, Supplier<List<? extends Block>> supplier, Position position) {
    checkNotNull(supplier, "supplier");
    return appendChildren(parentBlockId, supplier.get(), position);
  }

  /**
   * Append block children to a parent block at a specific position.
   *
   * @param parentBlockId The ID of the parent block
   * @param block child blocks to append
   * @param position insertion position relative to the existing children
   * @return BlocksResponse containing the appended blocks
   */
  private BlockList appendChildren(
      String parentBlockId, List<? extends Block> block, Position position) {
    Validator.checkNotNullOrEmpty(parentBlockId, "parentBlockId");

    AppendBlockChildrenParams request =
        AppendBlockChildrenParams.builder().children(block).position(position).build();

    return appendChildren(parentBlockId, request);
  }

  /**
   * Append block children to a parent block.
   *
   * @param parentBlockId The ID of the parent block
   * @param request The request containing children blocks to append
   * @return BlocksResponse containing the appended blocks
   */
  public BlockList appendChildren(String parentBlockId, AppendBlockChildrenParams request) {
    checkNotNullOrEmpty(parentBlockId, "parentBlockId");
    checkNotNull(request, "request");

    ApiPath urlInfo =
        ApiPath.builder("/blocks/{block_id}/children").pathParam("block_id", parentBlockId).build();

    return getClient().call("PATCH", urlInfo, request, BlockList.class);
  }

  /**
   * Update a block.
   *
   * @param request The update request
   * @return The updated block
   */
  public Block update(Block request) {
    checkNotNull(request, "request");
    return update(request.getId(), request);
  }

  /**
   * Update a block.
   *
   * @param request The update request
   * @return The updated block
   */
  public Block update(String blockId, Block request) {
    checkNotNullOrEmpty(blockId, "blockId");
    checkNotNull(request, "request");

    ApiPath urlInfo = ApiPath.builder("/blocks/{block_id}").pathParam("block_id", blockId).build();

    return getClient().call("PATCH", urlInfo, request, Block.class);
  }

  /**
   * Delete a block.
   *
   * @param blockId The ID of the block to delete
   * @return The deleted block (marked as archived)
   */
  public Block delete(String blockId) {
    checkNotNullOrEmpty(blockId, "blockId");
    ApiPath urlInfo = ApiPath.builder("/blocks/{block_id}").pathParam("block_id", blockId).build();
    return getClient().call("DELETE", urlInfo, Block.class);
  }

  /**
   * Delete a block.
   *
   * @param blockId The ID of the block to delete
   * @return The deleted block (marked as archived)
   */
  public Block restore(String blockId) {
    checkNotNullOrEmpty(blockId, "blockId");

    Block body = new Block();
    body.setInTrash(false);
    return update(blockId, body);
  }
}
