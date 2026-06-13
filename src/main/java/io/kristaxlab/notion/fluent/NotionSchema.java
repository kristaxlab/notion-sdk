package io.kristaxlab.notion.fluent;

import io.kristaxlab.notion.model.datasource.properties.ButtonSchema;
import io.kristaxlab.notion.model.datasource.properties.CheckboxSchema;
import io.kristaxlab.notion.model.datasource.properties.CreatedBySchema;
import io.kristaxlab.notion.model.datasource.properties.CreatedTimeSchema;
import io.kristaxlab.notion.model.datasource.properties.DataSourcePropertySchema;
import io.kristaxlab.notion.model.datasource.properties.DateSchema;
import io.kristaxlab.notion.model.datasource.properties.EmailSchema;
import io.kristaxlab.notion.model.datasource.properties.FilesSchema;
import io.kristaxlab.notion.model.datasource.properties.FormulaSchema;
import io.kristaxlab.notion.model.datasource.properties.LastEditedBySchema;
import io.kristaxlab.notion.model.datasource.properties.LastEditedTimeSchema;
import io.kristaxlab.notion.model.datasource.properties.MultiSelectSchema;
import io.kristaxlab.notion.model.datasource.properties.NumberFormatType;
import io.kristaxlab.notion.model.datasource.properties.NumberSchema;
import io.kristaxlab.notion.model.datasource.properties.PeopleSchema;
import io.kristaxlab.notion.model.datasource.properties.PhoneSchema;
import io.kristaxlab.notion.model.datasource.properties.PlaceSchema;
import io.kristaxlab.notion.model.datasource.properties.RelationSchema;
import io.kristaxlab.notion.model.datasource.properties.RichTextSchema;
import io.kristaxlab.notion.model.datasource.properties.RollupFunctionType;
import io.kristaxlab.notion.model.datasource.properties.RollupSchema;
import io.kristaxlab.notion.model.datasource.properties.SelectOption;
import io.kristaxlab.notion.model.datasource.properties.SelectSchema;
import io.kristaxlab.notion.model.datasource.properties.StatusSchema;
import io.kristaxlab.notion.model.datasource.properties.TitleSchema;
import io.kristaxlab.notion.model.datasource.properties.UniqueIdSchema;
import io.kristaxlab.notion.model.datasource.properties.UrlSchema;
import io.kristaxlab.notion.model.datasource.properties.VerificationSchema;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Static factory helpers for constructing data source property <b>schemas</b> (column definitions).
 *
 * <p>Where {@link NotionProperties} builds page property <i>values</i> (the data inside a row),
 * this class builds the <i>schema</i> of a data source — the typed columns themselves. It mirrors
 * the {@link NotionBlocks} / {@link NotionProperties} design so the three DSLs feel the same.
 *
 * <p>Designed for use with a static import so call sites read as a concise DSL:
 *
 * <pre>{@code
 * import static io.kristaxlab.notion.fluent.NotionSchema.*;
 *
 * client.dataSources().create(ds -> ds
 *     .inDatabase("database-id")
 *     .title("Tasks")
 *     .properties(s -> s
 *         .title("Name")
 *         .richText("Description")
 *         .number("Priority")
 *         .select("Status", "Todo", "In progress", "Done")
 *         .multiSelect("Tags", "urgent", "review")
 *         .date("Due Date")
 *         .checkbox("Done")));
 * }</pre>
 *
 * <p>The static methods on this class return individual {@link DataSourcePropertySchema} instances;
 * use {@link NotionSchemaBuilder} to assemble a {@code name -> schema} map for create/update
 * payloads. As with property values, Notion accepts <b>property names and ids interchangeably</b>
 * as map keys.
 */
public final class NotionSchema {

  private NotionSchema() {}

  /**
   * Creates a fluent schema builder.
   *
   * @return new schema builder
   */
  public static NotionSchemaBuilder schemaBuilder() {
    return new NotionSchemaBuilder();
  }

  // Title

  /**
   * Creates a title column schema. Every data source must have exactly one title column.
   *
   * @return title schema
   */
  public static TitleSchema title() {
    return new TitleSchema();
  }

  // Rich text

  /**
   * Creates a rich-text column schema.
   *
   * @return rich-text schema
   */
  public static RichTextSchema richText() {
    return new RichTextSchema();
  }

