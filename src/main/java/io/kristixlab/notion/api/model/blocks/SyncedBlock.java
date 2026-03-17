package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class SyncedBlock extends Block {

  @JsonProperty("synced_block")
  private Synced syncedBlock;

  public SyncedBlock() {
    setType("synced_block");
    syncedBlock = new Synced();
  }

  @Data
  public static class Synced {

    /*
     *  This field is always included, even if null because otherwise API doesn't let you add a new Synced Block
     */
    @JsonProperty("synced_from")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private SyncedFrom syncedFrom;

    @JsonProperty("children")
    private List<Block> children;
  }

  @Data
  public static class SyncedFrom {
    @JsonProperty("type")
    private String type;

    @JsonProperty("block_id")
    private String blockId;
  }
}
