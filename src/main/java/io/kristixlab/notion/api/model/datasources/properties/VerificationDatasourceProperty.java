package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Database property for verification columns. Allows verification of information with approval
 * workflow.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VerificationDatasourceProperty extends DatasourceProperty {

  @JsonProperty("verification")
  private Object verification = new Object();

}
