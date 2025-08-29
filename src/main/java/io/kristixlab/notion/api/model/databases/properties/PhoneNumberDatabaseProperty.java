package io.kristixlab.notion.api.model.databases.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Database property for phone number columns. Stores phone numbers with validation. */
@Data
@EqualsAndHashCode(callSuper = true)
public class PhoneNumberDatabaseProperty extends DatabaseProperty {
  @JsonProperty("phone_number")
  private Object phoneNumber;
}
