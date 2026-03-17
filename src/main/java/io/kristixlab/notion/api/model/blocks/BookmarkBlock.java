package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class BookmarkBlock extends Block {
  @JsonProperty("bookmark")
  private Bookmark bookmark;

  public BookmarkBlock() {
    setType("bookmark");
    bookmark = new Bookmark();
  }

  @Data
  public static class Bookmark {
    @JsonProperty("url")
    private String url;

    @JsonProperty("caption")
    private List<RichText> caption;
  }
}
