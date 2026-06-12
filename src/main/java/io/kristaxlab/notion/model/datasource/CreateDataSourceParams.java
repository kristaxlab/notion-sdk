package io.kristaxlab.notion.model.datasource;

import io.kristaxlab.notion.fluent.NotionText;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.model.common.richtext.RichText;
import io.kristaxlab.notion.model.datasource.properties.DataSourcePropertySchema;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Data;

/**
 * Represents a Notion data source object. Contains the data source schema, properties, and
 * metadata.
 */
@Data
public class CreateDataSourceParams {

  private Parent parent;

  private List<RichText> title;

  private Map<String, DataSourcePropertySchema> properties;

  private Icon icon;

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Parent parent;
    private List<RichText> title;
    private Map<String, DataSourcePropertySchema> properties;
    private Icon icon;

    public Builder inDatabase(String parendDatabaseId) {
      return parent(Parent.databaseParent(parendDatabaseId));
    }

    public Builder parent(Parent parent) {
      this.parent = parent;
      return this;
    }

    public Builder title(String title) {
      return title(NotionText.plainText(title).asList());
    }

    public Builder title(List<RichText> title) {
      this.title = title;
      return this;
    }

    /**
     * Sets the data source property schema via a pre-built map.
     *
     * <p>Prefer {@link #propertiesBuilder()} or {@link #properties(Consumer)} to avoid the explicit
     * {@code DataSourceSchemaBuilder.builder()} / {@code .build()} wrapping. /** Sets the property
     * schema via a pre-built map.
     */
    public Builder properties(Map<String, DataSourcePropertySchema> properties) {
      this.properties = properties;
      return this;
    }

    /**
     * Sets a single property schema entry. Use as an escape hatch when only one property needs to
     * be added or updated without configuring the full schema map.
     *
     * @param name the property name or ID
     * @param property the schema params, or {@code null} to delete the property
     * @return this builder
     */
    public Builder property(String name, DataSourcePropertySchema property) {
      if (this.properties == null) {
        this.properties = new java.util.LinkedHashMap<>();
      }
      this.properties.put(name, property);
      return this;
    }

    public Builder icon(Icon icon) {
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
