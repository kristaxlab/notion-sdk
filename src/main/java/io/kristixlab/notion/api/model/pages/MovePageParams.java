package io.kristixlab.notion.api.model.pages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.Parent;
import lombok.Data;

@Data
public class MovePageParams {

  @JsonProperty("parent")
  private Parent parent;

}
