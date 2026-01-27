package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.users.User;
import io.kristixlab.notion.api.util.PagePropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreatedByProperty extends PageProperty {
  private final String type = PagePropertyType.CREATED_BY.type();

  @JsonProperty("created_by")
  private User createdBy;

  public static CreatedByProperty of(User createdBy) {
    CreatedByProperty property = new CreatedByProperty();
    property.setCreatedBy(createdBy);
    return property;
  }
}
