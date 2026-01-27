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
public class TitleProperty extends PageProperty {
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
    property.setTitle(new ArrayList<>());
    property.getTitle().add(RichText.of(text));
    return property;
  }
}
