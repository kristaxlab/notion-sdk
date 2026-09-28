package io.kristaxlab.notion.model.search;

import io.kristaxlab.notion.model.common.NotionList;
import io.kristaxlab.notion.model.common.NotionObject;
import io.kristaxlab.notion.model.common.RequestStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * Paginated response of the search endpoint. Results are pages and data sources interleaved; each
 * element is a {@link NotionObject} narrowed by the caller.
 */
@Getter
@Setter
public class SearchList extends NotionList<NotionObject> {

  /** Empty object present on Notion list responses whose type is {@code page_or_data_source}. */
  private Object pageOrDataSource;

  /** Optional result completeness signal for this response page. */
  private RequestStatus requestStatus;
}
