package io.kristixlab.notion.api.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyItem extends PageProperty {

  private final String type = "property_item";

  private PropertyItemValue propertyItem;

  @Getter
  @Setter
  public static class PropertyItemValue {

    private String id;

    private String type;

    private String nextUrl;
  }
}
