package io.kristaxlab.notion.model.block;

import lombok.Getter;
import lombok.Setter;

/**
 * Shared content model for heading blocks, extending {@link BlockWithChildren} with a toggleable
 * flag that controls whether the heading can expand/collapse child content.
 *
 * @see HeadingOneBlock
 * @see HeadingTwoBlock
 * @see HeadingThreeBlock
 * @see HeadingFourBlock
 */
@Getter
@Setter
public final class Heading extends BlockWithChildren {

  /** Whether the heading acts as a toggle that can reveal or hide child blocks. */
  private Boolean isToggleable;
}
