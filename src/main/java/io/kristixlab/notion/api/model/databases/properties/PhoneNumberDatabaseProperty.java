package io.kristixlab.notion.api.model.databases.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** Database property for phone number columns. Stores phone numbers with validation. */
@Data
@EqualsAndHashCode(callSuper = true)
public class PhoneNumberDatabaseProperty extends DatabaseProperty {
  // Phone number properties have no additional configuration
}
