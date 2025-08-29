package io.kristixlab.notion.api.model.databases.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for last_edited_by columns. Automatically populated with the user who last
 * edited the page.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LastEditedByDatabaseProperty extends DatabaseProperty {
  @JsonProperty("last_edited_by")
  private Object lastEditedBy;
}
