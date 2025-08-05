package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class CalloutBlock extends Block {

  @JsonProperty("callout")
  private Callout callout;

  @Data
  public static class Callout {
    @JsonProperty("rich_text")
    private List<RichText> richText;

    @JsonProperty("icon")
    private Icon icon;

    @JsonProperty("color")
    private String color;

    @JsonProperty("children")
    private List<Block> children;
  }

  @Data
  public static class Icon {
    @JsonProperty("type")
    private String type;

    @JsonProperty("emoji")
    private String emoji;

    @JsonProperty("external")
    private External external;

    @JsonProperty("file")
    private File file;
  }

  @Data
  public static class External {
    @JsonProperty("url")
    private String url;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class File {
    @JsonProperty("url")
    private String url;

    @JsonProperty("expiry_time")
    private String expiryTime;
  }
}
