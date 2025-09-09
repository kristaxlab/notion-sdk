package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for created_by columns. Automatically populated with the user who created the
 * page.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreatedByDatasourceProperty extends DatasourceProperty {

  @JsonProperty("created_by")
  private Object createdBy = new Object();
}
