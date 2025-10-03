package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class RichTextProperty extends PageProperty {
  private final String type = "rich_text";

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
