package io.kristixlab.notion.api.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneNumberProperty extends PageProperty {
  private final String type = PagePropertyType.PHONE_NUMBER.type();

  private String phoneNumber;

  public static PhoneNumberProperty of(String phoneNumber) {
    PhoneNumberProperty property = new PhoneNumberProperty();
    property.setPhoneNumber(phoneNumber);
    return property;
  }
}
