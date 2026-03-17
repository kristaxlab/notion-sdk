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
public class CodeBlock extends Block {
  @JsonProperty("code")
  private Code code;

  public CodeBlock() {
    setType("code");
    code = new Code();
  }

  @Data
  public static class Code {
    @JsonProperty("rich_text")
    private List<RichText> richText;

    @JsonProperty("language")
    private String language;

    @JsonProperty("caption")
    private List<RichText> caption;
  }
}
