package io.kristixlab.notion.api.model.blocks;

/** Request object for updating a block. */
public class UpdateBlockRequest {
  private Boolean archived;
  private Block blockData; // This would contain the specific block type data

  public UpdateBlockRequest() {}

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Block getBlockData() {
    return blockData;
  }

  public void setBlockData(Block blockData) {
    this.blockData = blockData;
  }
}
