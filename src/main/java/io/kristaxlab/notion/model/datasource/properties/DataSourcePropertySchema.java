package io.kristaxlab.notion.model.datasource.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

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
  @JsonSubTypes.Type(value = CheckboxSchema.class, name = "checkbox"),
  @JsonSubTypes.Type(value = ButtonSchema.class, name = "button"),
  @JsonSubTypes.Type(value = CreatedBySchema.class, name = "created_by"),
  @JsonSubTypes.Type(value = CreatedTimeSchema.class, name = "created_time"),
  @JsonSubTypes.Type(value = DateSchema.class, name = "date"),
  @JsonSubTypes.Type(value = EmailSchema.class, name = "email"),
  @JsonSubTypes.Type(value = FilesSchema.class, name = "files"),
  @JsonSubTypes.Type(value = FormulaSchema.class, name = "formula"),
  @JsonSubTypes.Type(value = LastEditedBySchema.class, name = "last_edited_by"),
  @JsonSubTypes.Type(value = LastEditedTimeSchema.class, name = "last_edited_time"),
  @JsonSubTypes.Type(value = MultiSelectSchema.class, name = "multi_select"),
  @JsonSubTypes.Type(value = NumberSchema.class, name = "number"),
  @JsonSubTypes.Type(value = PeopleSchema.class, name = "people"),
  @JsonSubTypes.Type(value = PhoneSchema.class, name = "phone_number"),
  @JsonSubTypes.Type(value = RelationSchema.class, name = "relation"),
  @JsonSubTypes.Type(value = RichTextSchema.class, name = "rich_text"),
  @JsonSubTypes.Type(value = RollupSchema.class, name = "rollup"),
  @JsonSubTypes.Type(value = SelectSchema.class, name = "select"),
  @JsonSubTypes.Type(value = StatusSchema.class, name = "status"),
  @JsonSubTypes.Type(value = TitleSchema.class, name = "title"),
  @JsonSubTypes.Type(value = UniqueIdSchema.class, name = "unique_id"),
  @JsonSubTypes.Type(value = UrlSchema.class, name = "url"),
  @JsonSubTypes.Type(value = VerificationSchema.class, name = "verification"),
  @JsonSubTypes.Type(value = PlaceSchema.class, name = "place")
})
@Getter
@Setter
public abstract class DataSourcePropertySchema {

  private String id;

  private String name;

  private String description;

  public DataSourcePropertySchema() {}

  public abstract String getType();

  public DataSourcePropertySchema(String name) {
    this.name = name;
  }

  // Type conversion methods similar to PageProperty
  public CheckboxSchema asCheckbox() {
    return (CheckboxSchema) this;
  }

  public CreatedBySchema asCreatedBy() {
    return (CreatedBySchema) this;
  }

  public CreatedTimeSchema asCreatedTime() {
    return (CreatedTimeSchema) this;
  }

  public DateSchema asDate() {
    return (DateSchema) this;
  }

  public EmailSchema asEmail() {
    return (EmailSchema) this;
  }

  public FilesSchema asFiles() {
    return (FilesSchema) this;
  }

  public FormulaSchema asFormula() {
    return (FormulaSchema) this;
  }

  public LastEditedBySchema asLastEditedBy() {
    return (LastEditedBySchema) this;
  }

  public LastEditedTimeSchema asLastEditedTime() {
    return (LastEditedTimeSchema) this;
  }

  public MultiSelectSchema asMultiSelect() {
    return (MultiSelectSchema) this;
  }

  public NumberSchema asNumber() {
    return (NumberSchema) this;
  }

  public PeopleSchema asPeople() {
    return (PeopleSchema) this;
  }

  public PhoneSchema asPhoneNumber() {
    return (PhoneSchema) this;
  }

  public RelationSchema asRelation() {
    return (RelationSchema) this;
  }

  public RichTextSchema asRichText() {
    return (RichTextSchema) this;
  }

  public RollupSchema asRollup() {
    return (RollupSchema) this;
  }

  public SelectSchema asSelect() {
    return (SelectSchema) this;
  }

  public StatusSchema asStatus() {
    return (StatusSchema) this;
  }

  public TitleSchema asTitle() {
    return (TitleSchema) this;
  }

  public UniqueIdSchema asUniqueId() {
    return (UniqueIdSchema) this;
  }

  public UrlSchema asUrl() {
    return (UrlSchema) this;
  }

  public VerificationSchema asVerification() {
    return (VerificationSchema) this;
  }

  // Type checking methods
  @JsonIgnore
  public boolean isButton() {
    return "button".equals(getType());
  }

  @JsonIgnore
  public boolean isCheckbox() {
    return "checkbox".equals(getType());
  }

  @JsonIgnore
  public boolean isCreatedBy() {
    return "created_by".equals(getType());
  }

  @JsonIgnore
  public boolean isCreatedTime() {
    return "created_time".equals(getType());
  }

  @JsonIgnore
  public boolean isDate() {
    return "date".equals(getType());
  }

  @JsonIgnore
  public boolean isEmail() {
    return "email".equals(getType());
  }

  @JsonIgnore
  public boolean isFiles() {
    return "files".equals(getType());
  }

  @JsonIgnore
  public boolean isFormula() {
    return "formula".equals(getType());
  }

  @JsonIgnore
  public boolean isLastEditedBy() {
    return "last_edited_by".equals(getType());
  }

  @JsonIgnore
  public boolean isLastEditedTime() {
    return "last_edited_time".equals(getType());
  }

  @JsonIgnore
  public boolean isMultiSelect() {
    return "multi_select".equals(getType());
  }

  @JsonIgnore
  public boolean isNumber() {
    return "number".equals(getType());
  }

  @JsonIgnore
  public boolean isPeople() {
    return "people".equals(getType());
  }

  @JsonIgnore
  public boolean isPhoneNumber() {
    return "phone_number".equals(getType());
  }

  @JsonIgnore
  public boolean isRelation() {
    return "relation".equals(getType());
  }

  @JsonIgnore
  public boolean isRichText() {
    return "rich_text".equals(getType());
  }

  @JsonIgnore
  public boolean isRollup() {
    return "rollup".equals(getType());
  }

  @JsonIgnore
  public boolean isSelect() {
    return "select".equals(getType());
  }

  @JsonIgnore
  public boolean isStatus() {
    return "status".equals(getType());
  }

  @JsonIgnore
  public boolean isTitle() {
    return "title".equals(getType());
  }

  @JsonIgnore
  public boolean isUniqueId() {
    return "unique_id".equals(getType());
  }

  @JsonIgnore
  public boolean isUrl() {
    return "url".equals(getType());
  }

  @JsonIgnore
  public boolean isVerification() {
    return "verification".equals(getType());
  }

  @JsonIgnore
  public boolean isPlace() {
    return "place".equals(getType());
  }
}
