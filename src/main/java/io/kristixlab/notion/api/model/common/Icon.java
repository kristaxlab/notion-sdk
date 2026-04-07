package io.kristixlab.notion.api.model.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Icon {

  private String type;

  private String emoji;

  private FileData file;
}
