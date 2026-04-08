package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.NotionList;
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
    setType("block");
    block = new Object();
  }
}
