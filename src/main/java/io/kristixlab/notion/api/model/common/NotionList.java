package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.BaseNotionResponse;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class NotionList<T> extends BaseNotionResponse {

  @JsonProperty("object")
  private String object;

  @JsonProperty("results")
  private List<T> results;

  @JsonProperty("next_cursor")
  private String nextCursor;

  @Accessors(fluent = true)
  @JsonProperty("has_more")
  private Boolean hasMore;

  @JsonProperty("type")
  private String type;
}
