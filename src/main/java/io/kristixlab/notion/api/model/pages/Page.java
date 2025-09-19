package io.kristixlab.notion.api.model.pages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.FileData;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.NotionObject;
import io.kristixlab.notion.api.model.pages.properties.PageProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Page extends NotionObject {

  @JsonProperty("properties")
  private Map<String, PageProperty> properties = new HashMap<>();

  @JsonProperty("url")
  private String url;

  @JsonProperty("public_url")
  private String publicUrl;

  @JsonProperty("icon")
  private Icon icon;

  @JsonProperty("cover")
  private FileData cover;

}
