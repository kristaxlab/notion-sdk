package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import lombok.Getter;
import lombok.Setter;

/** Database property for phone number columns. Stores phone numbers with validation. */
@Getter
@Setter
public class PhoneSchema extends DataSourcePropertySchema {

  public PhoneSchema() {
    setType(PropertyType.PHONE_NUMBER.type());
    phoneNumber = new Object();
  }

  private Object phoneNumber;
}
