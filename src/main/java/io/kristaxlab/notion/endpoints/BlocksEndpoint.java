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
  BlockList appendChildren(String parentBlockId, Block child);

  BlockList appendChildren(String parentBlockId, List<? extends Block> children);

  /** Appends blocks built from the {@link NotionBlocksBuilder} DSL. */
  BlockList appendChildren(String parentBlockId, Consumer<NotionBlocksBuilder> consumer);

  /** Appends lazily built blocks at the given position. */
  BlockList appendChildren(
      String parentBlockId, Supplier<List<? extends Block>> supplier, Position position);

  /** Full control over children list and optional position. */
  BlockList appendChildren(String parentBlockId, AppendBlockChildrenParams request);

  Block retrieve(String blockId);

  BlockList retrieveChildren(String blockId);

  BlockList retrieveChildren(String blockId, String startCursor, Integer pageSize);

  /** Updates using the block’s embedded id. */
  Block update(Block request);

  Block update(String blockId, Block request);

  /** Archives the block. */
  Block delete(String blockId);

  /** Restores the block from trash. */
  Block restore(String blockId);
}
