package io.kristaxlab.notion.model.block;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Filter for {@link QueryMeetingNotesParams}: either a property filter ({@code property} + {@code
 * filter}) or a combinator ({@code operator} + {@code filters}).
 *
 * <p>Wire fields stay {@link String} (ADR 0002). Prefer the static factories when building
 * requests.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeetingNotesFilter {

  /** Combinator: {@code and} or {@code or}. */
  private String operator;

  /** Nested filters for a combinator. */
  private List<MeetingNotesFilter> filters;

  /** Property name for a single property filter. */
  private String property;

  /** Condition for a single property filter. */
  private MeetingNotesFilterCondition filter;

  /**
   * Builds a property filter.
   *
   * @param property property name
   * @param condition nested filter condition
   * @return property filter
   * @throws IllegalArgumentException if either argument is {@code null}
   */
  public static MeetingNotesFilter property(
      MeetingNotesProperty property, MeetingNotesFilterCondition condition) {
    if (property == null) {
      throw new IllegalArgumentException("property cannot be null");
    }
    return property(property.getValue(), condition);
  }

  /**
   * Builds a property filter with a raw property token.
   *
   * @param property property name token
   * @param condition nested filter condition
   * @return property filter
   * @throws IllegalArgumentException if either argument is {@code null}
   */
  public static MeetingNotesFilter property(
      String property, MeetingNotesFilterCondition condition) {
    if (property == null || property.isBlank()) {
      throw new IllegalArgumentException("property cannot be null or blank");
    }
    if (condition == null) {
      throw new IllegalArgumentException("condition cannot be null");
    }
    MeetingNotesFilter filter = new MeetingNotesFilter();
    filter.setProperty(property);
    filter.setFilter(condition);
    return filter;
  }

  /**
   * Title contains the given text.
   *
   * @param text substring to match
   * @return property filter
   */
  public static MeetingNotesFilter titleContains(String text) {
    return property(
        MeetingNotesProperty.TITLE, MeetingNotesFilterCondition.text("string_contains", text));
  }

  /**
   * Title equals the given text.
   *
   * @param text exact title
   * @return property filter
   */
  public static MeetingNotesFilter titleIs(String text) {
    return property(
        MeetingNotesProperty.TITLE, MeetingNotesFilterCondition.text("string_is", text));
  }

  /**
   * Property is not empty.
   *
   * @param property property to check
   * @return property filter
   */
  public static MeetingNotesFilter isNotEmpty(MeetingNotesProperty property) {
    return property(property, MeetingNotesFilterCondition.emptiness("is_not_empty"));
  }

  /**
   * Property is empty.
   *
   * @param property property to check
   * @return property filter
   */
  public static MeetingNotesFilter isEmpty(MeetingNotesProperty property) {
    return property(property, MeetingNotesFilterCondition.emptiness("is_empty"));
  }

  /**
   * Attendees contain the given user id.
   *
   * @param userId Notion user id
   * @return property filter
   */
  public static MeetingNotesFilter attendeesContainUser(String userId) {
    return property(
        MeetingNotesProperty.ATTENDEES,
        MeetingNotesFilterCondition.person(
            "person_contains", MeetingNotesFilterValue.personUser(userId)));
  }

  /**
   * {@code and} combinator over the given filters.
   *
   * @param filters nested filters
   * @return combinator filter
   */
  public static MeetingNotesFilter and(MeetingNotesFilter... filters) {
    return combinator("and", filters);
  }

  /**
   * {@code or} combinator over the given filters.
   *
   * @param filters nested filters
   * @return combinator filter
   */
  public static MeetingNotesFilter or(MeetingNotesFilter... filters) {
    return combinator("or", filters);
  }

  private static MeetingNotesFilter combinator(String operator, MeetingNotesFilter... filters) {
    if (filters == null || filters.length == 0) {
      throw new IllegalArgumentException("filters cannot be null or empty");
    }
    MeetingNotesFilter filter = new MeetingNotesFilter();
    filter.setOperator(operator);
    filter.setFilters(new ArrayList<>(Arrays.asList(filters)));
    return filter;
  }
}
