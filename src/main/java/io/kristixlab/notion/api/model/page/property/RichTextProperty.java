package io.kristixlab.notion.api.model.page.property;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RichTextProperty extends PageProperty {
  private final String type = PagePropertyType.RICH_TEXT.type();

  private List<RichText> richText = new ArrayList<>();

  public static RichTextProperty of(String text) {
    return of(RichText.of(text));
  }

  public static RichTextProperty of(RichText richText) {
    return of(new ArrayList<>(List.of(richText)));
  }

  public static RichTextProperty of(List<RichText> richText) {
    RichTextProperty property = new RichTextProperty();
    property.getRichText().addAll(richText);
    return property;
  }

  public static RichTextProperty of(Consumer<RichText.Builder> consumer) {
    RichText.Builder builder = RichText.builder();
    consumer.accept(builder);
    return of(builder.buildList());
  }
}
