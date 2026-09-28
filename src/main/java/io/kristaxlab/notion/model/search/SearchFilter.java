package io.kristaxlab.notion.model.search;

import lombok.Getter;
import lombok.Setter;

/**
 * Search endpoint filter: limit matches by object ({@code page} or {@code data_source}) and/or
 * whether the match is in the trash.
 *
 * <p>Wire fields stay {@link String} so unknown Notion tokens still deserialize (ADR 0002). Write
 * helpers hard-code the tokens the SDK builds today.
 */
@Getter
@Setter
public class SearchFilter {

  private String property;

  private String value;

  private Boolean inTrash;

  /**
   * Filter that keeps only pages.
   *
   * @return filter with {@code property=object} and {@code value=page}
   */
  public static SearchFilter pages() {
    SearchFilter filter = new SearchFilter();
    filter.setProperty("object");
    filter.setValue("page");
    return filter;
  }

  /**
   * Filter that keeps only data sources.
   *
   * @return filter with {@code property=object} and {@code value=data_source}
   */
  public static SearchFilter dataSources() {
    SearchFilter filter = new SearchFilter();
    filter.setProperty("object");
    filter.setValue("data_source");
    return filter;
  }

  /**
   * Trash-only filter (no object constraint).
   *
   * @param inTrash {@code true} for trashed matches; {@code false} for non-trashed
   * @return filter with only {@code in_trash} set
   */
  public static SearchFilter inTrash(boolean inTrash) {
    SearchFilter filter = new SearchFilter();
    filter.setInTrash(inTrash);
    return filter;
  }

  /**
   * Combines this object filter with an {@code in_trash} constraint.
   *
   * @param inTrash {@code true} for trashed matches; {@code false} for non-trashed
   * @return this filter
   */
  public SearchFilter withInTrash(boolean inTrash) {
    this.inTrash = inTrash;
    return this;
  }
}
