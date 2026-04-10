package io.kristixlab.notion.api.model.page.markdown;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePageAsMarkdownParams {

  /* update_content or replace_content  */
  private String type;

  private UpdateContent updateContent;

  private ReplaceContent replaceContent;

  public static UpdatePageAsMarkdownParams replaceContent(String newStr) {
    return replaceContent(newStr, false);
  }

  public static UpdatePageAsMarkdownParams replaceContent(
      String newStr, boolean allowDeletingContent) {
    ReplaceContent rc = new ReplaceContent();
    rc.setNewStr(newStr);
    rc.setAllowDeletingContent(allowDeletingContent);
    UpdatePageAsMarkdownParams params = new UpdatePageAsMarkdownParams();
    params.setType("replace_content");
    params.setReplaceContent(rc);
    return params;
  }

  public static UpdatePageAsMarkdownParams updateContent(List<ContentUpdate> updates) {
    return updateContent(updates, false);
  }

  public static UpdatePageAsMarkdownParams updateContent(
      List<ContentUpdate> updates, boolean allowDeletingContent) {
    UpdatePageAsMarkdownParams params = new UpdatePageAsMarkdownParams();
    params.setType("update_content");
    UpdateContent uc = new UpdateContent();
    uc.setContentUpdates(updates);
    uc.setAllowDeletingContent(allowDeletingContent);
    params.setUpdateContent(uc);
    return params;
  }
}
