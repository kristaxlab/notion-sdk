package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.kristixlab.notion.api.model.helper.BlocksBuilder;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion synced block.
 *
 * <p>Synced blocks come in two forms:
 *
 * <ul>
 *   <li><b>Original</b> — holds the content; {@code synced_from} is {@code null} in the API.
 *   <li><b>Copy</b> — references an original block by ID; {@code synced_from.block_id} is set.
 * </ul>
 *
 * <p>Use the static factories for the common cases:
 *
 * <pre>{@code
 * // Create an original synced block with content
 * SyncedBlock original = SyncedBlock.original(b -> b
 *     .paragraph("This content can be synced elsewhere.")
 *     .bulletedListItem("item"));
 *
 * // Create a copy pointing to an existing original block
 * SyncedBlock copy = SyncedBlock.syncedFrom("source-block-id");
 * }</pre>
 *
 * <p>Use {@link #builder()} when you need incremental construction.
 */
@Getter
@Setter
public class SyncedBlock extends Block {

  private Synced syncedBlock;

  public SyncedBlock() {
    setType("synced_block");
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

    /** The content blocks. Populated only on original blocks; {@code null} on copies. */
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

  /**
   * Creates an original synced block whose content is defined by a {@link BlocksBuilder} consumer.
   * The {@code synced_from} field is left {@code null}, as required by the Notion API for original
   * blocks.
   *
   * @param contentConsumer a consumer that populates the block's children
   * @return a new original SyncedBlock
   */
  public static SyncedBlock original(Consumer<BlocksBuilder> contentConsumer) {
    return builder().children(contentConsumer).build();
  }

  /**
   * Creates a synced copy that references an existing original block.
   *
   * @param sourceBlockId the ID of the original synced block to reference
   * @return a new SyncedBlock copy
   */
  public static SyncedBlock syncedFrom(String sourceBlockId) {
    return builder().syncedFrom(sourceBlockId).build();
  }

  /**
   * Returns a new builder for constructing a {@link SyncedBlock}.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder for {@link SyncedBlock}.
   *
   * <p>Either set children (original block) or call {@link #syncedFrom(String)} (copy), but not
   * both — these are mutually exclusive in the Notion API.
   */
  public static class Builder {

    private List<Block> children;
    private String sourceBlockId;

    private Builder() {}

    /**
     * Adds child blocks from a pre-built list, making this block an <em>original</em>.
     *
     * @param children the child blocks to add
     * @return this builder
     */
    public Builder children(List<Block> children) {
      getChildren().addAll(children);
      return this;
    }

    /**
     * Sets the content children of an original synced block via a {@link BlocksBuilder} consumer.
     * Calling this makes the block an <em>original</em>; do not also call {@link #syncedFrom}.
     *
     * @param contentConsumer a consumer that populates the block's children
     * @return this builder
     */
    public Builder children(Consumer<BlocksBuilder> contentConsumer) {
      BlocksBuilder b = BlocksBuilder.of();
      contentConsumer.accept(b);
      getChildren().addAll(b.build());
      return this;
    }

    /**
     * Makes this block a synced <em>copy</em> pointing to the given original block. Do not also
     * call {@link #children} — copies have no independent children.
     *
     * @param sourceBlockId the ID of the original synced block to reference
     * @return this builder
     */
    public Builder syncedFrom(String sourceBlockId) {
      this.sourceBlockId = sourceBlockId;
      return this;
    }

    private List<Block> getChildren() {
      if (children == null) {
        children = new java.util.ArrayList<>();
      }
      return children;
    }

    /**
     * Builds the {@link SyncedBlock}.
     *
     * @return a new SyncedBlock
     */
    public SyncedBlock build() {
      SyncedBlock block = new SyncedBlock();
      if (sourceBlockId != null) {
        SyncedFrom syncedFrom = new SyncedFrom();
        block.getSyncedBlock().setSyncedFrom(syncedFrom);
        syncedFrom.setBlockId(sourceBlockId);
      }
      if (children != null) {
        block.getSyncedBlock().setChildren(children);
      }
      return block;
    }
  }
}
