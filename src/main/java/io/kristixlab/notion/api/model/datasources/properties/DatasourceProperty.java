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
    defaultImpl = UnknownDatasourceProperty.class,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = CheckboxDatasourceProperty.class, name = "checkbox"),
  @JsonSubTypes.Type(value = ButtonDatasourceProperty.class, name = "button"),
  @JsonSubTypes.Type(value = CreatedByDatasourceProperty.class, name = "created_by"),
  @JsonSubTypes.Type(value = CreatedTimeDatasourceProperty.class, name = "created_time"),
  @JsonSubTypes.Type(value = DateDatasourceProperty.class, name = "date"),
  @JsonSubTypes.Type(value = EmailDatasourceProperty.class, name = "email"),
  @JsonSubTypes.Type(value = FilesDatasourceProperty.class, name = "files"),
  @JsonSubTypes.Type(value = FormulaDatasourceProperty.class, name = "formula"),
  @JsonSubTypes.Type(value = LastEditedByDatasourceProperty.class, name = "last_edited_by"),
  @JsonSubTypes.Type(value = LastEditedTimeDatasourceProperty.class, name = "last_edited_time"),
  @JsonSubTypes.Type(value = MultiSelectDatasourceProperty.class, name = "multi_select"),
  @JsonSubTypes.Type(value = NumberDatasourceProperty.class, name = "number"),
  @JsonSubTypes.Type(value = PeopleDatasourceProperty.class, name = "people"),
  @JsonSubTypes.Type(value = PhoneNumberDatasourceProperty.class, name = "phone_number"),
  @JsonSubTypes.Type(value = RelationDatasourceProperty.class, name = "relation"),
  @JsonSubTypes.Type(value = RichTextDatasourceProperty.class, name = "rich_text"),
  @JsonSubTypes.Type(value = RollupDatasourceProperty.class, name = "rollup"),
  @JsonSubTypes.Type(value = SelectDatasourceProperty.class, name = "select"),
  @JsonSubTypes.Type(value = StatusDatasourceProperty.class, name = "status"),
  @JsonSubTypes.Type(value = TitleDatasourceProperty.class, name = "title"),
  @JsonSubTypes.Type(value = UniqueIdDatasourceProperty.class, name = "unique_id"),
  @JsonSubTypes.Type(value = UrlDatasourceProperty.class, name = "url"),
  @JsonSubTypes.Type(value = VerificationDatasourceProperty.class, name = "verification"),
  @JsonSubTypes.Type(value = PlaceDatasourceProperty.class, name = "place")
})
@Data
public abstract class DatasourceProperty {

  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("type")
  private String type;

  @JsonProperty("description")
  private String description;

  // Type conversion methods similar to PageProperty
  public CheckboxDatasourceProperty asCheckbox() {
    return (CheckboxDatasourceProperty) this;
  }

  public CreatedByDatasourceProperty asCreatedBy() {
    return (CreatedByDatasourceProperty) this;
  }

  public CreatedTimeDatasourceProperty asCreatedTime() {
    return (CreatedTimeDatasourceProperty) this;
  }

  public DateDatasourceProperty asDate() {
    return (DateDatasourceProperty) this;
  }

  public EmailDatasourceProperty asEmail() {
    return (EmailDatasourceProperty) this;
  }

  public FilesDatasourceProperty asFiles() {
    return (FilesDatasourceProperty) this;
  }

  public FormulaDatasourceProperty asFormula() {
    return (FormulaDatasourceProperty) this;
  }

  public LastEditedByDatasourceProperty asLastEditedBy() {
    return (LastEditedByDatasourceProperty) this;
  }

  public LastEditedTimeDatasourceProperty asLastEditedTime() {
    return (LastEditedTimeDatasourceProperty) this;
  }

  public MultiSelectDatasourceProperty asMultiSelect() {
    return (MultiSelectDatasourceProperty) this;
  }

  public NumberDatasourceProperty asNumber() {
    return (NumberDatasourceProperty) this;
  }

  public PeopleDatasourceProperty asPeople() {
    return (PeopleDatasourceProperty) this;
  }

  public PhoneNumberDatasourceProperty asPhoneNumber() {
    return (PhoneNumberDatasourceProperty) this;
  }

  public RelationDatasourceProperty asRelation() {
    return (RelationDatasourceProperty) this;
  }

  public RichTextDatasourceProperty asRichText() {
    return (RichTextDatasourceProperty) this;
  }

  public RollupDatasourceProperty asRollup() {
    return (RollupDatasourceProperty) this;
  }

  public SelectDatasourceProperty asSelect() {
    return (SelectDatasourceProperty) this;
  }

  public StatusDatasourceProperty asStatus() {
    return (StatusDatasourceProperty) this;
  }

  public TitleDatasourceProperty asTitle() {
    return (TitleDatasourceProperty) this;
  }

  public UniqueIdDatasourceProperty asUniqueId() {
    return (UniqueIdDatasourceProperty) this;
  }

  public UrlDatasourceProperty asUrl() {
    return (UrlDatasourceProperty) this;
  }

  public VerificationDatasourceProperty asVerification() {
    return (VerificationDatasourceProperty) this;
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
}
