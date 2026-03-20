package io.kristixlab.notion.api.model.databases;

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

@Data
public class CreateDatabaseParams {

  @JsonProperty("parent")
  private Parent parent;

  @JsonProperty("title")
  private List<RichText> title;

  @JsonProperty("description")
  private List<RichText> description;

  @JsonProperty("icon")
  private IconParams icon;

  @JsonProperty("cover")
  private CoverParams cover;

  @JsonProperty("is_inline")
  private Boolean isInline;

  @JsonProperty("initial_data_source")
  private InitialDatasource initialDataSource;

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Parent parent;
    private List<RichText> title;
    private List<RichText> description;
    private IconParams icon;
    private CoverParams cover;
    private Boolean isInline;
    private InitialDatasource initialDataSource;

    public Builder inPage(String pageId) {
      return parent(Parent.pageParent(pageId));
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

    public Builder description(String description) {
      return description(RichText.builder().fromText(description).buildAsList());
    }

    public Builder description(List<RichText> description) {
      this.description = description;
      return this;
    }

    public Builder icon(IconParams icon) {
      this.icon = icon;
      return this;
    }

    public Builder cover(CoverParams cover) {
      this.cover = cover;
      return this;
    }

    public Builder isInline(Boolean isInline) {
      this.isInline = isInline;
      return this;
    }

    /**
     * Configures the initial data source properties via a pre-built schema map.
     *
     * <p>Prefer {@link #propertiesBuilder()} or {@link #properties(Consumer)} to avoid the explicit
     * {@code DataSourceSchemaBuilder.builder()} / {@code .build()} wrapping.
     */
    public Builder properties(
        Map<String, DataSourcePropertySchemaParams> initialDataSourceProperties) {
      InitialDatasource initialDataSource = new InitialDatasource();
      initialDataSource.setProperties(initialDataSourceProperties);
      this.initialDataSource = initialDataSource;
      return this;
    }

    /**
     * Opens an embedded schema step-builder for configuring the initial data source properties.
     *
     * <p>Chain property methods on the returned {@link PropertiesStep}, then call {@link
     * PropertiesStep#buildProperties()} to seal the schema and return to this builder.
     *
     * <pre>{@code
     * CreateDatabaseParams.builder()
     *     .parentPage(pageId)
     *     .title("My DB")
     *     .propertiesBuilder()
     *         .title("Name")
     *         .number("Price", NumberFormatType.EURO)
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
     * Configures the initial data source properties via a lambda that receives a fresh {@link
     * DataSourceSchemaBuilder}.
     *
     * <p>This is the most concise option — no explicit {@code builder()} / {@code build()} and no
     * {@code buildProperties()} needed:
     *
     * <pre>{@code
     * CreateDatabaseParams.builder()
     *     .parentPage(pageId)
     *     .title("My DB")
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
     * Configures the initial data source properties using a custom producer.
     *
     * <p>The supplier is called exactly once during this method and its result is forwarded to
     * {@link #properties(Map)}. Use this overload to inject a dedicated schema factory, a
     * Spring/CDI bean, or any other externally managed producer:
     *
     * <pre>{@code
     * CreateDatabaseParams.builder()
     *     .parentPage(pageId)
     *     .title("My DB")
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

    public CreateDatabaseParams build() {
      CreateDatabaseParams params = new CreateDatabaseParams();
      params.setParent(parent);
      params.setTitle(title);
      params.setDescription(description);
      params.setIcon(icon);
      params.setCover(cover);
      params.setIsInline(isInline);
      params.setInitialDataSource(initialDataSource);
      return params;
    }
  }
}
