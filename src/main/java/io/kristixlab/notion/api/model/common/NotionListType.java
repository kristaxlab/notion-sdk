package io.kristixlab.notion.api.model.common;

import io.kristixlab.notion.api.model.BaseNotionObject;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class NotionListType<T> extends BaseNotionObject {

  private List<T> results;

  private String nextCursor;

  @Accessors(fluent = true)
  private Boolean hasMore;

  private String type;
}
