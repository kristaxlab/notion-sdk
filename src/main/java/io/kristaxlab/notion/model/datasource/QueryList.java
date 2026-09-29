package io.kristaxlab.notion.model.datasource;

import io.kristaxlab.notion.model.common.NotionList;
import io.kristaxlab.notion.model.common.RequestStatus;
import io.kristaxlab.notion.model.page.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * Response object for database query operations. Contains the pages that match the query criteria.
 */
@Getter
@Setter
public class QueryList extends NotionList<Page> {

  private Object pageOrDataSource;

  /** Optional result completeness signal for this response page. */
  private RequestStatus requestStatus;
}
