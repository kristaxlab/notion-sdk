package io.kristixlab.notion.api.endpoints.impl;

import io.kristixlab.notion.api.http.NotionHttpTransport;
import io.kristixlab.notion.api.http.transport.HttpTransportImpl;
import io.kristixlab.notion.api.util.Pagination;
import io.kristixlab.notion.api.endpoints.BlocksEndpoint;
import io.kristixlab.notion.api.http.transport.rq.URLInfo;
import io.kristixlab.notion.api.http.transport.util.URLInfoBuilder;
import io.kristixlab.notion.api.model.blocks.AppendBlockChildrenRequest;
import io.kristixlab.notion.api.model.blocks.Block;
import io.kristixlab.notion.api.model.blocks.Blocks;

/**
 * API for interacting with Notion Blocks endpoints. Provides methods to retrieve, create, update,
 * and delete blocks.
 */
public class BlocksEndpointImpl implements BlocksEndpoint {

  private static final String BLOCK_ID = "block_id";

  private final HttpTransportImpl transport;

  public BlocksEndpointImpl(NotionHttpTransport transport) {
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

    URLInfo urlInfo = URLInfo.builder("/blocks/{block_id}/children").pathParam(BLOCK_ID, blockId).build();

    return transport.call("GET", urlInfo, Block.class);
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
   * @param blockId     The ID of the parent block
   * @param startCursor Cursor for pagination (optional)
   * @param pageSize    Number of items to return (optional, max 100)
   * @return BlocksResponse containing the child blocks
   */
  public Blocks retrieveChildren(String blockId, String startCursor, Integer pageSize) {
    validateBlockId(blockId);

    URLInfoBuilder urlInfo = URLInfo.builder("/blocks/{block_id}/children").pathParam(BLOCK_ID, blockId);

    if (startCursor != null) {
      urlInfo.queryParam(Pagination.START_CURSOR, startCursor);
    }

    if (pageSize != null) {
      urlInfo.queryParam(Pagination.PAGE_SIZE, pageSize);
    }

    return transport.call("GET", urlInfo.build(), Blocks.class);
  }


  /**
   * Append block children to a parent block.
   *
   * @param parentBlockId The ID of the parent block
   * @param child         child block to append
   * @return BlocksResponse containing the appended blocks
   */
  public Blocks appendChildren(String parentBlockId, Block child) {
    AppendBlockChildrenRequest request = new AppendBlockChildrenRequest();
    request.getChildren().add(child);
    return appendChildren(parentBlockId, request);
  }

  /**
   * Append block children to a parent block.
   *
   * @param parentBlockId The ID of the parent block
   * @param request       The request containing children blocks to append
   * @return BlocksResponse containing the appended blocks
   */
  public Blocks appendChildren(String parentBlockId, AppendBlockChildrenRequest request) {
    validateBlockId(parentBlockId);
    validateRequest(request);

    URLInfo urlInfo = URLInfo.builder("/blocks/{block_id}/children").pathParam(BLOCK_ID, parentBlockId).build();

    return transport.call("PATCH", urlInfo, request, Blocks.class);
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

    URLInfo urlInfo = URLInfo.builder("/blocks/{block_id}").pathParam(BLOCK_ID, blockId).build();

    return transport.call("PATCH", urlInfo, request, Block.class);
  }

  /**
   * Delete a block.
   *
   * @param blockId The ID of the block to delete
   * @return The deleted block (marked as archived)
   */
  public Block delete(String blockId) {
    validateBlockId(blockId);
    URLInfo urlInfo = URLInfo.builder("/blocks/{block_id}").pathParam(BLOCK_ID, blockId).build();
    return transport.call("DELETE", urlInfo, Block.class);
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
