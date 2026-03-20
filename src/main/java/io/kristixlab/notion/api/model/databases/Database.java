package io.kristixlab.notion.api.model.databases;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.*;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Represents a Notion database object. Contains the database schema, properties, and metadata. */
@Data
@EqualsAndHashCode(callSuper = true)
public class Database extends NotionObjectType {

  @JsonProperty("title")
  private List<RichText> title;

  @JsonProperty("description")
  private List<RichText> description;

  @JsonProperty("icon")
  private Icon icon;

  @JsonProperty("cover")
  private Cover cover;

  @JsonProperty("url")
  private String url;

  @JsonProperty("public_url")
  private String publicUrl;

  @JsonProperty("is_inline")
  private Boolean isInline;

  @JsonProperty("is_locked")
  private Boolean isLocked;

  @JsonProperty("data_sources")
  private List<DataSourceRef> dataSources;
}
