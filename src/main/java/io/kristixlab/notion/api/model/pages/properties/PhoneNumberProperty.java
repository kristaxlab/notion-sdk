package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.util.PagePropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PhoneNumberProperty extends PageProperty {
  private final String type = PagePropertyType.PHONE_NUMBER.type();

  @JsonProperty("phone_number")
  private String phoneNumber;

  public static PhoneNumberProperty of(String phoneNumber) {
    PhoneNumberProperty property = new PhoneNumberProperty();
    property.setPhoneNumber(phoneNumber);
    return property;
  }
}
