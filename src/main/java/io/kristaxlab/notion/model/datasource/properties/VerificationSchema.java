package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/**
 * Database property for verification columns. Allows verification of information with approval
 * workflow.
 */
@Getter
@Setter
public class VerificationSchema extends DataSourcePropertySchema {

  public VerificationSchema() {
    setType(PropertyType.VERIFICATION.type());
    verification = new Object();
  }

  private Object verification;
}
