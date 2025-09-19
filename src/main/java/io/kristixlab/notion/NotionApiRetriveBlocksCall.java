package io.kristixlab.notion;

import io.kristixlab.notion.api.model.blocks.Blocks;
import io.kristixlab.notion.api.NotionApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotionApiRetriveBlocksCall {
  private static Logger LOGGER = LoggerFactory.getLogger(NotionApiRetriveBlocksCall.class);

  private final NotionApiClient notionApiClient;
  private final String blockId;

  public NotionApiRetriveBlocksCall(NotionApiClient notionApiClient, String blockId) {
    this.notionApiClient = notionApiClient;
    this.blockId = blockId;
  }

  public Blocks execute() {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Retrieving children for blockId: {}", blockId);
    }

    Blocks blocks = notionApiClient.blocks().retrieveChildren(blockId);

    while (blocks.hasMore() != null && blocks.hasMore()) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(
            "Calling next page with blocks, blockId: {}, requestId: {}, nextCursor: {}",
            blockId,
            blocks.getRequestId(),
            blocks.getNextCursor());
      }
      Blocks nextBlocks =
          notionApiClient.blocks().retrieveChildren(blockId, blocks.getNextCursor(), 100);
      blocks.getResults().addAll(nextBlocks.getResults());
      blocks.setNextCursor(nextBlocks.getNextCursor());
      blocks.hasMore(nextBlocks.hasMore());
    }

    return blocks;
  }
}
