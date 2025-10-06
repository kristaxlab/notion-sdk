package io.kristixlab.notion.api.model.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.NotionListType;
import io.kristixlab.notion.api.model.common.NotionObjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Response object for search API calls containing search results and pagination info. */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchResult extends NotionListType<NotionObjectType> {

  @JsonProperty("page_or_data_source")
  private Object pageOrDataSource;
}
