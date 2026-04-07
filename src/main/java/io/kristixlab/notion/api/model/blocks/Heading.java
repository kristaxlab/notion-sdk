package io.kristixlab.notion.api.model.blocks;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Heading extends BlockWithChildren {

  private Boolean isToggleable;
}
