package io.kristixlab.notion.api.endpoints.impl;

import static io.kristixlab.notion.api.endpoints.util.Validator.checkNotNull;
import static io.kristixlab.notion.api.endpoints.util.Validator.checkNotNullOrEmpty;

import io.kristixlab.notion.api.endpoints.BlocksEndpoint;
import io.kristixlab.notion.api.http.base.client.ApiClient;
import io.kristixlab.notion.api.http.base.request.ApiPath;
import io.kristixlab.notion.api.model.block.AppendBlockChildrenParams;
import io.kristixlab.notion.api.model.block.Block;
import io.kristixlab.notion.api.model.block.BlockList;
import io.kristixlab.notion.api.model.common.Position;
import java.util.List;

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
  @Override
  public BlockList appendChildren(String parentBlockId, Block child, Position position) {
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
  public BlockList appendChildren(String parentBlockId, List<Block> children) {
    return appendChildren(parentBlockId, children, null);
  }

  /**
   * Append block children to a parent block at a specific position.
   *
   * @param parentBlockId The ID of the parent block
   * @param block child blocks to append
   * @param position insertion position relative to the existing children
   * @return BlocksResponse containing the appended blocks
   */
  @Override
  public BlockList appendChildren(
      String parentBlockId, java.util.List<Block> block, Position position) {
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
    checkNotNullOrEmpty(parentBlockId, "blockId");
    checkNotNull(request, "request");

    ApiPath urlInfo =
        ApiPath.builder("/blocks/{block_id}/children").pathParam("block_id", parentBlockId).build();

    return getClient().call("PATCH", urlInfo, request, BlockList.class);
  }

  /**
   * Update a block.
   *
   * @param blockId The ID of the block to update
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
