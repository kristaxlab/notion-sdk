package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import io.kristixlab.notion.api.util.PagePropertyType;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RichTextProperty extends PageProperty {
  private final String type = PagePropertyType.RICH_TEXT.type();

  @JsonProperty("rich_text")
  private List<RichText> richText = new ArrayList<>();

  public static RichTextProperty of(String text) {
    RichTextProperty property = new RichTextProperty();
    property.getRichText().add(RichText.of(text));
    return property;
  }

  public static RichTextProperty of(List<RichText> richText) {
    RichTextProperty property = new RichTextProperty();
    property.getRichText().addAll(richText);
    return property;
  }
}
