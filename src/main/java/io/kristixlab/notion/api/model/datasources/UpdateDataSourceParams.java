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

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private List<RichText> title;
    private Map<String, DataSourcePropertySchemaParams> properties;
    private IconParams icon;
    private Boolean inTrash;

    public Builder title(String title) {
      return title(RichText.builder().fromText(title).buildAsList());
    }

    public Builder title(List<RichText> title) {
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
     * Opens an embedded schema step-builder.
     * Call {@link PropertiesStep#buildProperties()} to return to this builder.
     */
    public PropertiesStep<Builder> propertiesBuilder() {
      return new PropertiesStep<>(this, this::properties);
    }

    /** Sets the property schema via a consumer lambda. */
    public Builder properties(Consumer<DataSourceSchemaBuilder> configurator) {
      DataSourceSchemaBuilder schema = DataSourceSchemaBuilder.builder();
      configurator.accept(schema);
      return properties(schema.build());
    }

    /** Sets the property schema via an external producer. */
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
