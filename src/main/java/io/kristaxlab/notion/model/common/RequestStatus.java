package io.kristaxlab.notion.model.common;

import lombok.Getter;
import lombok.Setter;

/**
 * Result completeness signal on a search endpoint response or a data-source query response.
 *
 * <p>Wire fields stay {@link String} so unknown Notion tokens still deserialize (ADR 0002).
 *
 * @see io.kristaxlab.notion.model.search.SearchList
 * @see io.kristaxlab.notion.model.datasource.QueryList
 */
@Getter
@Setter
public class RequestStatus {

  /** Completeness token such as {@code complete} or {@code incomplete}. */
  private String type;

  /** Present when {@code type} is {@code incomplete}, e.g. {@code query_result_limit_reached}. */
  private String incompleteReason;
}
