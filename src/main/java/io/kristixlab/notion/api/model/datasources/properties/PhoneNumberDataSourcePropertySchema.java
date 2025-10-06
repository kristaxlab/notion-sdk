package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Database property for phone number columns. Stores phone numbers with validation. */
@Data
@EqualsAndHashCode(callSuper = true)
public class PhoneNumberDataSourcePropertySchema extends DataSourcePropertySchema {
  @JsonProperty("phone_number")
  private Object phoneNumber = new Object();
}
