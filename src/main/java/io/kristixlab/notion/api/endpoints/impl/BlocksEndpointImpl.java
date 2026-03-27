package io.kristixlab.notion.api.endpoints.impl;

import io.kristixlab.notion.api.endpoints.BlocksEndpoint;
import io.kristixlab.notion.api.http.client.ApiClient;
import static io.kristixlab.notion.api.endpoints.util.PaginationHelper.paginatedPath;

import io.kristixlab.notion.api.http.request.ApiPath;
import io.kristixlab.notion.api.model.blocks.AppendBlockChildrenParams;
import io.kristixlab.notion.api.model.blocks.Block;
import io.kristixlab.notion.api.model.blocks.BlockList;

/**
 * API for interacting with Notion Blocks endpoints. Provides methods to retrieve, create, update,
 * and delete blocks.
 */
public class BlocksEndpointImpl implements BlocksEndpoint {

  private static final String BLOCK_ID = "block_id";

  private final ApiClient client;

  public BlocksEndpointImpl(ApiClient client) {
    this.client = client;
  }

  /**
   * Retrieve a block by its ID.
   *
   * @param blockId The ID of the block to retrieve
   * @return The block object
   */
  public Block retrieve(String blockId) {
    validateBlockId(blockId);

    ApiPath urlInfo = ApiPath.builder("/blocks/{block_id}").pathParam(BLOCK_ID, blockId).build();

    return client.call("GET", urlInfo, Block.class);
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
    validateBlockId(blockId);

    ApiPath.Builder urlInfo =
        paginatedPath("/blocks/{block_id}/children", startCursor, pageSize)
            .pathParam(BLOCK_ID, blockId);

    return client.call("GET", urlInfo.build(), BlockList.class);
  }

  /**
   * Append block children to a parent block.
   *
   * @param parentBlockId The ID of the parent block
   * @param child child block to append
   * @return BlocksResponse containing the appended blocks
   */
  public BlockList appendChildren(String parentBlockId, Block child) {
    AppendBlockChildrenParams request = new AppendBlockChildrenParams();
    request.getChildren().add(child);
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
    validateBlockId(parentBlockId);
    validateRequest(request);

    ApiPath urlInfo =
        ApiPath.builder("/blocks/{block_id}/children").pathParam(BLOCK_ID, parentBlockId).build();

    return client.call("PATCH", urlInfo, request, BlockList.class);
  }

  /**
   * Update a block.
   *
   * @param blockId The ID of the block to update
   * @param request The update request
   * @return The updated block
   */
  public Block update(String blockId, Block request) {
    validateBlockId(blockId);
    validateRequest(request);

    ApiPath urlInfo = ApiPath.builder("/blocks/{block_id}").pathParam(BLOCK_ID, blockId).build();

    return client.call("PATCH", urlInfo, request, Block.class);
  }

  /**
   * Delete a block.
   *
   * @param blockId The ID of the block to delete
   * @return The deleted block (marked as archived)
   */
  public Block delete(String blockId) {
    validateBlockId(blockId);
    ApiPath urlInfo = ApiPath.builder("/blocks/{block_id}").pathParam(BLOCK_ID, blockId).build();
    return client.call("DELETE", urlInfo, Block.class);
  }

  /**
   * Delete a block.
   *
   * @param blockId The ID of the block to delete
   * @return The deleted block (marked as archived)
   */
  public Block restore(String blockId) {
    Block body = new Block();
    body.setInTrash(false);
    return update(blockId, body);
  }

  /**
   * Validates the block ID.
   *
   * @param blockId The block ID to validate
   * @throws IllegalArgumentException if the block ID is null or empty
   */
  private void validateBlockId(String blockId) {
    if (blockId == null || blockId.trim().isEmpty()) {
      throw new IllegalArgumentException("Block ID cannot be null or empty");
    }
  }

  /**
   * Validates the request object.
   *
   * @param request The request object to validate
   * @throws IllegalArgumentException if the request is null
   */
  private void validateRequest(Object request) {
    if (request == null) {
      throw new IllegalArgumentException("Request cannot be null");
    }
  }
}
