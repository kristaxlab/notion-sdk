package io.kristixlab.notion;

import io.kristixlab.notion.api.model.blocks.Blocks;
import io.kristixlab.notion.sdk.model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotionApiRetriveBlocksCall {
  private static Logger LOGGER = LoggerFactory.getLogger(NotionApiRetriveBlocksCall.class);

  private final NotionClient notionClient;
  private final String blockId;

  public NotionApiRetriveBlocksCall(NotionClient notionClient, String blockId) {
    this.notionClient = notionClient;
    this.blockId = blockId;
  }

  public Page execute() {
    Page page = new Page();

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Retrieving children for blockId: {}", blockId);
    }

    Blocks rs = notionClient.blocks().retrieveChildren(blockId);
    page.getContent().addAll(rs.getResults());

    while (rs.hasMore() != null && rs.hasMore()) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(
                "Calling next page with blocks, blockId: {}, requestId: {}, nextCursor: {}",
                blockId,
                rs.getRequestId(),
                rs.getNextCursor());
      }
      rs = notionClient.blocks().retrieveChildren(blockId, rs.getNextCursor(), 100);
      page.getContent().addAll(rs.getResults());
    }

    return page;
  }
}
