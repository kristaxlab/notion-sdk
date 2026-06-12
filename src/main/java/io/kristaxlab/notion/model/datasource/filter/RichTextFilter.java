package io.kristaxlab.notion.model.datasource.filter;

import io.kristixlab.notion.api.model.datasources.filter.condition.TextFilterCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RichTextFilter extends Filter {

  private TextFilterCondition richText;

  public static RichTextFilter isEmpty() {
    RichTextFilter filter = new RichTextFilter();
    TextFilterCondition condition = new TextFilterCondition();
    condition.setIsEmpty(true);
    filter.setRichText(condition);
    return filter;
  }

  public static RichTextFilter isNotEmpty() {
    RichTextFilter filter = new RichTextFilter();
    TextFilterCondition condition = new TextFilterCondition();
    condition.setIsNotEmpty(true);
    filter.setRichText(condition);
    return filter;
  }

  public static RichTextFilter contains(String value) {
    RichTextFilter filter = new RichTextFilter();
    TextFilterCondition condition = new TextFilterCondition();
    condition.setContains(value);
    filter.setRichText(condition);
    return filter;
  }

  public static RichTextFilter doesNotContain(String value) {
    RichTextFilter filter = new RichTextFilter();
    TextFilterCondition condition = new TextFilterCondition();
    condition.setDoesNotContain(value);
    filter.setRichText(condition);
    return filter;
  }

  public static RichTextFilter equals(String value) {
    RichTextFilter filter = new RichTextFilter();
    TextFilterCondition condition = new TextFilterCondition();
    condition.setEquals(value);
    filter.setRichText(condition);
    return filter;
  }

  public static RichTextFilter doesNotEqual(String value) {
    RichTextFilter filter = new RichTextFilter();
    TextFilterCondition condition = new TextFilterCondition();
    condition.setDoesNotEqual(value);
    filter.setRichText(condition);
    return filter;
  }
}
