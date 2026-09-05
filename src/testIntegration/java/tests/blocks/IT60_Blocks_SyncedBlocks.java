package tests.blocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.fluent.NotionBlocks;
import io.kristaxlab.notion.fluent.NotionBlocksViewer;
import io.kristaxlab.notion.http.error.ValidationException;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.BlockType;
import io.kristaxlab.notion.model.block.SyncedBlock;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT60_Blocks_SyncedBlocks extends WithEmptyTestPage {

  private static final List<String> ORIGINAL_CONTENT =
      List.of("This is the original synced block content.", "Original item 1", "Original item 2");

  @Test
  @DisplayName("IT-60: Blocks - Append synced blocks")
  public void testAppendSyncedBlocks() {
    // 1. Append the original synced block - the one that owns the content
    SyncedBlock original =
        NotionBlocks.synced(
            b ->
                b.paragraph(ORIGINAL_CONTENT.get(0))
                    .bullet(ORIGINAL_CONTENT.get(1))
                    .bullet(ORIGINAL_CONTENT.get(2)));

    BlockList originalRs = getNotionClient().blocks().appendChildren(getTestPageId(), original);

    assertEquals(1, originalRs.getResults().size());

    SyncedBlock savedOriginal = originalRs.getResults().get(0).asSynced();
    assertEquals(BlockType.SYNCED_BLOCK.getValue(), savedOriginal.getType());
    assertTrue(savedOriginal.getHasChildren());
    assertNull(
        savedOriginal.getSyncedBlock().getSyncedFrom(),
        "The original synced block is not synced from anywhere");

    String originalId = savedOriginal.getId();
    assertEquals(
        ORIGINAL_CONTENT,
        NotionBlocksViewer.of(getNotionClient().blocks().retrieveChildren(originalId))
            .plainTextList());

    // 2. Append a duplicate that mirrors the original
    BlockList duplicateRs =
        getNotionClient()
            .blocks()
            .appendChildren(getTestPageId(), NotionBlocks.syncedFrom(originalId));

    assertEquals(1, duplicateRs.getResults().size());

    SyncedBlock duplicate = duplicateRs.getResults().get(0).asSynced();
    assertEquals(originalId, duplicate.getSyncedBlock().getSyncedFrom().getBlockId());
    assertEquals(
        ORIGINAL_CONTENT,
        NotionBlocksViewer.of(getNotionClient().blocks().retrieveChildren(duplicate.getId()))
            .plainTextList(),
        "The duplicate exposes the content of the original block");

    // 3. Unsyncing a duplicate is not supported by the API
    duplicate.getSyncedBlock().setSyncedFrom(null);

    assertThrows(
        ValidationException.class,
        () -> getNotionClient().blocks().update(duplicate.getId(), duplicate));
  }
}
