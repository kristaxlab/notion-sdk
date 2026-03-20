package io.kristixlab.notion.api.model.datasources;

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

    public Builder parentDatabase(String parendDatabaseId) {
      return parent(Parent.databaseParent(parendDatabaseId));
    }

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

    /**
     * Sets the data source property schema via a pre-built map.
     *
     * <p>Prefer {@link #propertiesBuilder()} or {@link #properties(Consumer)} to avoid the explicit
     * {@code DataSourceSchemaBuilder.builder()} / {@code .build()} wrapping.
     */
    public Builder properties(Map<String, DataSourcePropertySchemaParams> properties) {
      this.properties = properties;
      return this;
    }

    /**
     * Opens an embedded schema step-builder for configuring the data source properties.
     *
     * <p>Chain property methods on the returned {@link PropertiesStep}, then call {@link
     * PropertiesStep#buildProperties()} to seal the schema and return to this builder.
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
     * CreateDataSourceParams.builder()
     *     .parentDatabase(dbId)
     *     .title("Catalog")
     *     .properties(s -> s
     *         .title("Name")
     *         .number("Price", NumberFormatType.EURO))
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
     * CreateDataSourceParams.builder()
     *     .parentDatabase(dbId)
     *     .title("Catalog")
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
