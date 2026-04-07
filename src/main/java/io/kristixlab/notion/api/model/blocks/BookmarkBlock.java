package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

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

  public static Builder builder() {
    return new Builder();
  }

  @Getter
  @Setter
  public static class Bookmark {

    private String url;

    private List<RichText> caption;
  }

  public static class Builder {
    private String url;

    private List<RichText> caption;

    private Builder() {}

    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public Builder caption(String caption) {
      this.caption = RichText.of(caption);
      return this;
    }

    public Builder caption(List<RichText> caption) {
      this.caption = caption;
      return this;
    }

    public BookmarkBlock build() {
      BookmarkBlock block = new BookmarkBlock();
      block.getBookmark().setUrl(url);
      block.getBookmark().setCaption(caption);
      return block;
    }
  }
}
