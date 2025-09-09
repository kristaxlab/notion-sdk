package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for last_edited_by columns. Automatically populated with the user who last
 * edited the page.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LastEditedByDatasourceProperty extends DatasourceProperty {
  @JsonProperty("last_edited_by")
  private Object lastEditedBy = new Object();
}