  // Number

  /**
   * Creates a number column schema with the default format.
   *
   * @return number schema
   */
  public static NumberSchema number() {
    return number(NumberFormatType.NUMBER);
  }

  /**
   * Creates a number column schema with the given format.
   *
   * @param format number display format
   * @return number schema
   */
  public static NumberSchema number(NumberFormatType format) {
    return number(format == null ? null : format.getValue());
  }

  /**
   * Creates a number column schema from a raw format token.
   *
   * @param format number display format token (e.g. {@code "dollar"})
   * @return number schema
   */
  public static NumberSchema number(String format) {
    NumberSchema schema = new NumberSchema();
    schema.getNumber().setFormat(format);
    return schema;
  }

  // Select

  /**
   * Creates a select column schema from option names.
   *
   * @param optionNames option names to seed the column with
   * @return select schema
   */
  public static SelectSchema select(String... optionNames) {
    return select(toOptions(optionNames));
  }

  /**
   * Creates a select column schema from prepared options.
   *
   * @param options select options
   * @return select schema
   */
  public static SelectSchema select(List<SelectOption> options) {
    SelectSchema schema = new SelectSchema();
    schema.getSelect().setOptions(new ArrayList<>(options));
    return schema;
  }

  // Multi-select

  /**
   * Creates a multi-select column schema from option names.
   *
   * @param optionNames option names to seed the column with
   * @return multi-select schema
   */
  public static MultiSelectSchema multiSelect(String... optionNames) {
    return multiSelect(toOptions(optionNames));
  }

  /**
   * Creates a multi-select column schema from prepared options.
   *
   * @param options select options
   * @return multi-select schema
   */
  public static MultiSelectSchema multiSelect(List<SelectOption> options) {
    MultiSelectSchema schema = new MultiSelectSchema();
    schema.getMultiSelect().setOptions(new ArrayList<>(options));
    return schema;
  }

  /**
   * Creates a {@link SelectOption} with just a name.
   *
   * @param name option name
   * @return select option
   */
  public static SelectOption option(String name) {
    SelectOption option = new SelectOption();
    option.setName(name);
    return option;
  }

  /**
   * Creates a {@link SelectOption} with a name and color.
   *
   * @param name option name
   * @param color option color token (e.g. {@code "blue"})
   * @return select option
   */
  public static SelectOption option(String name, String color) {
    SelectOption option = option(name);
    option.setColor(color);
    return option;
  }

  // Status

  /**
   * Creates a status column schema.
   *
   * @return status schema
   */
  public static StatusSchema status() {
    return new StatusSchema();
  }

  // Date

  /**
   * Creates a date column schema.
   *
   * @return date schema
   */
  public static DateSchema date() {
    return new DateSchema();
  }

  // People

  /**
   * Creates a people column schema.
   *
   * @return people schema
   */
  public static PeopleSchema people() {
    return new PeopleSchema();
  }

  // Files

  /**
   * Creates a files column schema.
   *
   * @return files schema
   */
  public static FilesSchema files() {
    return new FilesSchema();
  }

  // Checkbox

  /**
   * Creates a checkbox column schema.
   *
   * @return checkbox schema
   */
  public static CheckboxSchema checkbox() {
    return new CheckboxSchema();
  }

  // URL, email, phone

  /**
   * Creates a URL column schema.
   *
   * @return URL schema
   */
  public static UrlSchema url() {
    return new UrlSchema();
  }

  /**
   * Creates an email column schema.
   *
   * @return email schema
   */
  public static EmailSchema email() {
    return new EmailSchema();
  }

  /**
   * Creates a phone-number column schema.
   *
   * @return phone-number schema
   */
  public static PhoneSchema phoneNumber() {
    return new PhoneSchema();
  }

  // Formula

  /**
   * Creates a formula column schema.
   *
   * @param expression the Notion formula expression
   * @return formula schema
   */
  public static FormulaSchema formula(String expression) {
    FormulaSchema schema = new FormulaSchema();
    schema.getFormula().setExpression(expression);
    return schema;
  }

  // Relation

