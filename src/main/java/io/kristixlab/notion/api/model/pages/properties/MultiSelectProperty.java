package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MultiSelectProperty extends PageProperty {
  private final String type = "multi_select";

  @JsonProperty("multi_select")
  private List<SelectProperty.SelectValue> multiSelect;
}
