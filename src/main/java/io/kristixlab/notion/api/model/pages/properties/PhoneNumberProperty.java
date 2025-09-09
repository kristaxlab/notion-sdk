package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PhoneNumberProperty extends PageProperty {
  private final String type = "phone_number";

  @JsonProperty("phone_number")
  private String phoneNumber;
}
