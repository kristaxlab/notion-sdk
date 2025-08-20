package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MultiSelectProperty extends PageProperty {
  private final String type = "multi_select";
  @JsonProperty("multi_select")
  private List<SelectProperty.SelectValue> multiSelect;
}
