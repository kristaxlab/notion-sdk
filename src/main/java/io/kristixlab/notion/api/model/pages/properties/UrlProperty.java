package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.util.PagePropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UrlProperty extends PageProperty {
  private final String type = PagePropertyType.URL.type();

  @JsonProperty("url")
  private String url;

  public static UrlProperty of(String url) {
    UrlProperty property = new UrlProperty();
    property.setUrl(url);
    return property;
  }
}
