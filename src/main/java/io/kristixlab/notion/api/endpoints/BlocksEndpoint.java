package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.block.AppendBlockChildrenParams;
import io.kristixlab.notion.api.model.block.Block;
import io.kristixlab.notion.api.model.block.BlockList;
import io.kristixlab.notion.api.model.common.Position;
import io.kristixlab.notion.api.model.helper.NotionBlocksBuilder;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Interface defining operations for Notion Blocks.
 *
 * @see <a href="https://developers.notion.com/reference/blocks">Notion Blocks API</a>
 */
public interface BlocksEndpoint {
  BlockList appendChildren(String parentBlockId, Block child);

  BlockList appendChildren(String parentBlockId, Block child, Position position);

  BlockList appendChildren(String parentBlockId, List<? extends Block> children);

  BlockList appendChildren(String parentBlockId, List<? extends Block> children, Position position);

  BlockList appendChildren(String parentBlockId, Consumer<NotionBlocksBuilder> consumer);

  BlockList appendChildren(
      String parentBlockId, Consumer<NotionBlocksBuilder> consumer, Position position);

  BlockList appendChildren(String parentBlockId, Supplier<List<? extends Block>> supplier);

  BlockList appendChildren(
      String parentBlockId, Supplier<List<? extends Block>> supplier, Position position);

  BlockList appendChildren(String parentBlockId, AppendBlockChildrenParams request);

  Block retrieve(String blockId);

  BlockList retrieveChildren(String blockId);

  BlockList retrieveChildren(String blockId, String startCursor, Integer pageSize);

  Block update(String blockId, Block request);

  Block delete(String blockId);

  Block restore(String blockId);
}
