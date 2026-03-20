package io.kristixlab.notion.api.model.datasources.properties.helper;

import io.kristixlab.notion.api.model.datasources.properties.*;
import io.kristixlab.notion.api.model.datasources.properties.NumberFormatType;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A fluent builder for constructing a Notion data source (database) property schema.
 *
 * <p>Use {@link #builder()} to obtain an instance, chain the desired property methods, and call
 * {@link #build()} to produce the property map that can be passed to the Notion API when creating
 * or updating a database schema.
 *
 * <p>Each property is identified by its name or ID ({@code nameOrId}). When an overload without
 * explicit schema-params is used, a default {@code SchemaParams} instance is created automatically
 * (telescoping-constructor pattern). Every method returns {@code this} to support method chaining.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * Map<String, DataSourcePropertySchemaParams> schema = DataSourceSchemaBuilder.builder()
 *     .title("Name")
 *     .number("Price", NumberFormatType.DOLLAR)
 *     .select("Status", "Active", "Archived")
 *     .relation("Project", projectDatabaseId)
 *     .build();
 * }</pre>
 *
 * @see DataSourcePropertySchemaParams
 */
public class DataSourceSchemaBuilder {

  private Map<String, DataSourcePropertySchemaParams> properties = new LinkedHashMap<>();

  /**
   * Creates and returns a new {@code DataSourceSchemaBuilder} instance.
   *
   * @return a fresh builder
   */
  public static DataSourceSchemaBuilder builder() {
    return new DataSourceSchemaBuilder();
  }

  /**
   * Builds and returns the property schema map.
   *
   * <p>The returned map preserves insertion order (backed by a {@link LinkedHashMap}) and maps each
   * property name/ID to its corresponding schema-params object. A {@code null} value for a given
   * key signals to the Notion API that the property should be deleted.
   *
   * @return an unmodifiable-friendly map of property names/IDs to their schema params
   */
  public Map<String, DataSourcePropertySchemaParams> build() {
    return properties;
  }

  // -------------------------------------------------------------------------
  // Property definitions
  // -------------------------------------------------------------------------

  /**
   * Adds a {@code checkbox} property with default parameters.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder checkbox(String nameOrId) {
    return checkbox(nameOrId, new CheckboxSchemaParams());
  }

  /**
   * Adds a {@code checkbox} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the checkbox schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder checkbox(String nameOrId, CheckboxSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code created_by} property with default parameters.
   *
   * <p>This is a read-only, auto-populated Notion property that stores the user who created the
   * page.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder createdBy(String nameOrId) {
    return createdBy(nameOrId, new CreatedBySchemaParams());
  }

  /**
   * Adds a {@code created_by} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the created-by schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder createdBy(String nameOrId, CreatedBySchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code created_time} property with default parameters.
   *
   * <p>This is a read-only, auto-populated Notion property that stores the timestamp when the page
   * was created.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder createdTime(String nameOrId) {
    return createdTime(nameOrId, new CreatedTimeSchemaParams());
  }

  /**
   * Adds a {@code created_time} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the created-time schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder createdTime(String nameOrId, CreatedTimeSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code date} property with default parameters.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder date(String nameOrId) {
    return date(nameOrId, new DateSchemaParams());
  }

  /**
   * Adds a {@code date} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the date schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder date(String nameOrId, DateSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds an {@code email} property with default parameters.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder email(String nameOrId) {
    return email(nameOrId, new EmailSchemaParams());
  }

  /**
   * Adds an {@code email} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the email schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder email(String nameOrId, EmailSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code files} property with default parameters.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder files(String nameOrId) {
    return files(nameOrId, new FilesSchemaParams());
  }

  /**
   * Adds a {@code files} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the files schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder files(String nameOrId, FilesSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code formula} property with an empty expression.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder formula(String nameOrId) {
    return formula(nameOrId, new FormulaSchemaParams());
  }

  /**
   * Adds a {@code formula} property with the given Notion formula expression string.
   *
   * <p>Example expression: {@code "\"Contact email: \" + prop(\"Contact Email\")"}
   *
   * @param nameOrId the property name or ID
   * @param expression the Notion formula expression
   * @return this builder
   */
  public DataSourceSchemaBuilder formula(String nameOrId, String expression) {
    FormulaSchemaParams params = new FormulaSchemaParams();
    params.getFormula().setExpression(expression);
    return formula(nameOrId, params);
  }

  /**
   * Adds a {@code formula} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the formula schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder formula(String nameOrId, FormulaSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code last_edited_by} property with default parameters.
   *
   * <p>This is a read-only, auto-populated Notion property that stores the user who last edited the
   * page.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder lastEditedBy(String nameOrId) {
    return lastEditedBy(nameOrId, new LastEditedBySchemaParams());
  }

  /**
   * Adds a {@code last_edited_by} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the last-edited-by schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder lastEditedBy(String nameOrId, LastEditedBySchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code last_edited_time} property with default parameters.
   *
   * <p>This is a read-only, auto-populated Notion property that stores the timestamp of the most
   * recent edit to the page.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder lastEditedTime(String nameOrId) {
    return lastEditedTime(nameOrId, new LastEditedTimeSchemaParams());
  }

  /**
   * Adds a {@code last_edited_time} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the last-edited-time schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder lastEditedTime(
      String nameOrId, LastEditedTimeSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code multi_select} property with no predefined options.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder multiSelect(String nameOrId) {
    return multiSelect(nameOrId, new MultiSelectSchemaParams());
  }

  /**
   * Adds a {@code multi_select} property with the given predefined option names.
   *
   * @param nameOrId the property name or ID
   * @param options one or more option names
   * @return this builder
   */
  public DataSourceSchemaBuilder multiSelect(String nameOrId, String... options) {
    return multiSelect(nameOrId, MultiSelectSchemaParams.of(options));
  }

  /**
   * Adds a {@code multi_select} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the multi-select schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder multiSelect(String nameOrId, MultiSelectSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code number} property with the default format ({@code number}).
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder number(String nameOrId) {
    return number(nameOrId, new NumberSchemaParams());
  }

  /**
   * Adds a {@code number} property with the given format string (e.g. {@code "dollar"}).
   *
   * @param nameOrId the property name or ID
   * @param format the number format string accepted by the Notion API
   * @return this builder
   */
  public DataSourceSchemaBuilder number(String nameOrId, String format) {
    return number(nameOrId, NumberSchemaParams.of(format));
  }

  /**
   * Adds a {@code number} property with the given {@link NumberFormatType}.
   *
   * @param nameOrId the property name or ID
   * @param format the strongly-typed number format
   * @return this builder
   */
  public DataSourceSchemaBuilder number(String nameOrId, NumberFormatType format) {
    return number(nameOrId, NumberSchemaParams.of(format));
  }

  /**
   * Adds a {@code number} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the number schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder number(String nameOrId, NumberSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code people} property with default parameters.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder people(String nameOrId) {
    return people(nameOrId, new PeopleSchemaParams());
  }

  /**
   * Adds a {@code people} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the people schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder people(String nameOrId, PeopleSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code phone_number} property with default parameters.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder phone(String nameOrId) {
    return phone(nameOrId, new PhoneSchemaParams());
  }

  /**
   * Adds a {@code phone_number} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the phone schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder phone(String nameOrId, PhoneSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code place} (location) property with default parameters.
   *
   * <p>A place property stores geographic data including latitude, longitude, a human-readable
   * name, and an address.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder place(String nameOrId) {
    return place(nameOrId, new PlaceSchemaParams());
  }

  /**
   * Adds a {@code place} (location) property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the place schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder place(String nameOrId, PlaceSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a single-way {@code relation} property pointing to the specified data source.
   *
   * @param nameOrId the property name or ID
   * @param dataSourceId the UUID of the related Notion database
   * @return this builder
   */
  public DataSourceSchemaBuilder relation(String nameOrId, String dataSourceId) {
    RelationSchemaParams params = RelationSchemaParams.builder().dataSourceId(dataSourceId).build();
    return relation(nameOrId, params);
  }

  /**
   * Adds a dual-property (two-way) {@code relation} property.
   *
   * <p>Providing {@code syncedPropName} implicitly sets the relation type to {@code dual_property},
   * creating a mirrored relation on the target database.
   *
   * @param nameOrId the property name or ID
   * @param dataSourceId the UUID of the related Notion database
   * @param syncedPropName the name of the synced (mirrored) property on the target database
   * @return this builder
   */
  public DataSourceSchemaBuilder relation(
      String nameOrId, String dataSourceId, String syncedPropName) {
    RelationSchemaParams params =
        RelationSchemaParams.builder()
            .dataSourceId(dataSourceId)
            .syncedProperty(syncedPropName) // implicitly sets type to dual_property
            .build();
    return relation(nameOrId, params);
  }

  /**
   * Adds a {@code relation} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the relation schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder relation(String nameOrId, RelationSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code rollup} property that aggregates values from a related database.
   *
   * @param nameOrId the property name or ID for this rollup
   * @param relationNameOrId the name or ID of the relation property to roll up through
   * @param rollupNameOrId the name or ID of the property in the related database to aggregate
   * @param rollupFunction the aggregation function (e.g. {@code "sum"}, {@code "count"}, {@code
   *     "average"})
   * @return this builder
   */
  public DataSourceSchemaBuilder rollup(
      String nameOrId, String relationNameOrId, String rollupNameOrId, String rollupFunction) {
    RollupSchemaParams params =
        RollupSchemaParams.builder()
            .relation(relationNameOrId)
            .property(rollupNameOrId)
            .calculate(rollupFunction)
            .build();
    return rollup(nameOrId, params);
  }

  /**
   * Adds a {@code rollup} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the rollup schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder rollup(String nameOrId, RollupSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code select} property with no predefined options.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder select(String nameOrId) {
    return select(nameOrId, new SelectSchemaParams());
  }

  /**
   * Adds a {@code select} property with the given predefined option names.
   *
   * @param nameOrId the property name or ID
   * @param options one or more option names
   * @return this builder
   */
  public DataSourceSchemaBuilder select(String nameOrId, String... options) {
    return select(nameOrId, SelectSchemaParams.of(options));
  }

  /**
   * Adds a {@code select} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the select schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder select(String nameOrId, SelectSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code status} property with default parameters.
   *
   * <p>Notion auto-populates the status groups and options; the returned schema will use the
   * default configuration unless custom params are supplied.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder status(String nameOrId) {
    return status(nameOrId, new StatusSchemaParams());
  }

  /**
   * Adds a {@code status} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the status schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder status(String nameOrId, StatusSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code rich_text} property with default parameters.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder text(String nameOrId) {
    return text(nameOrId, new RichTextSchemaParams());
  }

  /**
   * Adds a {@code rich_text} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the rich-text schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder text(String nameOrId, RichTextSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code title} property with default parameters.
   *
   * <p>Every Notion database must have exactly one title property. Attempting to add more than one
   * will result in a Notion API error.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder title(String nameOrId) {
    return title(nameOrId, new TitleSchemaParams());
  }

  /**
   * Adds a {@code title} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the title schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder title(String nameOrId, TitleSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code unique_id} property with default parameters.
   *
   * <p>A unique-ID property auto-increments and assigns each page a sequential numeric identifier.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder uniqueId(String nameOrId) {
    return uniqueId(nameOrId, new UniqueIdSchemaParams());
  }

  /**
   * Adds a {@code unique_id} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the unique-ID schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder uniqueId(String nameOrId, UniqueIdSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  /**
   * Adds a {@code url} property with default parameters.
   *
   * @param nameOrId the property name or ID
   * @return this builder
   */
  public DataSourceSchemaBuilder url(String nameOrId) {
    return url(nameOrId, new UrlSchemaParams());
  }

  /**
   * Adds a {@code url} property with the given parameters.
   *
   * @param nameOrId the property name or ID
   * @param params the URL schema params
   * @return this builder
   */
  public DataSourceSchemaBuilder url(String nameOrId, UrlSchemaParams params) {
    properties.put(nameOrId, params);
    return this;
  }

  // -------------------------------------------------------------------------
  // Modify operations
  // -------------------------------------------------------------------------

  /**
   * Renames an existing property.
   *
   * <p>If the property does not yet exist in the schema being built, a stub entry is created so
   * that the rename instruction is still sent to the Notion API (useful when updating an existing
   * database without re-specifying all other properties).
   *
   * @param nameOrId the current property name or ID
   * @param newName the desired new display name
   * @return this builder
   */
  public DataSourceSchemaBuilder rename(String nameOrId, String newName) {
    properties
        .computeIfAbsent(nameOrId, (k) -> new DataSourcePropertySchemaParams())
        .setName(newName);
    return this;
  }

  /**
   * Marks an existing property for deletion.
   *
   * <p>A {@code null} value in the schema map instructs the Notion API to remove the corresponding
   * property from the database. This is a destructive, irreversible operation; any data stored in
   * the property will be permanently lost.
   *
   * @param nameOrId the property name or ID to delete
   * @return this builder
   */
  public DataSourceSchemaBuilder delete(String nameOrId) {
    properties.put(nameOrId, null);
    return this;
  }
}
