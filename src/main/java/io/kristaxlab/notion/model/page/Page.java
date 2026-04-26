package io.kristaxlab.notion.model.page;

import io.kristaxlab.notion.model.common.Cover;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.common.NotionObject;
import io.kristaxlab.notion.model.page.property.PageProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/** Notion page object: properties map, URLs, icon, cover, and lock state. */
@Getter
@Setter
public class Page extends NotionObject {

  private Map<String, PageProperty> properties = new HashMap<>();

  private String url;

  private String publicUrl;

  private Icon icon;

  private Cover cover;

  private Boolean isLocked;
}
