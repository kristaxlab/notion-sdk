package io.kristaxlab.notion.model.common;

import io.kristaxlab.notion.model.BaseNotionObject;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionList<T> extends BaseNotionObject {

  private List<T> results;

  private String type;

  private Boolean hasMore;

  private String nextCursor;
}
