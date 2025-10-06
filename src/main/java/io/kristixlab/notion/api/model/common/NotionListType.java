package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.BaseNotionObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotionListType<T> extends BaseNotionObject {

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
