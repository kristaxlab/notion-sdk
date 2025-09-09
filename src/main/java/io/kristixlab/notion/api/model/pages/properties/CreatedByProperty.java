package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.users.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreatedByProperty extends PageProperty {
  private final String type = "created_by";

  @JsonProperty("created_by")
  private User createdBy;
}
