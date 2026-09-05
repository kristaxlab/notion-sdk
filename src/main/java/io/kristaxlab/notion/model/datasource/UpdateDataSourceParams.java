package io.kristaxlab.notion.model.datasource;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.kristaxlab.notion.fluent.NotionSchema;
import io.kristaxlab.notion.fluent.NotionSchemaBuilder;
import io.kristaxlab.notion.fluent.NotionText;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.model.common.richtext.RichText;
import io.kristaxlab.notion.model.datasource.properties.DataSourcePropertySchema;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a Notion data source object. Contains the data source schema, properties, and
 * metadata.
 */
@Getter
@Setter
public class UpdateDataSourceParams {

  private Parent parent;

  private List<RichText> title;

  @JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.ALWAYS)
  private Map<String, DataSourcePropertySchema> properties;

  // only icon, cover update is not supported for a data source, database update should be used
  // instead
  private Icon icon;

  private Boolean inTrash;

  public static UpdateDataSourceParams fromProperty(
      String propertyNameOrId, DataSourcePropertySchema propertySchema) {
    return new Builder().property(propertyNameOrId, propertySchema).build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Parent parent;
    private List<RichText> title;
    private Map<String, DataSourcePropertySchema> properties = new LinkedHashMap<>();
    private Icon icon;
    private Boolean inTrash;

    /**
     * Moves the data source into another database.
     *
     * @param parentDatabaseId destination database identifier
     * @return this builder
     */
    public Builder inDatabase(String parentDatabaseId) {
      return parent(Parent.databaseParent(parentDatabaseId));
    }

    /**
     * Sets a fully constructed {@link Parent} (overrides any prior {@link #inDatabase(String)}
     * call). Notion accepts a database parent to move the data source.
     *
     * @param parent parent descriptor
     * @return this builder
     */
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

    public Builder icon(Icon icon) {
      this.icon = icon;
      return this;
    }

    public Builder icon(String emoji) {
      this.icon = Icon.emoji(emoji);
      return this;
    }

    public Builder inTrash(Boolean inTrash) {
      this.inTrash = inTrash;
      return this;
    }

    /**
     * Sets the property schema via a pre-built map.
     *
     * <p>Prefer {@link #properties(Consumer)} for the concise fluent DSL.
     *
     * @param properties name-or-id to schema mapping
     * @return this builder
     */
    public Builder properties(Map<String, DataSourcePropertySchema> properties) {
      this.properties.putAll(properties);
      return this;
    }

    /**
     * Configures the property schema via a lambda that receives a fresh {@link
     * NotionSchemaBuilder}.
     *
     * <p>Add, modify, or {@link NotionSchemaBuilder#remove(String) remove} columns:
     *
     * <pre>{@code
     * UpdateDataSourceParams.builder()
     *     .properties(s -> s
     *         .richText("Notes")
     *         .select("Priority", "Low", "High")
     *         .remove("Obsolete column"))
     *     .build();
     * }</pre>
     *
     * @param configurator a lambda that chains property methods on the provided schema builder
     * @return this builder
     */
    public Builder properties(Consumer<NotionSchemaBuilder> configurator) {
      NotionSchemaBuilder schema = NotionSchema.schemaBuilder();
      configurator.accept(schema);
      return properties(schema.build());
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
      this.properties.put(name, property);
      return this;
    }

    public UpdateDataSourceParams build() {
      UpdateDataSourceParams params = new UpdateDataSourceParams();
      params.setParent(parent);
      params.setTitle(title);
      params.setProperties(properties);
      params.setIcon(icon);
      params.setInTrash(inTrash);
      return params;
    }
  }
}
