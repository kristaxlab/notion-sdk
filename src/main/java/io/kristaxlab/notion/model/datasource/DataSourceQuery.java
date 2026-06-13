package io.kristaxlab.notion.model.datasource;

import io.kristaxlab.notion.model.datasource.filter.Filter;
import io.kristaxlab.notion.model.datasource.sort.Sort;
import io.kristaxlab.notion.model.datasource.sort.SortDirection;
import io.kristaxlab.notion.model.datasource.sort.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Request object for querying a database. Supports filtering, sorting, and pagination. */
@Getter
@Setter
public class DataSourceQuery {

  private Filter filter;

  private List<Sort> sorts;

  private String startCursor;

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

  public void addSort(String property, SortDirection direction) {
    if (this.sorts == null) {
      this.sorts = new ArrayList<>();
    }
    this.sorts.add(Sort.by(property, direction));
  }

  public void addSort(Timestamp timestamp, SortDirection direction) {
    if (this.sorts == null) {
      this.sorts = new ArrayList<>();
    }
    this.sorts.add(Sort.by(timestamp, direction));
  }
}
