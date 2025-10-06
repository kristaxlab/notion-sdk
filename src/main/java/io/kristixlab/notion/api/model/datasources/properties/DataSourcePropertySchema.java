package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
        @JsonSubTypes.Type(value = CheckboxDataSourcePropertySchema.class, name = "checkbox"),
        @JsonSubTypes.Type(value = ButtonDataSourcePropertySchema.class, name = "button"),
        @JsonSubTypes.Type(value = CreatedByDataSourcePropertySchema.class, name = "created_by"),
        @JsonSubTypes.Type(value = CreatedTimeDataSourcePropertySchema.class, name = "created_time"),
        @JsonSubTypes.Type(value = DateDataSourcePropertySchema.class, name = "date"),
        @JsonSubTypes.Type(value = EmailDataSourcePropertySchema.class, name = "email"),
        @JsonSubTypes.Type(value = FilesDataSourcePropertySchema.class, name = "files"),
        @JsonSubTypes.Type(value = FormulaDataSourcePropertySchema.class, name = "formula"),
        @JsonSubTypes.Type(value = LastEditedByDataSourcePropertySchema.class, name = "last_edited_by"),
        @JsonSubTypes.Type(value = LastEditedTimeDataSourcePropertySchema.class, name = "last_edited_time"),
        @JsonSubTypes.Type(value = MultiSelectDataSourcePropertySchema.class, name = "multi_select"),
        @JsonSubTypes.Type(value = NumberDataSourcePropertySchema.class, name = "number"),
        @JsonSubTypes.Type(value = PeopleDataSourcePropertySchema.class, name = "people"),
        @JsonSubTypes.Type(value = PhoneNumberDataSourcePropertySchema.class, name = "phone_number"),
        @JsonSubTypes.Type(value = RelationDataSourcePropertySchema.class, name = "relation"),
        @JsonSubTypes.Type(value = RichTextDataSourcePropertySchema.class, name = "rich_text"),
        @JsonSubTypes.Type(value = RollupDataSourcePropertySchema.class, name = "rollup"),
        @JsonSubTypes.Type(value = SelectDataSourcePropertySchema.class, name = "select"),
        @JsonSubTypes.Type(value = StatusDataSourcePropertySchema.class, name = "status"),
        @JsonSubTypes.Type(value = TitleDataSourcePropertySchema.class, name = "title"),
        @JsonSubTypes.Type(value = UniqueIdDataSourcePropertySchema.class, name = "unique_id"),
        @JsonSubTypes.Type(value = UrlDataSourcePropertySchema.class, name = "url"),
        @JsonSubTypes.Type(value = VerificationDataSourcePropertySchema.class, name = "verification"),
        @JsonSubTypes.Type(value = PlaceDataSourcePropertySchema.class, name = "place")
})
@Data
public abstract class DataSourcePropertySchema {

  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("type")
  private String type;

  @JsonProperty("description")
  private String description;

  // Type conversion methods similar to PageProperty
  public CheckboxDataSourcePropertySchema asCheckbox() {
    return (CheckboxDataSourcePropertySchema) this;
  }

  public CreatedByDataSourcePropertySchema asCreatedBy() {
    return (CreatedByDataSourcePropertySchema) this;
  }

  public CreatedTimeDataSourcePropertySchema asCreatedTime() {
    return (CreatedTimeDataSourcePropertySchema) this;
  }

  public DateDataSourcePropertySchema asDate() {
    return (DateDataSourcePropertySchema) this;
  }

  public EmailDataSourcePropertySchema asEmail() {
    return (EmailDataSourcePropertySchema) this;
  }

  public FilesDataSourcePropertySchema asFiles() {
    return (FilesDataSourcePropertySchema) this;
  }

  public FormulaDataSourcePropertySchema asFormula() {
    return (FormulaDataSourcePropertySchema) this;
  }

  public LastEditedByDataSourcePropertySchema asLastEditedBy() {
    return (LastEditedByDataSourcePropertySchema) this;
  }

