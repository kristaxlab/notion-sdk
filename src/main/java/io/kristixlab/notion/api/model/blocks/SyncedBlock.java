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

  @Data
  public static class Synced {
    @JsonProperty("synced_from")
    private SyncedFrom syncedFrom;

    @JsonProperty("children")
    private List<Block> children;
  }

  @Data
  public static class SyncedFrom {
    @JsonProperty("block_id")
    private String blockId;
  }
}
