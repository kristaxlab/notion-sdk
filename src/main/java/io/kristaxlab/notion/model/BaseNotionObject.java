package io.kristaxlab.notion.model;

import lombok.Getter;
import lombok.Setter;

/** Base metadata fields present on many Notion API objects. */
@Getter
@Setter
public class BaseNotionObject {

  private String object; // e.g., "list", "block", "page", "property_item", etc.

  private String requestId;
}
