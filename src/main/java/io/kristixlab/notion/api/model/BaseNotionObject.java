package io.kristixlab.notion.api.model;

import lombok.Data;

@Data
public class BaseNotionObject {

  private String object; // e.g., "list", "block", "page", "property_item", etc.

  private String requestId;
}
