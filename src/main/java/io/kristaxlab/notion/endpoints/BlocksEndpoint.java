package io.kristaxlab.notion.endpoints;

import io.kristaxlab.notion.fluent.NotionBlocksBuilder;
import io.kristaxlab.notion.model.block.AppendBlockChildrenParams;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.common.Position;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Block CRUD, child listing, and appending children (with optional {@link Position}).
 *
 * @see <a href="https://developers.notion.com/reference/blocks">Notion Blocks API</a>
 */
public interface BlocksEndpoint {
  /**
   * Appends a single child block under a parent block.
   *
   * @param parentBlockId parent block identifier
   * @param child block to append
   * @return API response containing appended blocks
   */
  BlockList appendChildren(String parentBlockId, Block child);

  /**
   * Appends multiple child blocks under a parent block.
   *
   * @param parentBlockId parent block identifier
   * @param children blocks to append
   * @return API response containing appended blocks
   */
  BlockList appendChildren(String parentBlockId, List<? extends Block> children);

  /**
   * Appends blocks built from the {@link NotionBlocksBuilder} DSL.
   *
   * @param parentBlockId parent block identifier
   * @param consumer builder consumer that produces child blocks
   * @return API response containing appended blocks
   */
  BlockList appendChildren(String parentBlockId, Consumer<NotionBlocksBuilder> consumer);

  /**
   * Appends lazily built blocks at the given position.
   *
   * @param parentBlockId parent block identifier
   * @param supplier supplier of child blocks
   * @param position optional insert position relative to existing children
   * @return API response containing appended blocks
   */
  BlockList appendChildren(
      String parentBlockId, Supplier<List<? extends Block>> supplier, Position position);

  /**
   * Appends children using a fully prepared request payload.
   *
   * @param parentBlockId parent block identifier
   * @param request append payload including children and optional position
   * @return API response containing appended blocks
   */
  BlockList appendChildren(String parentBlockId, AppendBlockChildrenParams request);

  /**
   * Retrieves a single block by id.
   *
   * @param blockId block identifier
   * @return retrieved block
   */
  Block retrieve(String blockId);

  /**
   * Retrieves child blocks for a parent block using default pagination.
   *
   * @param blockId parent block identifier
   * @return paginated child block response
   */
  BlockList retrieveChildren(String blockId);

  /**
   * Retrieves child blocks for a parent block with explicit pagination.
   *
   * @param blockId parent block identifier
   * @param startCursor pagination cursor
   * @param pageSize max page size
   * @return paginated child block response
   */
  BlockList retrieveChildren(String blockId, String startCursor, Integer pageSize);

  /**
   * Updates a block using the id embedded in the request payload.
   *
   * @param request partial block payload with update fields
   * @return updated block
   */
  Block update(Block request);

  /**
   * Updates an existing block.
   *
   * @param blockId block identifier
   * @param request partial block payload with update fields
   * @return updated block
   */
  Block update(String blockId, Block request);

  /**
   * Archives (deletes) a block.
   *
   * @param blockId block identifier
   * @return archived block
   */
  Block delete(String blockId);

  /**
   * Restores a block from trash by clearing the archived state.
   *
   * @param blockId block identifier
   * @return restored block
   */
  Block restore(String blockId);
}