  public LastEditedTimeDataSourcePropertySchema asLastEditedTime() {
    return (LastEditedTimeDataSourcePropertySchema) this;
  }

  public MultiSelectDataSourcePropertySchema asMultiSelect() {
    return (MultiSelectDataSourcePropertySchema) this;
  }

  public NumberDataSourcePropertySchema asNumber() {
    return (NumberDataSourcePropertySchema) this;
  }

  public PeopleDataSourcePropertySchema asPeople() {
    return (PeopleDataSourcePropertySchema) this;
  }

  public PhoneNumberDataSourcePropertySchema asPhoneNumber() {
    return (PhoneNumberDataSourcePropertySchema) this;
  }

  public RelationDataSourcePropertySchema asRelation() {
    return (RelationDataSourcePropertySchema) this;
  }

  public RichTextDataSourcePropertySchema asRichText() {
    return (RichTextDataSourcePropertySchema) this;
  }

  public RollupDataSourcePropertySchema asRollup() {
    return (RollupDataSourcePropertySchema) this;
  }

  public SelectDataSourcePropertySchema asSelect() {
    return (SelectDataSourcePropertySchema) this;
  }

  public StatusDataSourcePropertySchema asStatus() {
    return (StatusDataSourcePropertySchema) this;
  }

  public TitleDataSourcePropertySchema asTitle() {
    return (TitleDataSourcePropertySchema) this;
  }

  public UniqueIdDataSourcePropertySchema asUniqueId() {
    return (UniqueIdDataSourcePropertySchema) this;
  }

  public UrlDataSourcePropertySchema asUrl() {
    return (UrlDataSourcePropertySchema) this;
  }

  public VerificationDataSourcePropertySchema asVerification() {
    return (VerificationDataSourcePropertySchema) this;
  }

  // Type checking methods
  @JsonIgnore
  public boolean isButton() {
    return "button".equals(type);
  }

  @JsonIgnore
  public boolean isCheckbox() {
    return "checkbox".equals(type);
  }

  @JsonIgnore
  public boolean isCreatedBy() {
    return "created_by".equals(type);
  }

  @JsonIgnore
  public boolean isCreatedTime() {
    return "created_time".equals(type);
  }

  @JsonIgnore
  public boolean isDate() {
    return "date".equals(type);
  }

  @JsonIgnore
  public boolean isEmail() {
    return "email".equals(type);
  }

  @JsonIgnore
  public boolean isFiles() {
    return "files".equals(type);
  }

  @JsonIgnore
  public boolean isFormula() {
    return "formula".equals(type);
  }

  @JsonIgnore
  public boolean isLastEditedBy() {
    return "last_edited_by".equals(type);
  }

  @JsonIgnore
  public boolean isLastEditedTime() {
    return "last_edited_time".equals(type);
  }

  @JsonIgnore
  public boolean isMultiSelect() {
    return "multi_select".equals(type);
  }

  @JsonIgnore
  public boolean isNumber() {
    return "number".equals(type);
  }

  @JsonIgnore
  public boolean isPeople() {
    return "people".equals(type);
  }

  @JsonIgnore
  public boolean isPhoneNumber() {
    return "phone_number".equals(type);
  }

  @JsonIgnore
  public boolean isRelation() {
    return "relation".equals(type);
  }

  @JsonIgnore
  public boolean isRichText() {
    return "rich_text".equals(type);
  }

  @JsonIgnore
  public boolean isRollup() {
    return "rollup".equals(type);
  }

  @JsonIgnore
  public boolean isSelect() {
    return "select".equals(type);
  }

  @JsonIgnore
  public boolean isStatus() {
    return "status".equals(type);
  }

  @JsonIgnore
  public boolean isTitle() {
    return "title".equals(type);
  }

  @JsonIgnore
  public boolean isUniqueId() {
    return "unique_id".equals(type);
  }

  @JsonIgnore
  public boolean isUrl() {
    return "url".equals(type);
  }

  @JsonIgnore
  public boolean isVerification() {
    return "verification".equals(type);
  }

  @JsonIgnore
  public boolean isPlace() {
    return "place".equals(type);
  }
}
