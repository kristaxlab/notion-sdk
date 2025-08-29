package io.kristixlab.notion.api.model.databases.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for checkbox columns. Simple boolean property with no additional configuration.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ButtonDatabaseProperty extends DatabaseProperty {

  @JsonProperty("button")
  private Object button;
}
