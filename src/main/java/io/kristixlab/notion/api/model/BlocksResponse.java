package io.kristixlab.notion.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.blocks.Block;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BlocksResponse extends BaseNotionResponse {

  @JsonProperty("object")
  private String object;

  @JsonProperty("results")
  private List<Block> results;

  @JsonProperty("next_cursor")
  private String nextCursor;

  @JsonProperty("has_more")
  private Boolean hasMore;

  @JsonProperty("type")
  private String type;

  @JsonProperty("block")
  private Object block;
}
