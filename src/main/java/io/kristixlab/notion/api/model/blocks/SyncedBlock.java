package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SyncedBlock extends Block {

  private Synced syncedBlock;

  public SyncedBlock() {
    setType("synced_block");
    syncedBlock = new Synced();
  }

  @Getter
  @Setter
  public static class Synced {

    /*
     *  This field is always included, even if null because otherwise API doesn't let you add a new Synced Block
     */
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private SyncedFrom syncedFrom;

    private List<Block> children;
  }

  @Getter
  @Setter
  public static class SyncedFrom {

    private String type;

    private String blockId;
  }
}
