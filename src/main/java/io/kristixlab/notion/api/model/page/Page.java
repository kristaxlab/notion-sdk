package io.kristixlab.notion.api.model.page;

import io.kristixlab.notion.api.model.common.Cover;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.NotionObject;
import io.kristixlab.notion.api.model.page.property.PageProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

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
