package io.kristixlab.notion.api.model.datasources.properties.helper;

import io.kristixlab.notion.api.model.datasources.properties.*;
import io.kristixlab.notion.api.model.datasources.properties.NumberFormatType;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Abstract fluent base for building a Notion data source (database) property schema.
 *
 * <p>Uses the <em>curiously recurring template pattern</em> (CRTP) so that every property-defining
 * method returns the concrete subclass type, keeping the chain unbroken regardless of which
 * concrete builder is in use.
 *
 * <p>This class is intentionally package-private; clients interact only with its concrete
 * descendants.
 *
 * @param <SELF> the concrete subclass type (enables covariant method chaining)
 */
abstract class AbstractDataSourceSchemaBuilder<SELF extends AbstractDataSourceSchemaBuilder<SELF>> {

  protected final Map<String, DataSourcePropertySchemaParams> properties = new LinkedHashMap<>();

  @SuppressWarnings("unchecked")
  private SELF self() {
    return (SELF) this;
  }

  /**
   * Builds and returns the property schema map.
   *
   * <p>The returned map preserves insertion order and maps each property name/ID to its schema
   * params. A {@code null} value signals the Notion API to delete the property.
   *
   * @return the accumulated property schema map
   */
  public Map<String, DataSourcePropertySchemaParams> build() {
    return properties;
  }

  // ── Property definitions ────────────────────────────────────────────────

  /** Adds a {@code checkbox} property with default parameters. */
  public SELF checkbox(String nameOrId) {
    return checkbox(nameOrId, new CheckboxSchemaParams());
  }

