package io.kristaxlab.notion.model.block;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * The nested {@code filter} object of a meeting-notes property filter ({@code operator} plus
 * optional {@code value} / {@code use_end}).
 *
 * <p>Wire fields stay {@link String} (ADR 0002). Prefer the static factories.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeetingNotesFilterCondition {

  private String operator;

  /**
   * Comparison value: a single {@link MeetingNotesFilterValue}, a {@link List} of those values
   * (person filters), or {@code null} for empty/not-empty operators.
   */
  private Object value;

  /** When true, date comparisons use the end of a range rather than the start. */
  private Boolean useEnd;

  /**
   * Text operator with an exact string value.
   *
   * @param operator e.g. {@code string_contains}
   * @param text literal text
   * @return condition
   */
  public static MeetingNotesFilterCondition text(String operator, String text) {
    MeetingNotesFilterCondition condition = new MeetingNotesFilterCondition();
    condition.setOperator(operator);
    condition.setValue(MeetingNotesFilterValue.exactText(text));
    return condition;
  }

  /**
   * Person operator with one or more person values.
   *
   * @param operator e.g. {@code person_contains}
   * @param people person values
   * @return condition
   */
  public static MeetingNotesFilterCondition person(
      String operator, MeetingNotesFilterValue... people) {
    MeetingNotesFilterCondition condition = new MeetingNotesFilterCondition();
    condition.setOperator(operator);
    condition.setValue(Arrays.asList(people));
    return condition;
  }

  /**
   * Date operator with a typed value.
   *
   * @param operator e.g. {@code date_is}
   * @param value date value
   * @return condition
   */
  public static MeetingNotesFilterCondition date(String operator, MeetingNotesFilterValue value) {
    MeetingNotesFilterCondition condition = new MeetingNotesFilterCondition();
    condition.setOperator(operator);
    condition.setValue(value);
    return condition;
  }

  /**
   * Empty / not-empty operator (no value).
   *
   * @param operator {@code is_empty} or {@code is_not_empty}
   * @return condition
   */
  public static MeetingNotesFilterCondition emptiness(String operator) {
    MeetingNotesFilterCondition condition = new MeetingNotesFilterCondition();
    condition.setOperator(operator);
    return condition;
  }

  /**
   * Marks this date condition to compare against the end of a range.
   *
   * @return this condition
   */
  public MeetingNotesFilterCondition useEnd() {
    this.useEnd = true;
    return this;
  }
}
