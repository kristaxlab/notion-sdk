package io.kristixlab.notion.api.model.page.property;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TitleProperty extends PageProperty {
  public static final String NAME = PagePropertyType.TITLE.type();
  private final String type = PagePropertyType.TITLE.type();

  private List<RichText> title;

  public static TitleProperty of(String text) {
    return of(RichText.of(text));
  }

  public static TitleProperty of(RichText richText) {
    return of(new ArrayList<>(List.of(richText)));
  }

  public static TitleProperty of(List<RichText> richText) {
    TitleProperty property = new TitleProperty();
    property.setTitle(richText);
    return property;
  }

  public static TitleProperty of(Consumer<RichText.Builder> consumer) {
    RichText.Builder builder = RichText.builder();
    consumer.accept(builder);
    return of(builder.buildList());
  }
}
