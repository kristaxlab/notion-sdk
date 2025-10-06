package io.kristixlab.notion.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BaseNotionObject {

  @JsonProperty("object")
  private String object; // e.g., "list", "block", "page", "property_item", etc.

  @JsonProperty("request_id")
  private String requestId;
}
