package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion bookmark block that displays a link preview for a URL.
 *
 * <p>Simple construction via {@link #of(String)}. For adding a caption use {@link #builder()}.
 */
@Getter
@Setter
public class BookmarkBlock extends Block {

  private Bookmark bookmark;

  public BookmarkBlock() {
    setType("bookmark");
    bookmark = new Bookmark();
  }

  /**
   * Creates a bookmark block with the given URL.
   *
   * @param url the URL to bookmark
   * @return a new BookmarkBlock
   */
  public static BookmarkBlock of(String url) {
    BookmarkBlock block = new BookmarkBlock();
    block.getBookmark().setUrl(url);
    return block;
  }

  /**
   * Returns a new builder for constructing a {@link BookmarkBlock} with URL and optional caption.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** The inner content object of a bookmark block. */
  @Getter
  @Setter
  public static class Bookmark {

    private String url;

    private List<RichText> caption;
  }

  /** Builder for {@link BookmarkBlock}. */
  public static class Builder {
    private String url;

    private List<RichText> caption;

    private Builder() {}

    /**
     * Sets the bookmark URL.
     *
     * @param url the URL to bookmark
     * @return this builder
     */
    public Builder url(String url) {
      this.url = url;
      return this;
    }

    /**
     * Sets the caption from a plain text string.
     *
     * @param caption the caption text
     * @return this builder
     */
    public Builder caption(String caption) {
      this.caption = RichText.of(caption);
      return this;
    }

    /**
     * Sets the caption from a pre-built rich text list.
     *
     * @param caption the caption rich text elements
     * @return this builder
     */
    public Builder caption(List<RichText> caption) {
      this.caption = caption;
      return this;
    }

    /**
     * Builds the {@link BookmarkBlock}.
     *
     * @return a new BookmarkBlock
     */
    public BookmarkBlock build() {
      BookmarkBlock block = new BookmarkBlock();
      block.getBookmark().setUrl(url);
      block.getBookmark().setCaption(caption);
      return block;
    }
  }
}
