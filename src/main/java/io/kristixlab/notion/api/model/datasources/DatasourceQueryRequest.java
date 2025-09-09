package io.kristixlab.notion.api.model.datasources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.datasources.filter.Filter;
import io.kristixlab.notion.database.properties.SortDirectionType;
import io.kristixlab.notion.database.properties.TimestampType;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** Request object for querying a database. Supports filtering, sorting, and pagination. */
@Data
public class DatasourceQueryRequest {

  @JsonProperty("filter")
  private Filter filter;

  @JsonProperty("sorts")
  private List<Sort> sorts;

  @JsonProperty("start_cursor")
  private String startCursor;

  @JsonProperty("page_size")
  private Integer pageSize;

  public void setFilter(String property, Filter filter) {
    filter.setProperty(property);
    this.filter = filter;
  }

  public void addSort(Sort sort) {
    if (this.sorts == null) {
      this.sorts = new ArrayList<>();
    }
    this.sorts.add(sort);
  }

  public void addSort(String property, SortDirectionType direction) {
    if (this.sorts == null) {
      this.sorts = new ArrayList<>();
    }
    this.sorts.add(Sort.by(property, direction));
  }

  public void addSort(TimestampType timestamp, SortDirectionType direction) {
    if (this.sorts == null) {
      this.sorts = new ArrayList<>();
    }
    this.sorts.add(Sort.by(timestamp, direction));
  }
}
