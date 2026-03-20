package io.kristixlab.notion.api.model.datasources.properties.helper;

import io.kristixlab.notion.api.model.datasources.properties.*;

/**
 * A fluent builder for constructing a Notion data source (database) property schema.
 *
 * <p>Use {@link #builder()} to obtain an instance, chain the desired property methods, and call
 * {@link #build()} to produce the property map for the Notion API.
 *
 * <p>All property methods are defined in {@link AbstractDataSourceSchemaBuilder}; this class is the
 * concrete, standalone entry point. For embedding inside a request builder (avoiding the explicit
 * {@code DataSourceSchemaBuilder.builder()} / {@code .build()} wrapping) use the {@code
 * propertiesBuilder()} method provided by {@code CreateDatabaseParams.Builder} or {@code
 * CreateDataSourceParams.Builder}.
 *
 * <p>Example — standalone:
 *
 * <pre>{@code
 * Map<String, DataSourcePropertySchemaParams> schema = DataSourceSchemaBuilder.builder()
 *     .title("Name")
 *     .number("Price", NumberFormatType.DOLLAR)
 *     .select("Status", "Active", "Archived")
 *     .build();
 * }</pre>
 *
 * <p>Example — embedded (step-builder):
 *
 * <pre>{@code
 * CreateDatabaseParams params = CreateDatabaseParams.builder()
 *     .parentPage(pageId)
 *     .title("My DB")
 *     .propertiesBuilder()
 *         .title("Name")
 *         .number("Price", NumberFormatType.DOLLAR)
 *         .select("Status", "Active", "Archived")
 *     .buildProperties()
 *     .build();
 * }</pre>
 *
 * <p>Example — consumer (lambda):
 *
 * <pre>{@code
 * CreateDatabaseParams params = CreateDatabaseParams.builder()
 *     .parentPage(pageId)
 *     .title("My DB")
 *     .properties(s -> s
 *         .title("Name")
 *         .number("Price", NumberFormatType.DOLLAR)
 *         .select("Status", "Active", "Archived"))
 *     .build();
 * }</pre>
 *
 * @see AbstractDataSourceSchemaBuilder
 * @see PropertiesStep
 */
public final class DataSourceSchemaBuilder
    extends AbstractDataSourceSchemaBuilder<DataSourceSchemaBuilder> {

  private DataSourceSchemaBuilder() {}

  /**
   * Creates and returns a new {@code DataSourceSchemaBuilder} instance.
   *
   * @return a fresh builder
   */
  public static DataSourceSchemaBuilder builder() {
    return new DataSourceSchemaBuilder();
  }
}
