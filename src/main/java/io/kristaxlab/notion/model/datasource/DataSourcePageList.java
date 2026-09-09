package io.kristaxlab.notion.model.datasource;

import io.kristaxlab.notion.model.common.NotionList;
import io.kristaxlab.notion.model.page.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * Response object for database query operations. Contains the pages that match the query criteria.
 */
@Getter
@Setter
public class DataSourcePageList extends NotionList<Page> {

  private Object pageOrDataSource;
}
