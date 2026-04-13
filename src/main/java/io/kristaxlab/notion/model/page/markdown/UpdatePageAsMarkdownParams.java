package io.kristaxlab.notion.model.page.markdown;

import java.util.ArrayList;
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

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private ReplaceContent replaceContent;
    private UpdateContent updateContent;

    private Boolean allowDeletingContent;

    private Builder() {}

    public Builder replaceContent(String newStr) {
      if (updateContent != null) {
        throw new IllegalStateException(
            "Cannot call replaceContent() after updateContent(); only one of these methods can be called on the builder");
      }
      if (replaceContent != null) {
        throw new IllegalStateException(
            "replaceContent() has already been called on the builder; only one call to replaceContent() is allowed");
      }
      ReplaceContent rc = new ReplaceContent();
      rc.setNewStr(newStr);
      rc.setAllowDeletingContent(false);
      return this;
    }

    public Builder updateContent(String oldStr, String newStr, boolean replaceAllMatches) {
      updateContent = ensureUpdateContent();

      ContentUpdate cu = new ContentUpdate();
      cu.setOldStr(oldStr);
      cu.setNewStr(newStr);
      cu.setReplaceAllMatches(replaceAllMatches);
      updateContent.getContentUpdates().add(cu);
      return this;
    }

    public Builder allowDeletingContent(boolean allowDeletingContent) {
      this.allowDeletingContent = allowDeletingContent;
      return this;
    }

    private UpdateContent ensureUpdateContent() {
      if (replaceContent != null) {
        throw new IllegalStateException(
            "Cannot call updateContent() after replaceContent(); only one of these methods can be called on the builder");
      }

      if (updateContent == null) {
        updateContent = new UpdateContent();
        updateContent.setContentUpdates(new ArrayList<>());
        updateContent.setAllowDeletingContent(false);
      }
      return updateContent;
    }

    public UpdatePageAsMarkdownParams build() {
      UpdatePageAsMarkdownParams params = new UpdatePageAsMarkdownParams();
      if (replaceContent != null) {
        params.setType("replace_content");
        params.setReplaceContent(replaceContent);
        if (allowDeletingContent != null) {
          params.getReplaceContent().setAllowDeletingContent(allowDeletingContent);
        }
      } else if (updateContent != null) {
        params.setType("update_content");
        params.setUpdateContent(updateContent);
        if (allowDeletingContent != null) {
          params.getUpdateContent().setAllowDeletingContent(allowDeletingContent);
        }
      } else {
        throw new IllegalStateException(
            "Either replaceContent() or updateContent() must be called on the builder before calling build()");
      }
      return params;
    }
  }
}
