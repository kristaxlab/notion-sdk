package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class UnsupportedBlock extends Block {
  @JsonProperty("unsupported")
  private Unsupported unsupported;

  @Data
  public class Unsupported {
    @JsonProperty("block_type")
    private String blockType;
  }
}
