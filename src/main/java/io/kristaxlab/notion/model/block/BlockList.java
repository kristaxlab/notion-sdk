package io.kristaxlab.notion.model.block;

import io.kristaxlab.notion.model.common.NotionList;
import lombok.Getter;
import lombok.Setter;

/**
 * Paginated list of {@link Block} objects returned by the Notion API.
 *
 * @see NotionList
 */
@Getter
@Setter
public class BlockList extends NotionList<Block> {

  private Object block;

  public BlockList() {
    setType(BlockType.BLOCK.getValue());
    block = new Object();
  }
}
