package io.kristixlab.notion.api.model.datasources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.*;
import io.kristixlab.notion.api.model.datasources.properties.DataSourcePropertySchemaParams;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * Represents a Notion data source object. Contains the data source schema, properties, and
 * metadata.
 */
@Data
public class CreateDataSourceParams {

  @JsonProperty("parent")
  private Parent parent;

  @JsonProperty("title")
  private List<RichText> title;

  @JsonProperty("properties")
  private Map<String, DataSourcePropertySchemaParams> properties;

  @JsonProperty("icon")
  private IconParams icon;

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Parent parent;
    private List<RichText> title;
    private Map<String, DataSourcePropertySchemaParams> properties;
    private IconParams icon;

    public Builder parent(Parent parent) {
      this.parent = parent;
      return this;
    }

    public Builder title(String title) {
      return title(RichText.builder().fromText(title).buildAsList());
    }

    public Builder title(List<RichText> title) {
      this.title = title;
      return this;
    }

    public Builder properties(Map<String, DataSourcePropertySchemaParams> properties) {
      this.properties = properties;
      return this;
    }

    public Builder icon(IconParams icon) {
      this.icon = icon;
      return this;
    }

    public CreateDataSourceParams build() {
      CreateDataSourceParams params = new CreateDataSourceParams();
      params.setParent(parent);
      params.setTitle(title);
      params.setProperties(properties);
      params.setIcon(icon);
      return params;
    }
  }
}
