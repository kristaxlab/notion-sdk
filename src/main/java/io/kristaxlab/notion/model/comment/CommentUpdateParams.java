package io.kristaxlab.notion.model.comment;

import io.kristaxlab.notion.fluent.NotionText;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Request body for {@link io.kristaxlab.notion.endpoints.CommentsEndpoint#update(String,
 * CommentUpdateParams)}.
 *
 * <p>Provide exactly one body: {@code rich_text} or {@code markdown}.
 */
@Getter
@Setter
public class CommentUpdateParams {

  private List<RichText> richText;

  private String markdown;

  /**
   * Creates a builder for a comment update request.
   *
   * @return new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link CommentUpdateParams}. */
  public static class Builder {

    private List<RichText> richText;
    private String markdown;

    /**
     * Sets the updated comment body to a single plain-text rich text run.
     *
     * @param text plain text content
     * @return this builder
     */
    public Builder richText(String text) {
      return richText(List.of(NotionText.plainText(text)));
    }

    /**
     * Sets the updated comment body to the given rich text runs.
     *
     * @param richText rich text runs
     * @return this builder
     */
    public Builder richText(RichText... richText) {
      return richText(Arrays.asList(richText));
    }

    /**
     * Sets the updated comment body to the given rich text runs.
     *
     * @param richText rich text runs
     * @return this builder
     */
    public Builder richText(List<RichText> richText) {
      this.richText = new ArrayList<>(richText);
      return this;
    }

    /**
     * Sets the updated comment body to a Markdown string. Comment Markdown supports inline
     * formatting only.
     *
     * @param markdown Markdown string
     * @return this builder
     */
    public Builder markdown(String markdown) {
      this.markdown = markdown;
      return this;
    }

    /**
     * Builds the update request.
     *
     * @return comment update params
     */
    public CommentUpdateParams build() {
      CommentUpdateParams params = new CommentUpdateParams();
      if (richText != null) {
        params.setRichText(new ArrayList<>(richText));
      }
      params.setMarkdown(markdown);
      return params;
    }
  }
}