  /** Adds a {@code checkbox} property with the given parameters. */
  public SELF checkbox(String nameOrId, CheckboxSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /**
   * Adds a {@code created_by} property with default parameters.
   * Read-only; auto-populated by Notion.
   */
  public SELF createdBy(String nameOrId) {
    return createdBy(nameOrId, new CreatedBySchemaParams());
  }

  /** Adds a {@code created_by} property with the given parameters. */
  public SELF createdBy(String nameOrId, CreatedBySchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /**
   * Adds a {@code created_time} property with default parameters.
   * Read-only; auto-populated by Notion.
   */
  public SELF createdTime(String nameOrId) {
    return createdTime(nameOrId, new CreatedTimeSchemaParams());
  }

  /** Adds a {@code created_time} property with the given parameters. */
  public SELF createdTime(String nameOrId, CreatedTimeSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /** Adds a {@code date} property with default parameters. */
  public SELF date(String nameOrId) {
    return date(nameOrId, new DateSchemaParams());
  }

  /** Adds a {@code date} property with the given parameters. */
  public SELF date(String nameOrId, DateSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /** Adds an {@code email} property with default parameters. */
  public SELF email(String nameOrId) {
    return email(nameOrId, new EmailSchemaParams());
  }

  /** Adds an {@code email} property with the given parameters. */
  public SELF email(String nameOrId, EmailSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /** Adds a {@code files} property with default parameters. */
  public SELF files(String nameOrId) {
    return files(nameOrId, new FilesSchemaParams());
  }

  /** Adds a {@code files} property with the given parameters. */
  public SELF files(String nameOrId, FilesSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /** Adds a {@code formula} property with an empty expression. */
  public SELF formula(String nameOrId) {
    return formula(nameOrId, new FormulaSchemaParams());
  }

  /**
   * Adds a {@code formula} property with the given Notion formula expression.
   *
   * @param expression a Notion formula string, e.g. {@code "\"Hello: \" + prop(\"Name\")"}
   */
  public SELF formula(String nameOrId, String expression) {
    FormulaSchemaParams params = new FormulaSchemaParams();
    params.getFormula().setExpression(expression);
    return formula(nameOrId, params);
  }

  /** Adds a {@code formula} property with the given parameters. */
  public SELF formula(String nameOrId, FormulaSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /**
   * Adds a {@code last_edited_by} property with default parameters.
   * Read-only; auto-populated by Notion.
   */
  public SELF lastEditedBy(String nameOrId) {
    return lastEditedBy(nameOrId, new LastEditedBySchemaParams());
  }

  /** Adds a {@code last_edited_by} property with the given parameters. */
  public SELF lastEditedBy(String nameOrId, LastEditedBySchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /**
   * Adds a {@code last_edited_time} property with default parameters.
   * Read-only; auto-populated by Notion.
   */
  public SELF lastEditedTime(String nameOrId) {
    return lastEditedTime(nameOrId, new LastEditedTimeSchemaParams());
  }

  /** Adds a {@code last_edited_time} property with the given parameters. */
  public SELF lastEditedTime(String nameOrId, LastEditedTimeSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /** Adds a {@code multi_select} property with no predefined options. */
  public SELF multiSelect(String nameOrId) {
    return multiSelect(nameOrId, new MultiSelectSchemaParams());
  }

  /** Adds a {@code multi_select} property with the given predefined option names. */
  public SELF multiSelect(String nameOrId, String... options) {
    return multiSelect(nameOrId, MultiSelectSchemaParams.of(options));
  }

  /** Adds a {@code multi_select} property with the given parameters. */
  public SELF multiSelect(String nameOrId, MultiSelectSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /** Adds a {@code number} property with the default format. */
  public SELF number(String nameOrId) {
    return number(nameOrId, new NumberSchemaParams());
  }

  /** Adds a {@code number} property with the given format string (e.g. {@code "dollar"}). */
  public SELF number(String nameOrId, String format) {
    return number(nameOrId, NumberSchemaParams.of(format));
  }

  /** Adds a {@code number} property with the given {@link NumberFormatType}. */
  public SELF number(String nameOrId, NumberFormatType format) {
    return number(nameOrId, NumberSchemaParams.of(format));
  }

  /** Adds a {@code number} property with the given parameters. */
  public SELF number(String nameOrId, NumberSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /** Adds a {@code people} property with default parameters. */
  public SELF people(String nameOrId) {
    return people(nameOrId, new PeopleSchemaParams());
  }

  /** Adds a {@code people} property with the given parameters. */
  public SELF people(String nameOrId, PeopleSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /** Adds a {@code phone_number} property with default parameters. */
  public SELF phone(String nameOrId) {
    return phone(nameOrId, new PhoneSchemaParams());
  }

  /** Adds a {@code phone_number} property with the given parameters. */
  public SELF phone(String nameOrId, PhoneSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /**
   * Adds a {@code place} (location) property with default parameters.
   *
   * <p>Stores geographic data: latitude, longitude, name, and address.
   */
  public SELF place(String nameOrId) {
    return place(nameOrId, new PlaceSchemaParams());
  }

  /** Adds a {@code place} (location) property with the given parameters. */
  public SELF place(String nameOrId, PlaceSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /**
   * Adds a single-way {@code relation} property pointing to the given data source.
   *
   * @param dataSourceId the UUID of the related Notion database
   */
  public SELF relation(String nameOrId, String dataSourceId) {
    return relation(
        nameOrId, RelationSchemaParams.builder().dataSourceId(dataSourceId).build());
  }

  /**
   * Adds a dual-property (two-way) {@code relation}.
   *
   * @param dataSourceId   the UUID of the related Notion database
   * @param syncedPropName the name of the mirrored property on the target database
   */
  public SELF relation(String nameOrId, String dataSourceId, String syncedPropName) {
    return relation(
        nameOrId,
        RelationSchemaParams.builder()
            .dataSourceId(dataSourceId)
            .syncedProperty(syncedPropName)
            .build());
  }

  /** Adds a {@code relation} property with the given parameters. */
  public SELF relation(String nameOrId, RelationSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /**
   * Adds a {@code rollup} property that aggregates values from a related database.
   *
   * @param relationNameOrId the name or ID of the relation property to roll up through
   * @param rollupNameOrId   the name or ID of the property in the related DB to aggregate
   * @param rollupFunction   the aggregation function (e.g. {@code "sum"}, {@code "count"})
   */
  public SELF rollup(
      String nameOrId, String relationNameOrId, String rollupNameOrId, String rollupFunction) {
    RollupSchemaParams params = new RollupSchemaParams();
    params.getRollup().setRelationPropertyId(relationNameOrId);
    params.getRollup().setRollupPropertyId(rollupNameOrId);
    params.getRollup().setFunction(rollupFunction);
    return rollup(nameOrId, params);
  }

  /** Adds a {@code rollup} property with the given parameters. */
  public SELF rollup(String nameOrId, RollupSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /** Adds a {@code select} property with no predefined options. */
  public SELF select(String nameOrId) {
    return select(nameOrId, new SelectSchemaParams());
  }

  /** Adds a {@code select} property with the given predefined option names. */
  public SELF select(String nameOrId, String... options) {
    return select(nameOrId, SelectSchemaParams.of(options));
  }

  /** Adds a {@code select} property with the given parameters. */
  public SELF select(String nameOrId, SelectSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /**
   * Adds a {@code status} property with default parameters.
   *
   * <p>Notion places all options in the "To-do" group automatically on creation. Use
   * {@link StatusSchemaParams#editor(StatusSchema)} after creation to reorganise groups.
   */
  public SELF status(String nameOrId) {
    return status(nameOrId, new StatusSchemaParams());
  }

  /** Adds a {@code status} property with the given parameters. */
  public SELF status(String nameOrId, StatusSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /** Adds a {@code rich_text} property with default parameters. */
  public SELF text(String nameOrId) {
    return text(nameOrId, new RichTextSchemaParams());
  }

  /** Adds a {@code rich_text} property with the given parameters. */
  public SELF text(String nameOrId, RichTextSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /**
   * Adds a {@code title} property with default parameters.
   *
   * <p>Every Notion database must have exactly one title property.
   */
  public SELF title(String nameOrId) {
    return title(nameOrId, new TitleSchemaParams());
  }

  /** Adds a {@code title} property with the given parameters. */
  public SELF title(String nameOrId, TitleSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /**
   * Adds a {@code unique_id} property with default parameters.
   *
   * <p>Auto-increments and assigns each page a sequential numeric identifier.
   */
  public SELF uniqueId(String nameOrId) {
    return uniqueId(nameOrId, new UniqueIdSchemaParams());
  }

  /** Adds a {@code unique_id} property with the given parameters. */
  public SELF uniqueId(String nameOrId, UniqueIdSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  /** Adds a {@code url} property with default parameters. */
  public SELF url(String nameOrId) {
    return url(nameOrId, new UrlSchemaParams());
  }

  /** Adds a {@code url} property with the given parameters. */
  public SELF url(String nameOrId, UrlSchemaParams params) {
    properties.put(nameOrId, params);
    return self();
  }

  // ── Modify operations ───────────────────────────────────────────────────

  /**
   * Renames an existing property.
   *
   * <p>If the property is not already present a stub entry is created so that the rename
   * instruction is sent to the Notion API even without re-specifying every other property.
   */
  public SELF rename(String nameOrId, String newName) {
    properties
        .computeIfAbsent(nameOrId, k -> new DataSourcePropertySchemaParams())
        .setName(newName);
    return self();
  }

  /**
   * Marks a property for deletion.
   *
   * <p>A {@code null} value instructs the Notion API to permanently remove the property and all
   * data stored in it.
   */
  public SELF delete(String nameOrId) {
    properties.put(nameOrId, null);
    return self();
  }
}



