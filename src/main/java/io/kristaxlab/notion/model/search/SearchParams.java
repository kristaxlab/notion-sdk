package io.kristaxlab.notion.model.search;

import lombok.Getter;
import lombok.Setter;

/**
 * Request body of the search endpoint.
 *
 * @see io.kristaxlab.notion.endpoints.SearchEndpoint#search(SearchParams)
 */
@Getter
@Setter
public class SearchParams {

  private String query;

  private SearchFilter filter;

  private SearchSort sort;

  private String startCursor;

  private Integer pageSize;

  /**
   * Creates a builder for search endpoint params.
   *
   * @return new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link SearchParams}. */
  public static class Builder {

    private String query;
    private SearchFilter filter;
    private SearchSort sort;
    private String startCursor;
    private Integer pageSize;

    /**
     * Sets the title substring to match.
     *
     * @param query title query; omit or {@code null} to leave unset
     * @return this builder
     */
    public Builder query(String query) {
      this.query = query;
      return this;
    }

    /**
     * Sets the search endpoint filter.
     *
     * @param filter filter object
     * @return this builder
     */
    public Builder filter(SearchFilter filter) {
      this.filter = filter;
      return this;
    }

    /**
     * Sets the search endpoint sort.
     *
     * @param sort sort object
     * @return this builder
     */
    public Builder sort(SearchSort sort) {
      this.sort = sort;
      return this;
    }

    /**
     * Sets the start cursor for the next page of results.
     *
     * @param startCursor start cursor; {@code null} starts from the first page
     * @return this builder
     */
    public Builder startCursor(String startCursor) {
      this.startCursor = startCursor;
      return this;
    }

    /**
     * Sets the page size.
     *
     * @param pageSize page size; {@code null} uses the Notion default
     * @return this builder
     */
    public Builder pageSize(Integer pageSize) {
      this.pageSize = pageSize;
      return this;
    }

    /**
     * Builds search endpoint params.
     *
     * @return new params instance
     */
    public SearchParams build() {
      SearchParams params = new SearchParams();
      params.setQuery(query);
      params.setFilter(filter);
      params.setSort(sort);
      params.setStartCursor(startCursor);
      params.setPageSize(pageSize);
      return params;
    }
  }
}
