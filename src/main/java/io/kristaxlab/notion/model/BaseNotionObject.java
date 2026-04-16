package io.kristaxlab.notion.model;

import lombok.Data;

/** Base metadata fields present on many Notion API objects. */
@Data
public class BaseNotionObject {

  private String object; // e.g., "list", "block", "page", "property_item", etc.

  private String requestId;
}
