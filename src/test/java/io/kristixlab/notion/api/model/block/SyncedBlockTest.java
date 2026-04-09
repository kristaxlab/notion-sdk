package io.kristixlab.notion.api.model.block;

import static org.junit.jupiter.api.Assertions.*;

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

  // original() factory

  @Test
  void original_createsBlockWithChildren() {
    SyncedBlock block =
        SyncedBlock.original(b -> b.paragraph("Synced content").bulletedListItem("Item"));

    assertEquals("synced_block", block.getType());
    assertNotNull(block.getSyncedBlock().getChildren());
    assertEquals(2, block.getSyncedBlock().getChildren().size());
    assertInstanceOf(ParagraphBlock.class, block.getSyncedBlock().getChildren().get(0));
    assertInstanceOf(BulletedListItemBlock.class, block.getSyncedBlock().getChildren().get(1));
    assertNull(block.getSyncedBlock().getSyncedFrom());
  }

  @Test
  void original_withSingleChild() {
    SyncedBlock block = SyncedBlock.original(b -> b.paragraph("Only child"));

    assertEquals(1, block.getSyncedBlock().getChildren().size());
    assertNull(block.getSyncedBlock().getSyncedFrom());
  }

  // syncedFrom() factory

  @Test
  void syncedFrom_createsBlockWithReference() {
    SyncedBlock block = SyncedBlock.syncedFrom("source-block-id");

    assertEquals("synced_block", block.getType());
    assertNotNull(block.getSyncedBlock().getSyncedFrom());
    assertEquals("source-block-id", block.getSyncedBlock().getSyncedFrom().getBlockId());
    assertNull(block.getSyncedBlock().getChildren());
  }

  // Builder

  @Test
  void builder_withChildren() {
    SyncedBlock block =
        SyncedBlock.builder().children(b -> b.heading1("Title").paragraph("Content")).build();

    assertNotNull(block.getSyncedBlock().getChildren());
    assertEquals(2, block.getSyncedBlock().getChildren().size());
    assertNull(block.getSyncedBlock().getSyncedFrom());
  }

  @Test
  void builder_withSyncedFrom() {
    SyncedBlock block = SyncedBlock.builder().syncedFrom("block-abc-123").build();

    assertNotNull(block.getSyncedBlock().getSyncedFrom());
    assertEquals("block-abc-123", block.getSyncedBlock().getSyncedFrom().getBlockId());
    assertNull(block.getSyncedBlock().getChildren());
  }

  @Test
  void builder_emptyBuild_noChildrenNoSyncedFrom() {
    SyncedBlock block = SyncedBlock.builder().build();

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

    List<Block> children = List.of(ParagraphBlock.of("Child"));
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
