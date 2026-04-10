package io.kristixlab.notion.api.model.block;

import lombok.Getter;
import lombok.Setter;

/**
 * A read-only Notion block representing a block type that the API explicitly marks as unsupported.
 *
 * <p>Unlike {@link UnknownBlock}, which handles unmapped types, this class is returned when the API
 * responds with {@code type = "unsupported"}.
 */
@Getter
@Setter
public class UnsupportedBlock extends Block {

  private Unsupported unsupported;

  /** The inner content object of an unsupported block. */
  @Getter
  @Setter
  public class Unsupported {

    private String blockType;
  }
}
