package io.kristixlab.notion.api.model.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Request object for searching through pages and databases in a workspace.
 */
@Data
public class SearchRequest {

  /**
   * The text that the API compares page and database titles against.
   */
  @JsonProperty("query")
  private String query;

  /**
   * A set of criteria, each corresponding to a property, that limits the results to either only
   * pages or only databases.
   */
  @JsonProperty("filter")
  private SearchFilter filter;

  /**
   * The direction to sort results. Can be "ascending" or "descending".
   */
  @JsonProperty("sort")
  private SearchSort sort;

  /**
   * A cursor returned from a previous call to use for pagination.
   */
  @JsonProperty("start_cursor")
  private String startCursor;

  /**
   * The number of items from the full list desired in the response. Maximum: 100
   */
  @JsonProperty("page_size")
  private Integer pageSize;
}
