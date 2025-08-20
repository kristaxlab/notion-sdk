package io.kristixlab.notion.api.model.pages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.FileData;
import io.kristixlab.notion.api.model.common.Icon;
import io.kristixlab.notion.api.model.common.NotionObject;
import io.kristixlab.notion.api.model.pages.properties.PageProperty;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Page extends NotionObject {

  @JsonProperty("properties")
  private Map<String, PageProperty> properties;

  @JsonProperty("url")
  private String url;

  @JsonProperty("public_url")
  private String publicUrl;

  @JsonProperty("icon")
  private Icon icon;

  @JsonProperty("cover")
  private FileData cover;
}

// https://developers.notion.com/reference/page
