package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneNumberProperty extends PageProperty {
  private final String type = PropertyType.PHONE_NUMBER.type();

  private String phoneNumber;
}
