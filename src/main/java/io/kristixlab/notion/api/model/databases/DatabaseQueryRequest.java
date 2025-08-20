package io.kristixlab.notion.api.model.databases;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.databases.filter.DatabaseFilter;
import lombok.Data;

import java.util.List;

/**
 * Request object for querying a database.
 * Supports filtering, sorting, and pagination.
 */
@Data
public class DatabaseQueryRequest {

  @JsonProperty("filter")
  private DatabaseFilter filter;

  @JsonProperty("sorts")
  private List<DatabaseSort> sorts;

  @JsonProperty("start_cursor")
  private String startCursor;

  @JsonProperty("page_size")
  private Integer pageSize;

  @JsonProperty("archived")
  private Boolean archived;

  /**
   * Sort configuration for database queries.
   */
  @Data
  public static class DatabaseSort {
    @JsonProperty("property")
    private String property;

    @JsonProperty("direction")
    private String direction; // "ascending" or "descending"

    @JsonProperty("timestamp")
    private String timestamp; // "created_time" or "last_edited_time"
  }
}
