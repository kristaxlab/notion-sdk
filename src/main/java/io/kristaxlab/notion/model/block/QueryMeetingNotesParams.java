package io.kristaxlab.notion.model.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Request body for querying meeting notes.
 *
 * <p>This endpoint is not cursor-paginated; use {@code filter}, {@code sort}, and {@code limit} (1–
 * 50) to refine the result set.
 *
 * @see io.kristaxlab.notion.endpoints.BlocksEndpoint#queryMeetingNotes(QueryMeetingNotesParams)
 */
@Getter
@Setter
public class QueryMeetingNotesParams {

  private MeetingNotesFilter filter;

  private List<MeetingNotesSort> sort;

  private Integer limit;

  /**
   * Creates a builder for query params.
   *
   * @return new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link QueryMeetingNotesParams}. */
  public static class Builder {

    private MeetingNotesFilter filter;
    private List<MeetingNotesSort> sort;
    private Integer limit;

    /**
     * Sets the filter.
     *
     * @param filter property or combinator filter
     * @return this builder
     */
    public Builder filter(MeetingNotesFilter filter) {
      this.filter = filter;
      return this;
    }

    /**
     * Sets the sort list.
     *
     * @param sort ordered sort entries
     * @return this builder
     */
    public Builder sort(List<MeetingNotesSort> sort) {
      this.sort = sort;
      return this;
    }

    /**
     * Sets the sort entries.
     *
     * @param sort ordered sort entries
     * @return this builder
     */
    public Builder sort(MeetingNotesSort... sort) {
      this.sort = new ArrayList<>(Arrays.asList(sort));
      return this;
    }

    /**
     * Sets the maximum number of results (1–50).
     *
     * @param limit result limit
     * @return this builder
     */
    public Builder limit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Builds the query params.
     *
     * @return new params instance
     */
    public QueryMeetingNotesParams build() {
      QueryMeetingNotesParams params = new QueryMeetingNotesParams();
      params.setFilter(filter);
      params.setSort(sort);
      params.setLimit(limit);
      return params;
    }
  }
}
