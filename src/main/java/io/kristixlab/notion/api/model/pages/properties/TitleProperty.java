package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import io.kristixlab.notion.api.util.PagePropertyType;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TitleProperty extends PageProperty {
  public static final String NAME = "title";
  private final String type = PagePropertyType.TITLE.type();

  @JsonProperty("title")
  private List<RichText> title;

  public static TitleProperty of(List<RichText> title) {
    TitleProperty property = new TitleProperty();
    property.setTitle(title);
    return property;
  }

  public static TitleProperty of(String text) {
    TitleProperty property = new TitleProperty();
    List<RichText> richTexts = RichText.builder().fromText(text).buildAsList();
    property.setTitle(richTexts);
    return property;
  }
}
