package io.kristixlab.notion.api.model.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/** Filter criteria for search requests to limit results to pages or databases. */
@Data
public class SearchFilter {

  /** The value of the property to filter the results by. Possible values: "page" or "database" */
  @JsonProperty("value")
  private String value;

  /** The name of the property to filter by. For search, this is always "object" */
  @JsonProperty("property")
  private String property = "object";

  /** Creates a filter for pages only */
  public static SearchFilter pages() {
    SearchFilter filter = new SearchFilter();
    filter.setValue("page");
    return filter;
  }

  /** Creates a filter for databases only */
  public static SearchFilter databases() {
    SearchFilter filter = new SearchFilter();
    filter.setValue("database");
    return filter;
  }
}
