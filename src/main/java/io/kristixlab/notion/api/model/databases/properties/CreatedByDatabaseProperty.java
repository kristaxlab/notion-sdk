package io.kristixlab.notion.api.model.databases.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for created_by columns. Automatically populated with the user who created the
 * page.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreatedByDatabaseProperty extends DatabaseProperty {

  @JsonProperty("created_by")
  private Object createdBy;
}
