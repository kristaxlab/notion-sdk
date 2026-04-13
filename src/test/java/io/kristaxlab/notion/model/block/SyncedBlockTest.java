package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.helper.NotionBlocks;
import java.util.List;
import org.junit.jupiter.api.Test;

class SyncedBlockTest {

  // Constructor

  @Test
  void constructor_setsTypeAndInitializesSynced() {
    SyncedBlock block = new SyncedBlock();

    assertEquals("synced_block", block.getType());
    assertNotNull(block.getSyncedBlock());
    assertNull(block.getSyncedBlock().getSyncedFrom());
    assertNull(block.getSyncedBlock().getChildren());
  }

  // Synced inner class

  @Test
  void synced_getterSetter() {
    SyncedBlock.Synced synced = new SyncedBlock.Synced();

    assertNull(synced.getSyncedFrom());
    assertNull(synced.getChildren());

    SyncedBlock.SyncedFrom sf = new SyncedBlock.SyncedFrom();
    synced.setSyncedFrom(sf);
    assertSame(sf, synced.getSyncedFrom());

    List<Block> children = NotionBlocks.paragraphList("Child");
    synced.setChildren(children);
    assertSame(children, synced.getChildren());
  }

  // SyncedFrom inner class

  @Test
  void syncedFrom_getterSetter() {
    SyncedBlock.SyncedFrom sf = new SyncedBlock.SyncedFrom();

    assertNull(sf.getType());
    assertNull(sf.getBlockId());

    sf.setType("block_id");
    sf.setBlockId("test-id");

    assertEquals("block_id", sf.getType());
    assertEquals("test-id", sf.getBlockId());
  }
}
