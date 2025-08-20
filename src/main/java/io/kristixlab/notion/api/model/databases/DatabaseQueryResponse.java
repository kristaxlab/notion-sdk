package io.kristixlab.notion.api.model.databases;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.NotionList;
import io.kristixlab.notion.api.model.pages.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Response object for database query operations. Contains the pages that match the query criteria.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DatabaseQueryResponse extends NotionList<Page> {

  @JsonProperty("page_or_database")
  private Object pageOrDatabase;
}
