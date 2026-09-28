package io.kristaxlab.notion.endpoints;

import io.kristaxlab.notion.model.search.SearchList;
import io.kristaxlab.notion.model.search.SearchParams;
import java.util.function.Consumer;

/**
 * Notion Search API: search pages and data sources shared with the connection by title.
 *
 * @see <a href="https://developers.notion.com/reference/post-search">Search by title</a>
 */
public interface SearchEndpoint {

  /**
   * Searches with an empty body (Notion defaults for everything shared with the connection).
   *
   * @return paginated search endpoint response
   */
  SearchList search();

  /**
   * Searches for titles that include the given query string.
   *
   * @param query title substring to match
   * @return paginated search endpoint response
   * @throws IllegalArgumentException if {@code query} is {@code null}
   */
  SearchList search(String query);

  /**
   * Searches using the given search endpoint params.
   *
   * @param request search endpoint params
   * @return paginated search endpoint response
   * @throws IllegalArgumentException if {@code request} is {@code null}
   */
  SearchList search(SearchParams request);

  /**
   * Searches by configuring {@link SearchParams.Builder} in a lambda.
   *
   * @param consumer callback that fills the params builder
   * @return paginated search endpoint response
   * @throws IllegalArgumentException if {@code consumer} is {@code null}
   */
  SearchList search(Consumer<SearchParams.Builder> consumer);

  /**
   * Searches with start cursor and page size only.
   *
   * @param startCursor start cursor; {@code null} starts from the first page
   * @param pageSize page size; {@code null} uses the Notion default
   * @return paginated search endpoint response
   */
  SearchList search(String startCursor, Integer pageSize);

  /**
   * Searches using the given params, stamping start cursor and page size onto the request when
   * provided.
   *
   * @param request search endpoint params
   * @param startCursor start cursor; {@code null} leaves the request unchanged
   * @param pageSize page size; {@code null} leaves the request unchanged
   * @return paginated search endpoint response
   * @throws IllegalArgumentException if {@code request} is {@code null}
   */
  SearchList search(SearchParams request, String startCursor, Integer pageSize);
}
