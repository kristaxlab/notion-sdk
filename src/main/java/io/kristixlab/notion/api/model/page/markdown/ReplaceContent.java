package io.kristixlab.notion.api.model.page.markdown;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplaceContent {

  private String newStr;

  private Boolean allowDeletingContent;
}
