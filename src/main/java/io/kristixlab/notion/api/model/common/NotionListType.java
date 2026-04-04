package io.kristixlab.notion.api.model.common;

import io.kristixlab.notion.api.model.BaseNotionObject;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionListType<T> extends BaseNotionObject {

  private List<T> results;

  private String type;

  private Boolean hasMore;

  private String nextCursor;
}
