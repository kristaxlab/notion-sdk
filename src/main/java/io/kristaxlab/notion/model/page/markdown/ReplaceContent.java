package io.kristaxlab.notion.model.page.markdown;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplaceContent {

  private String newStr;

  private Boolean allowDeletingContent;
}
