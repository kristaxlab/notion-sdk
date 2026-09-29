package io.kristaxlab.notion.model.comment;

import io.kristaxlab.notion.fluent.NotionText;
import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Request body for {@link
 * io.kristaxlab.notion.endpoints.CommentsEndpoint#create(CommentCreateParams)}.
 *
 * <p>Provide exactly one target: a page parent, a block parent, or a {@code discussion_id}. Provide
 * exactly one body: {@code rich_text} or {@code markdown}.
 */
@Getter
@Setter
public class CommentCreateParams {

  private Parent parent;

  private String discussionId;

  private List<RichText> richText;

  private String markdown;

  private List<CommentAttachment> attachments;

  private CommentDisplayName displayName;

  /**
   * Creates a builder for a comment create request.
   *
   * @return new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link CommentCreateParams}. */
  public static class Builder {

    private Parent parent;
    private String discussionId;
    private List<RichText> richText;
    private String markdown;
    private List<CommentAttachment> attachments;
    private CommentDisplayName displayName;

    /**
     * Sets a fully constructed {@link Parent}. Comments accept a page or a block parent.
     *
     * @param parent parent descriptor
     * @return this builder
     */
    public Builder parent(Parent parent) {
      this.parent = parent;
      return this;
    }

    /**
     * Sets the parent to an existing page.
     *
     * @param pageId parent page identifier
     * @return this builder
     */
    public Builder inPage(String pageId) {
      return parent(Parent.pageParent(pageId));
    }

    /**
     * Sets the parent to an existing block.
     *
     * @param blockId parent block identifier
     * @return this builder
     */
    public Builder inBlock(String blockId) {
      return parent(Parent.blockParent(blockId));
    }

    /**
     * Sets {@code discussion_id} on the create request.
     *
     * @param discussionId value sent as {@code discussion_id}
     * @return this builder
     */
    public Builder discussionId(String discussionId) {
      this.discussionId = discussionId;
      return this;
    }

    /**
     * Sets the comment body to a single plain-text rich text run.
     *
     * @param text plain text content
     * @return this builder
     */
    public Builder richText(String text) {
      return richText(List.of(NotionText.plainText(text)));
    }

    /**
     * Sets the comment body to the given rich text runs.
     *
     * @param richText rich text runs
     * @return this builder
     */
    public Builder richText(RichText... richText) {
      return richText(Arrays.asList(richText));
    }

    /**
     * Sets the comment body to the given rich text runs.
     *
     * @param richText rich text runs
     * @return this builder
     */
    public Builder richText(List<RichText> richText) {
      this.richText = new ArrayList<>(richText);
      return this;
    }

    /**
     * Sets the comment body to a Markdown string. Comment Markdown supports inline formatting only.
     *
     * @param markdown Markdown string
     * @return this builder
     */
    public Builder markdown(String markdown) {
      this.markdown = markdown;
      return this;
    }

    /**
     * Replaces the attachment list.
     *
     * @param attachments comment attachments
     * @return this builder
     */
    public Builder attachments(List<CommentAttachment> attachments) {
      this.attachments = new ArrayList<>(attachments);
      return this;
    }

    /**
     * Adds one comment attachment.
     *
     * @param attachment comment attachment
     * @return this builder
     */
    public Builder attachment(CommentAttachment attachment) {
      if (this.attachments == null) {
        this.attachments = new ArrayList<>();
      }
      this.attachments.add(attachment);
      return this;
    }

    /**
     * Sets the comment display name.
     *
     * @param displayName comment display name
     * @return this builder
     */
    public Builder displayName(CommentDisplayName displayName) {
      this.displayName = displayName;
      return this;
    }

    /**
     * Builds the create request.
     *
     * @return comment create params
     */
    public CommentCreateParams build() {
      CommentCreateParams params = new CommentCreateParams();
      params.setParent(parent);
      params.setDiscussionId(discussionId);
      if (richText != null) {
        params.setRichText(new ArrayList<>(richText));
      }
      params.setMarkdown(markdown);
      if (attachments != null) {
        params.setAttachments(new ArrayList<>(attachments));
      }
      params.setDisplayName(displayName);
      return params;
    }
  }
}
