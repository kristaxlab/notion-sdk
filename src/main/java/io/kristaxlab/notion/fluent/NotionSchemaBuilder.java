package io.kristaxlab.notion.fluent;

import io.kristaxlab.notion.model.datasource.properties.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Fluent builder for assembling a {@code property name -> schema} map for create / update data
 * source payloads.
 *
 * <p>This is the schema-side counterpart to {@link NotionPropertiesBuilder}: instead of populating
 * a row's property <i>values</i>, it declares the typed <i>columns</i> of a data source. Every
 * method accepts a {@code nameOrId} string — either the schema column name (e.g. {@code "Status"})
 * or its stable id. Names are easier to write; ids survive renames.
 *
 * <p>Each method delegates to the matching static factory in {@link NotionSchema} and adds the
 * resulting {@link DataSourcePropertySchema} under the supplied key. Call {@link #build()} for the
 * accumulated map, or pass the builder via the {@code properties(Consumer)} hook on the data source
 * request builders.
 *
 * <pre>{@code
 * Map<String, DataSourcePropertySchema> schema = NotionSchema.schemaBuilder()
 *     .title("Name")
 *     .number("Priority", NumberFormatType.NUMBER)
 *     .select("Status", "Todo", "In progress", "Done")
 *     .build();
 * }</pre>
 *
 * <p>When updating an existing data source, mapping a name or id to {@code null} deletes that
 * column; see {@link #remove(String)}.
 */
public class NotionSchemaBuilder {

  private final Map<String, DataSourcePropertySchema> properties = new LinkedHashMap<>();

  protected NotionSchemaBuilder() {}

  /**
   * Builds and returns the accumulated schema.
   *
   * @return a copy of the schema map preserving insertion order
   */
  public Map<String, DataSourcePropertySchema> build() {
    return new LinkedHashMap<>(properties);
  }

  /**
   * Adds a pre-built schema under the given key. Use this escape hatch when shorthand methods don't
   * cover your use case (for example, to rename a column by setting a different {@code name} on the
   * schema).
   *
   * @param nameOrId schema property name or id
   * @param schema schema payload
   * @return this builder
   */
  public NotionSchemaBuilder property(String nameOrId, DataSourcePropertySchema schema) {
    properties.put(nameOrId, schema);
    return this;
  }

  /**
   * Adds all schemas from a pre-built map. Keys are passed through as-is and may be either names or
   * ids.
   *
   * @param values name-or-id to schema mapping
   * @return this builder
   */
  public NotionSchemaBuilder properties(Map<String, ? extends DataSourcePropertySchema> values) {
    properties.putAll(values);
    return this;
  }

  /**
   * Marks a column for deletion in an update payload by mapping its name or id to {@code null}.
   *
   * @param nameOrId schema property name or id to delete
   * @return this builder
   */
  public NotionSchemaBuilder remove(String nameOrId) {
    properties.put(nameOrId, null);
    return this;
  }

  /**
   * Renames a column already present in this builder.
   *
   * <p>Looks up the schema previously added under {@code nameOrId} (its existing name or id), sets
   * its display name to {@code newName}, and re-keys the entry under {@code newName} while
   * preserving insertion order. Its type and configuration are left untouched.
   *
   * <pre>{@code
   * client.dataSources().update(id, ds -> ds
   *     .properties(s -> s
   *         .number("Priority")
   *         .rename("Priority", "Urgency")));
   * }</pre>
   *
   * @param nameOrId existing schema property name or id (must already be present in this builder)
   * @param newName new display name for the property
   * @return this builder
   * @throws IllegalArgumentException if no schema has been added under {@code nameOrId}
   */
  public NotionSchemaBuilder rename(String nameOrId, String newName) {
    DataSourcePropertySchema schema = properties.get(nameOrId);
    if (schema == null) {
      schema = new UnknownDataSourcePropertySchema();
      schema.setName(newName);
      properties.put(nameOrId, schema);
    }
    schema.setName(newName);
    return this;
  }

  // Title

  /**
   * Adds a title column. Every data source must have exactly one title column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder title(String nameOrId) {
    return property(nameOrId, NotionSchema.title());
  }

  // Rich text

  /**
   * Adds a rich-text column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder richText(String nameOrId) {
    return property(nameOrId, NotionSchema.richText());
  }

  // Number

  /**
   * Adds a number column with the default format.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder number(String nameOrId) {
    return property(nameOrId, NotionSchema.number());
  }

  /**
   * Adds a number column with the given format.
   *
   * @param nameOrId schema property name or id
   * @param format number display format
   * @return this builder
   */
  public NotionSchemaBuilder number(String nameOrId, NumberFormatType format) {
    return property(nameOrId, NotionSchema.number(format));
  }

  /**
   * Adds a number column from a raw format token.
   *
   * @param nameOrId schema property name or id
   * @param format number display format token
   * @return this builder
   */
  public NotionSchemaBuilder number(String nameOrId, String format) {
    return property(nameOrId, NotionSchema.number(format));
  }

  // Select

  /**
   * Adds a select column seeded with option names.
   *
   * @param nameOrId schema property name or id
   * @param optionNames option names
   * @return this builder
   */
  public NotionSchemaBuilder select(String nameOrId, String... optionNames) {
    return property(nameOrId, NotionSchema.select(optionNames));
  }

  /**
   * Adds a select column seeded with prepared options.
   *
   * @param nameOrId schema property name or id
   * @param options select options
   * @return this builder
   */
  public NotionSchemaBuilder select(String nameOrId, List<SelectOption> options) {
    return property(nameOrId, NotionSchema.select(options));
  }

  // Multi-select

  /**
   * Adds a multi-select column seeded with option names.
   *
   * @param nameOrId schema property name or id
   * @param optionNames option names
   * @return this builder
   */
  public NotionSchemaBuilder multiSelect(String nameOrId, String... optionNames) {
    return property(nameOrId, NotionSchema.multiSelect(optionNames));
  }

  /**
   * Adds a multi-select column seeded with prepared options.
   *
   * @param nameOrId schema property name or id
   * @param options select options
   * @return this builder
   */
  public NotionSchemaBuilder multiSelect(String nameOrId, List<SelectOption> options) {
    return property(nameOrId, NotionSchema.multiSelect(options));
  }

  // Status

  /**
   * Adds a status column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder status(String nameOrId) {
    return property(nameOrId, NotionSchema.status());
  }

  // Date

  /**
   * Adds a date column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder date(String nameOrId) {
    return property(nameOrId, NotionSchema.date());
  }

  // People

  /**
   * Adds a people column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder people(String nameOrId) {
    return property(nameOrId, NotionSchema.people());
  }

  // Files

  /**
   * Adds a files column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder files(String nameOrId) {
    return property(nameOrId, NotionSchema.files());
  }

  // Checkbox

  /**
   * Adds a checkbox column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder checkbox(String nameOrId) {
    return property(nameOrId, NotionSchema.checkbox());
  }

  // URL, email, phone

  /**
   * Adds a URL column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder url(String nameOrId) {
    return property(nameOrId, NotionSchema.url());
  }

  /**
   * Adds an email column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder email(String nameOrId) {
    return property(nameOrId, NotionSchema.email());
  }

  /**
   * Adds a phone-number column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder phoneNumber(String nameOrId) {
    return property(nameOrId, NotionSchema.phoneNumber());
  }

  // Formula

  /**
   * Adds a formula column.
   *
   * @param nameOrId schema property name or id
   * @param expression the Notion formula expression
   * @return this builder
   */
  public NotionSchemaBuilder formula(String nameOrId, String expression) {
    return property(nameOrId, NotionSchema.formula(expression));
  }

  // Relation

  /**
   * Adds a single-property relation column linking to another data source.
   *
   * @param nameOrId schema property name or id
   * @param dataSourceId the id of the related data source
   * @return this builder
   */
  public NotionSchemaBuilder relation(String nameOrId, String dataSourceId) {
    return property(nameOrId, NotionSchema.relation(dataSourceId));
  }

  /**
   * Adds a dual-property relation column. Notion keeps a synced property on the related data
   * source.
   *
   * @param nameOrId schema property name or id
   * @param dataSourceId the id of the related data source
   * @param syncedPropertyName the synced property created on the related data source
   * @return this builder
   */
  public NotionSchemaBuilder relation(
      String nameOrId, String dataSourceId, String syncedPropertyName) {
    return property(nameOrId, NotionSchema.relation(dataSourceId, syncedPropertyName));
  }

  // Rollup

  /**
   * Adds a rollup column aggregating a property from a related data source.
   *
   * @param nameOrId schema property name or id
   * @param relationPropertyName the relation column to roll up through
   * @param rollupPropertyName the property in the related data source to aggregate
   * @param function the aggregation function
   * @return this builder
   */
  public NotionSchemaBuilder rollup(
      String nameOrId,
      String relationPropertyName,
      String rollupPropertyName,
      RollupFunctionType function) {
    return property(
        nameOrId, NotionSchema.rollup(relationPropertyName, rollupPropertyName, function));
  }

  // Computed / read-only columns

  /**
   * Adds a created-time column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder createdTime(String nameOrId) {
    return property(nameOrId, NotionSchema.createdTime());
  }

  /**
   * Adds a created-by column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder createdBy(String nameOrId) {
    return property(nameOrId, NotionSchema.createdBy());
  }

  /**
   * Adds a last-edited-time column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder lastEditedTime(String nameOrId) {
    return property(nameOrId, NotionSchema.lastEditedTime());
  }

  /**
   * Adds a last-edited-by column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder lastEditedBy(String nameOrId) {
    return property(nameOrId, NotionSchema.lastEditedBy());
  }

  // Button, place, verification

  /**
   * Adds a button column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder button(String nameOrId) {
    return property(nameOrId, NotionSchema.button());
  }

  /**
   * Adds a place (location) column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder place(String nameOrId) {
    return property(nameOrId, NotionSchema.place());
  }

  /**
   * Adds a verification column.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder verification(String nameOrId) {
    return property(nameOrId, NotionSchema.verification());
  }

  // Unique id

  /**
   * Adds a unique-id column with no prefix.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionSchemaBuilder uniqueId(String nameOrId) {
    return property(nameOrId, NotionSchema.uniqueId());
  }

  /**
   * Adds a unique-id column with the given prefix.
   *
   * @param nameOrId schema property name or id
   * @param prefix the prefix prepended to generated ids
   * @return this builder
   */
  public NotionSchemaBuilder uniqueId(String nameOrId, String prefix) {
    return property(nameOrId, NotionSchema.uniqueId(prefix));
  }
}
