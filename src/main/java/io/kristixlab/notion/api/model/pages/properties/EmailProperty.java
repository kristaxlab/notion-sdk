package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.util.PagePropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmailProperty extends PageProperty {
  private final String type = PagePropertyType.EMAIL.type();

  @JsonProperty("email")
  private String email;

  public static EmailProperty of(String email) {
    EmailProperty property = new EmailProperty();
    property.setEmail(email);
    return property;
  }
}
