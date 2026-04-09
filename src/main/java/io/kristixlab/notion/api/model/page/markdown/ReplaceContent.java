package io.kristixlab.notion.api.model.page.markdown;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplaceContent {

  private String newStr;

  private Boolean allowDeletingContent;

  public static ReplaceContent of(String newStr) {
    return of(newStr, false);
  }

  public static ReplaceContent of(String newStr, Boolean allowDeletingContent) {
    ReplaceContent rc = new ReplaceContent();
    rc.setNewStr(newStr);
    rc.setAllowDeletingContent(allowDeletingContent);
    return rc;
  }
}
