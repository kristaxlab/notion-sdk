package io.kristixlab.notion.api;

import io.kristixlab.notion.api.exchange.ApiTransport;
import io.kristixlab.notion.api.exchange.NotionApiTransport;
import io.kristixlab.notion.api.model.AppendBlockChildrenRequest;
import io.kristixlab.notion.api.model.blocks.Block;
import io.kristixlab.notion.api.model.blocks.Blocks;
import java.util.Map;

/**
 * API for interacting with Notion Blocks endpoints. Provides methods to retrieve, create, update,
 * and delete blocks.
 */
public class BlocksApi {

  private static final String BLOCK_ID = "block_id";

  private final ApiTransport transport;

  public BlocksApi(NotionApiTransport transport) {
    this.transport = transport;
  }

  /**
   * Retrieve a block by its ID.
   *
   * @param blockId The ID of the block to retrieve
   * @return The block object
   */
  public Block retrieve(String blockId) {
    validateBlockId(blockId);

    Map<String, String> pathParams = ApiRequestUtil.createPathParams(BLOCK_ID, blockId);

    return transport.call(
            "GET", "/blocks/{block_id}/children", null, pathParams, null, Block.class);
  }

  /**
   * Retrieve block children (children of a block).
   *
   * @param blockId The ID of the parent block
   * @return BlocksResponse containing the child blocks
   */
  public Blocks retrieveChildren(String blockId) {
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
  public Blocks retrieveChildren(String blockId, String startCursor, Integer pageSize) {
    validateBlockId(blockId);

    Map<String, String> pathParams = ApiRequestUtil.createPathParams(BLOCK_ID, blockId);
    Map<String, String[]> queryParams = ApiRequestUtil.createQueryParams(startCursor, pageSize);

    return transport.call(
            "GET", "/blocks/{block_id}/children", queryParams, pathParams, null, Blocks.class);
  }

  /**
   * Append block children to a parent block.
   *
   * @param blockId The ID of the parent block
   * @param request The request containing children blocks to append
   * @return BlocksResponse containing the appended blocks
   */
  public Blocks appendChildren(String blockId, AppendBlockChildrenRequest request) {
    validateBlockId(blockId);
    validateRequest(request);

    Map<String, String> pathParams = ApiRequestUtil.createPathParams(BLOCK_ID, blockId);

    return transport.call(
            "PATCH", "/blocks/{block_id}/children", null, pathParams, request, Blocks.class);
  }

  /**
   * Update a block.
   *
   * @param blockId The ID of the block to update
   * @param request The update request
   * @return The updated block
   */
  public Block updateBlock(String blockId, Block request) {
    validateBlockId(blockId);
    validateRequest(request);

    Map<String, String> pathParams = ApiRequestUtil.createPathParams(BLOCK_ID, blockId);

    return transport.call("PATCH", "/blocks/{block_id}", null, pathParams, request, Block.class);
  }

  /**
   * Delete a block.
   *
   * @param blockId The ID of the block to delete
   * @return The deleted block (marked as archived)
   */
  public Block delete(String blockId) {
    validateBlockId(blockId);

    Map<String, String> pathParams = ApiRequestUtil.createPathParams(BLOCK_ID, blockId);

    return transport.call("DELETE", "/blocks/{block_id}", null, pathParams, null, Block.class);
  }

  /**
   * Delete a block.
   *
   * @param blockId The ID of the block to delete
   * @return The deleted block (marked as archived)
   */
  public Block restore(String blockId) {
    validateBlockId(blockId);

    Map<String, String> pathParams = ApiRequestUtil.createPathParams(BLOCK_ID, blockId);
    Block body = new Block();
    body.setArchived(false);
    body.setInTrash(false);

    return transport.call("PATCH", "/blocks/{block_id}", null, pathParams, body, Block.class);
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
