package io.kristixlab.notion.api.model.datasources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.*;
import io.kristixlab.notion.api.model.datasources.properties.DataSourcePropertySchemaParams;
import io.kristixlab.notion.api.model.datasources.properties.helper.DataSourceSchemaBuilder;
import io.kristixlab.notion.api.model.datasources.properties.helper.PropertiesStep;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Data;

/**
 * Represents a Notion data source object. Contains the data source schema, properties, and
 * metadata.
 */
@Data
public class UpdateDataSourceParams {

  @JsonProperty("parent")
  private Parent parent;

  @JsonProperty("title")
  private List<RichText> title;

  @JsonProperty("properties")
  @JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.ALWAYS)
  private Map<String, DataSourcePropertySchemaParams> properties;

  @JsonProperty("icon")
  private IconParams icon;

  @JsonProperty("in_trash")
  private Boolean inTrash;

  public static UpdateDataSourceParams fromProperty(
      String propertyNameOrId, DataSourcePropertySchemaParams propertySchema) {
    return new Builder().property(propertyNameOrId, propertySchema).build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private List<RichText> title;
    private Map<String, DataSourcePropertySchemaParams> properties;
    private IconParams icon;
    private Boolean inTrash;

    public Builder dataSourceTitle(String title) {
      return dataSourceTitle(RichText.builder().fromText(title).buildAsList());
    }

    public Builder dataSourceTitle(List<RichText> title) {
      this.title = title;
      return this;
    }

    public Builder icon(IconParams icon) {
      this.icon = icon;
      return this;
    }

    public Builder inTrash(Boolean inTrash) {
      this.inTrash = inTrash;
      return this;
    }

    /** Sets the property schema via a pre-built map. */
    public Builder properties(Map<String, DataSourcePropertySchemaParams> properties) {
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
    public Builder property(String name, DataSourcePropertySchemaParams property) {
      if (this.properties == null) {
        this.properties = new java.util.LinkedHashMap<>();
      }
      this.properties.put(name, property);
      return this;
    }

    /**
     * Opens an embedded schema step-builder for configuring the data source properties.
     *
     * <p>Chain property methods on the returned {@link PropertiesStep}, then call {@link
     * PropertiesStep#buildProperties()} to seal the schema and return to this builder.
     *
     * <pre>{@code
     * UpdateDataSourceParams.builder()
     *     .propertiesBuilder()
     *         .status("Workflow", StatusSchemaParams.builder()
     *             .option("Backlog",      Color.DEFAULT)
     *             .option("In Progress",  Color.YELLOW)
     *             .option("Completed",    Color.GREEN)
     *             .build())
     *         .rename("Name", "Title")
     *         .delete("Legacy Field")
     *     .buildProperties()
     *     .build();
     * }</pre>
     *
     * @return a {@link PropertiesStep} whose {@code buildProperties()} returns this builder
     */
    public PropertiesStep<Builder> propertiesBuilder() {
      return new PropertiesStep<>(this, this::properties);
    }

    /**
     * Sets the data source property schema via a lambda that receives a fresh {@link
     * DataSourceSchemaBuilder}.
     *
     * <pre>{@code
     * UpdateDataSourceParams.builder()
     *     .properties(s -> s
     *         .status("Workflow", StatusSchemaParams.builder()
     *             .option("Backlog",     Color.DEFAULT)
     *             .option("In Progress", Color.YELLOW)
     *             .build())
     *         .rename("Name", "Title"))
     *     .build();
     * }</pre>
     *
     * @param configurator a lambda that chains property methods on the provided schema builder
     * @return this builder
     */
    public Builder properties(Consumer<DataSourceSchemaBuilder> configurator) {
      DataSourceSchemaBuilder schema = DataSourceSchemaBuilder.builder();
      configurator.accept(schema);
      return properties(schema.build());
    }

    /**
     * Sets the data source property schema using a custom producer.
     *
     * <p>The supplier is called exactly once and its result is forwarded to {@link
     * #properties(Map)}. Use this overload to inject a dedicated schema factory or any other
     * externally managed producer:
     *
     * <pre>{@code
     * UpdateDataSourceParams.builder()
     *     .properties(catalogSchemaFactory::defaultSchema)
     *     .build();
     * }</pre>
     *
     * @param producer a supplier that returns the property schema map
     * @return this builder
     */
    public Builder properties(Supplier<Map<String, DataSourcePropertySchemaParams>> producer) {
      return properties(producer.get());
    }

    public UpdateDataSourceParams build() {
      UpdateDataSourceParams params = new UpdateDataSourceParams();
      params.setTitle(title);
      params.setProperties(properties);
      params.setIcon(icon);
      params.setInTrash(inTrash);
      return params;
    }
  }
}
