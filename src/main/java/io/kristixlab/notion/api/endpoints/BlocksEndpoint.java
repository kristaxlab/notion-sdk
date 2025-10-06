package io.kristixlab.notion.api.endpoints;

import io.kristixlab.notion.api.model.blocks.AppendBlockChildrenRequest;
import io.kristixlab.notion.api.model.blocks.Block;
import io.kristixlab.notion.api.model.blocks.BlockList;

/**
 * Interface defining operations for Notion Blocks.
 *
 * @see <a href="https://developers.notion.com/reference/blocks">Notion Blocks API</a>
 */
public interface BlocksEndpoint {
  BlockList appendChildren(String parentBlockId, Block child);

  BlockList appendChildren(String parentBlockId, AppendBlockChildrenRequest request);

  Block retrieve(String blockId);

  BlockList retrieveChildren(String blockId);

  BlockList retrieveChildren(String blockId, String startCursor, Integer pageSize);

  Block update(String blockId, Block request);

  Block delete(String blockId);

  Block restore(String blockId);
}
