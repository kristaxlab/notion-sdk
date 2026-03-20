package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * Base class for database property objects. Database properties define the schema and configuration
 * of database columns.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    defaultImpl = UnknownDataSourcePropertySchema.class,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = CheckboxSchemaParams.class, name = "checkbox"),
  @JsonSubTypes.Type(value = CreatedBySchemaParams.class, name = "created_by"),
  @JsonSubTypes.Type(value = CreatedTimeSchemaParams.class, name = "created_time"),
  @JsonSubTypes.Type(value = DateSchemaParams.class, name = "date"),
  @JsonSubTypes.Type(value = EmailSchemaParams.class, name = "email"),
  @JsonSubTypes.Type(value = FilesSchemaParams.class, name = "files"),
  @JsonSubTypes.Type(value = FormulaSchemaParams.class, name = "formula"),
  @JsonSubTypes.Type(value = LastEditedBySchemaParams.class, name = "last_edited_by"),
  @JsonSubTypes.Type(value = LastEditedTimeSchemaParams.class, name = "last_edited_time"),
  @JsonSubTypes.Type(value = MultiSelectSchemaParams.class, name = "multi_select"),
  @JsonSubTypes.Type(value = NumberSchemaParams.class, name = "number"),
  @JsonSubTypes.Type(value = PeopleSchemaParams.class, name = "people"),
  @JsonSubTypes.Type(value = PhoneSchemaParams.class, name = "phone_number"),
  @JsonSubTypes.Type(value = RelationSchemaParams.class, name = "relation"), // TODO supported?
  @JsonSubTypes.Type(value = RichTextSchemaParams.class, name = "rich_text"),
  @JsonSubTypes.Type(value = RollupSchemaParams.class, name = "rollup"), // TODO supported?
  @JsonSubTypes.Type(value = SelectSchemaParams.class, name = "select"),
  @JsonSubTypes.Type(value = StatusSchemaParams.class, name = "status"), // TODO supported?
  @JsonSubTypes.Type(value = TitleSchemaParams.class, name = "title"),
  @JsonSubTypes.Type(value = UniqueIdSchemaParams.class, name = "unique_id"),
  @JsonSubTypes.Type(value = UrlSchemaParams.class, name = "url"),
  @JsonSubTypes.Type(value = PlaceSchemaParams.class, name = "place")
})
@Data
public class DataSourcePropertySchemaParams {

  /*
   * Use to change name
   */
  @JsonProperty("name")
  private String name;

  @JsonProperty("type")
  private String type;

  @JsonProperty("description")
  private String description;

  public DataSourcePropertySchemaParams() {
    setType(getType());
  }

  public DataSourcePropertySchemaParams(String name) {
    setName(name);
    setType(getType());
  }
}
