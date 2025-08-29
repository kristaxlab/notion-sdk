package io.kristixlab.notion.api.model.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.NotionList;
import io.kristixlab.notion.api.model.common.NotionObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Response object for search API calls containing search results and pagination info. */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchResponse extends NotionList<NotionObject> {

  @JsonProperty("page_or_database")
  private Object pageOrDatabase;
}
