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

  @Getter
  @Setter
  public static class Bookmark {

    private String url;

    private List<RichText> caption;
  }
}
