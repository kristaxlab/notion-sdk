package io.kristixlab.notion.api.model.page.markdown;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentUpdate {

  private String oldStr;

  private String newStr;

  private Boolean replaceAllMatches;
}
