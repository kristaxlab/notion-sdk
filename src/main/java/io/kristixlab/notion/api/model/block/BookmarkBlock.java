package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import io.kristixlab.notion.api.model.helper.NotionText;
import io.kristixlab.notion.api.model.helper.NotionTextBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

/** A Notion bookmark block that displays a link preview for a URL. */
@Getter
@Setter
public class BookmarkBlock extends Block {

  private Bookmark bookmark;

  public BookmarkBlock() {
    setType("bookmark");
    bookmark = new Bookmark();
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

    private List<RichText> caption = new ArrayList<>();

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

    public Builder caption(String caption) {
      this.caption.add(NotionText.plainText(caption));
      return this;
    }

    public Builder caption(RichText... caption) {
      return caption(Arrays.asList(caption));
    }

    public Builder caption(List<RichText> caption) {
      this.caption.addAll(caption);
      return this;
    }

    public Builder caption(Consumer<NotionTextBuilder> consumer) {
      NotionTextBuilder builder = new NotionTextBuilder();
      consumer.accept(builder);
      this.caption.addAll(builder.build());
      return this;
    }

    /**
     * Builds the {@link BookmarkBlock}.
     *
     * @return a new BookmarkBlock
     */
    public BookmarkBlock build() {
      if (url == null || url.isEmpty()) {
        throw new IllegalStateException("Bookmark URL cannot be null or empty");
      }
      BookmarkBlock block = new BookmarkBlock();
      block.getBookmark().setUrl(url);
      if (caption != null && !caption.isEmpty()) {
        block.getBookmark().setCaption(caption);
      }
      return block;
    }
  }
}
