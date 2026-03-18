package io.kristixlab.notion.api.model.pages;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class UpdatePageAsMarkdownParams {

  /* update_content or replace_content  */
  @JsonProperty("type")
  private String type;

  @JsonProperty("update_content")
  private UpdateContent updateContent;

  @JsonProperty("replace_content")
  private ReplaceContent replaceContent;

  public static UpdatePageAsMarkdownParams replaceContent(String newStr) {
    UpdatePageAsMarkdownParams params = new UpdatePageAsMarkdownParams();
    params.setType("replace_content");
    ReplaceContent rc = new ReplaceContent();
    rc.setNewStr(newStr);
    params.setReplaceContent(rc);
    return params;
  }

  public static UpdatePageAsMarkdownParams updateContent(List<ContentUpdate> updates) {
    UpdatePageAsMarkdownParams params = new UpdatePageAsMarkdownParams();
    params.setType("update_content");
    UpdateContent uc = new UpdateContent();
    uc.setContentUpdates(updates);
    params.setUpdateContent(uc);
    return params;
  }

  @Data
  public static class UpdateContent {

    @JsonProperty("content_updates")
    private List<ContentUpdate> contentUpdates;

    @JsonProperty("allow_deleting_content")
    private Boolean allowDeletingContent;
  }

  @Data
  public static class ContentUpdate {

    @JsonProperty("old_str")
    private String oldStr;

    @JsonProperty("new_str")
    private String newStr;

    @JsonProperty("replace_all_matches")
    private Boolean replaceAllMatches;

    public static ContentUpdate of(String oldStr, String newStr) {
      ContentUpdate cu = new ContentUpdate();
      cu.setOldStr(oldStr);
      cu.setNewStr(newStr);
      cu.setReplaceAllMatches(true);
      return cu;
    }
  }

  @Data
  public static class ReplaceContent {

    @JsonProperty("new_str")
    private String newStr;

    @JsonProperty("allow_deleting_content")
    private Boolean allowDeletingContent;
  }
}
