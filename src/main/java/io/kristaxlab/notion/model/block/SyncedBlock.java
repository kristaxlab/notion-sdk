package io.kristaxlab.notion.model.block;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** A Notion synced block. */
@Getter
@Setter
public class SyncedBlock extends Block {

  private Synced syncedBlock;

  public SyncedBlock() {
    setType(BlockType.SYNCED_BLOCK.getValue());
    syncedBlock = new Synced();
  }

  /** Represents the inner content object of a synced block. */
  @Getter
  @Setter
  public static class Synced {

    /**
     * For original blocks this is {@code null}. For copies it identifies the original block. Always
     * included to avoid VALIDATION_ERROR from Notion API.
     */
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private SyncedFrom syncedFrom;

    /**
     * The content blocks.
     */
    private List<Block> children;
  }

  /** Identifies the original block that a synced copy references. */
  @Getter
  @Setter
  public static class SyncedFrom {

    private String type;

    /** The ID of the original synced block. */
    private String blockId;
  }
}