  /**
   * Creates a single-property relation column schema linking to another data source.
   *
   * @param dataSourceId the id of the related data source
   * @return relation schema
   */
  public static RelationSchema relation(String dataSourceId) {
    RelationSchema schema = new RelationSchema();
    schema.getRelation().setDataSourceId(dataSourceId);
    schema.getRelation().setType("single_property");
    schema.getRelation().setSingleProperty(new RelationSchema.SinglePropertyConfig());
    return schema;
  }

  /**
   * Creates a dual-property relation column schema. Notion will create and keep a synced property
   * on the related data source.
   *
   * @param dataSourceId the id of the related data source
   * @param syncedPropertyName the name of the synced property created on the related data source
   * @return relation schema
   */
  public static RelationSchema relationDual(String dataSourceId, String syncedPropertyName) {
    RelationSchema schema = new RelationSchema();
    schema.getRelation().setDataSourceId(dataSourceId);
    schema.getRelation().setType("dual_property");
    RelationSchema.DualPropertyConfig dual = new RelationSchema.DualPropertyConfig();
    dual.setSyncedPropertyName(syncedPropertyName);
    schema.getRelation().setDualProperty(dual);
    return schema;
  }

  // Rollup

  /**
   * Creates a rollup column schema that aggregates a property from a related data source.
   *
   * @param relationPropertyName the relation column to roll up through
   * @param rollupPropertyName the property in the related data source to aggregate
   * @param function the aggregation function
   * @return rollup schema
   */
  public static RollupSchema rollup(
      String relationPropertyName, String rollupPropertyName, RollupFunctionType function) {
    return rollup(
        relationPropertyName, rollupPropertyName, function == null ? null : function.getValue());
  }

  /**
   * Creates a rollup column schema from a raw function token.
   *
   * @param relationPropertyName the relation column to roll up through
   * @param rollupPropertyName the property in the related data source to aggregate
   * @param function the aggregation function token (e.g. {@code "sum"})
   * @return rollup schema
   */
  public static RollupSchema rollup(
      String relationPropertyName, String rollupPropertyName, String function) {
    RollupSchema schema = new RollupSchema();
    schema.getRollup().setRelationPropertyName(relationPropertyName);
    schema.getRollup().setRollupPropertyName(rollupPropertyName);
    schema.getRollup().setFunction(function);
    return schema;
  }

  // Computed / read-only columns

  /**
   * Creates a created-time column schema.
   *
   * @return created-time schema
   */
  public static CreatedTimeSchema createdTime() {
    return new CreatedTimeSchema();
  }

  /**
   * Creates a created-by column schema.
   *
   * @return created-by schema
   */
  public static CreatedBySchema createdBy() {
    return new CreatedBySchema();
  }

  /**
   * Creates a last-edited-time column schema.
   *
   * @return last-edited-time schema
   */
  public static LastEditedTimeSchema lastEditedTime() {
    return new LastEditedTimeSchema();
  }

  /**
   * Creates a last-edited-by column schema.
   *
   * @return last-edited-by schema
   */
  public static LastEditedBySchema lastEditedBy() {
    return new LastEditedBySchema();
  }

  // Button, place, verification

  /**
   * Creates a button column schema.
   *
   * @return button schema
   */
  public static ButtonSchema button() {
    return new ButtonSchema();
  }

  /**
   * Creates a place (location) column schema.
   *
   * @return place schema
   */
  public static PlaceSchema place() {
    return new PlaceSchema();
  }

  /**
   * Creates a verification column schema.
   *
   * @return verification schema
   */
  public static VerificationSchema verification() {
    return new VerificationSchema();
  }

  // Unique id

  /**
   * Creates a unique-id column schema with no prefix.
   *
   * @return unique-id schema
   */
  public static UniqueIdSchema uniqueId() {
    return uniqueId(null);
  }

  /**
   * Creates a unique-id column schema with the given prefix.
   *
   * @param prefix the prefix prepended to generated ids (e.g. {@code "TASK"})
   * @return unique-id schema
   */
  public static UniqueIdSchema uniqueId(String prefix) {
    UniqueIdSchema schema = new UniqueIdSchema();
    schema.getUniqueId().setPrefix(prefix);
    return schema;
  }

  private static List<SelectOption> toOptions(String... optionNames) {
    List<SelectOption> options = new ArrayList<>();
    if (optionNames != null) {
      for (String name : Arrays.asList(optionNames)) {
        options.add(option(name));
      }
    }
    return options;
  }
}
