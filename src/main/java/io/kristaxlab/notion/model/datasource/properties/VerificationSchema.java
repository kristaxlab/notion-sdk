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

  private final String type = PropertyType.VERIFICATION.type();

  private Object verification = new Object();
}
