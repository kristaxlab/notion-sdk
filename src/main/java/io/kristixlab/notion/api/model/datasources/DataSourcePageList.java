package io.kristixlab.notion.api.model.datasources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.NotionListType;
import io.kristixlab.notion.api.model.pages.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Response object for database query operations. Contains the pages that match the query criteria.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataSourcePageList extends NotionListType<Page> {

  @JsonProperty("page_or_data_source")
  private Object pageOrDataSource;
}
