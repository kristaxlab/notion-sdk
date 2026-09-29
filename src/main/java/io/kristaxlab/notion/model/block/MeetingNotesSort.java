package io.kristaxlab.notion.model.block;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.kristaxlab.notion.model.common.SortDirection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * One sort entry on {@link QueryMeetingNotesParams}.
 *
 * <p>Wire fields stay {@link String} (ADR 0002). Prefer the static factories when building
 * requests.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeetingNotesSort {

  private String property;

  private String direction;

  /**
   * Sorts by the given property and direction.
   *
   * @param property sort property
   * @param direction ascending or descending
   * @return sort entry
   * @throws IllegalArgumentException if either argument is {@code null}
   */
  public static MeetingNotesSort of(MeetingNotesProperty property, SortDirection direction) {
    if (property == null) {
      throw new IllegalArgumentException("property cannot be null");
    }
    if (direction == null) {
      throw new IllegalArgumentException("direction cannot be null");
    }
    MeetingNotesSort sort = new MeetingNotesSort();
    sort.setProperty(property.getValue());
    sort.setDirection(direction.getValue());
    return sort;
  }

  /**
   * Sorts by last edited time descending.
   *
   * @return sort entry
   */
  public static MeetingNotesSort byLastEditedTimeDescending() {
    return of(MeetingNotesProperty.LAST_EDITED_TIME, SortDirection.DESCENDING);
  }

  /**
   * Copies the given sorts into a mutable list suitable for {@link QueryMeetingNotesParams}.
   *
   * @param sorts sort entries
   * @return mutable list
   */
  public static List<MeetingNotesSort> listOf(MeetingNotesSort... sorts) {
    return new ArrayList<>(Arrays.asList(sorts));
  }
}
