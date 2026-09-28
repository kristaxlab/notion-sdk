package io.kristaxlab.notion.endpoints.impl;

import static io.kristaxlab.notion.endpoints.util.Validator.checkNotNull;

import io.kristaxlab.notion.endpoints.SearchEndpoint;
import io.kristaxlab.notion.http.base.client.ApiClient;
import io.kristaxlab.notion.http.base.request.ApiPath;
import io.kristaxlab.notion.model.search.SearchList;
import io.kristaxlab.notion.model.search.SearchParams;
import java.util.function.Consumer;

/** Provides access to the Notion search endpoint. */
public class SearchEndpointImpl extends BaseEndpointImpl implements SearchEndpoint {

  private static final ApiPath SEARCH_PATH = ApiPath.from("/search");

  /**
   * Creates a search endpoint backed by the provided API client.
   *
   * @param client client used to execute search API requests
   */
  public SearchEndpointImpl(ApiClient client) {
    super(client);
  }

  @Override
  public SearchList search() {
    return search(new SearchParams());
  }

  @Override
  public SearchList search(String query) {
    checkNotNull(query, "query");
    return search(SearchParams.builder().query(query).build());
  }

  @Override
  public SearchList search(SearchParams request) {
    checkNotNull(request, "request");
    return getClient().call(POST, SEARCH_PATH, request, SearchList.class);
  }

  @Override
  public SearchList search(Consumer<SearchParams.Builder> consumer) {
    checkNotNull(consumer, "consumer");
    SearchParams.Builder builder = SearchParams.builder();
    consumer.accept(builder);
    return search(builder.build());
  }

  @Override
  public SearchList search(String startCursor, Integer pageSize) {
    return search(new SearchParams(), startCursor, pageSize);
  }

  @Override
  public SearchList search(SearchParams request, String startCursor, Integer pageSize) {
    checkNotNull(request, "request");
    if (startCursor != null) {
      request.setStartCursor(startCursor);
    }
    if (pageSize != null) {
      request.setPageSize(pageSize);
    }
    return search(request);
  }
}
