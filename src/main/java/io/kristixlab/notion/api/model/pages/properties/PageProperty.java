package io.kristixlab.notion.api.model.pages.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.kristixlab.notion.api.model.common.NotionListType;
import io.kristixlab.notion.api.model.pages.properties.list.ListedPageProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        defaultImpl = UnknownProperty.class,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ButtonProperty.class, name = "button"),
        @JsonSubTypes.Type(value = CheckboxProperty.class, name = "checkbox"),
        @JsonSubTypes.Type(value = CreatedByProperty.class, name = "created_by"),
        @JsonSubTypes.Type(value = CreatedTimeProperty.class, name = "created_time"),
        @JsonSubTypes.Type(value = DateProperty.class, name = "date"),
        @JsonSubTypes.Type(value = EmailProperty.class, name = "email"),
        @JsonSubTypes.Type(value = FilesProperty.class, name = "files"),
        @JsonSubTypes.Type(value = FormulaProperty.class, name = "formula"),
        @JsonSubTypes.Type(value = LastEditedByProperty.class, name = "last_edited_by"),
        @JsonSubTypes.Type(value = LastEditedTimeProperty.class, name = "last_edited_time"),
        @JsonSubTypes.Type(value = PropertyItem.class, name = "property_item"),
        @JsonSubTypes.Type(value = MultiSelectProperty.class, name = "multi_select"),
        @JsonSubTypes.Type(value = NumberProperty.class, name = "number"),
        @JsonSubTypes.Type(value = PeopleProperty.class, name = "people"),
        @JsonSubTypes.Type(value = PhoneNumberProperty.class, name = "phone_number"),
        @JsonSubTypes.Type(value = PlaceProperty.class, name = "place"),
        @JsonSubTypes.Type(value = RelationProperty.class, name = "relation"),
        @JsonSubTypes.Type(value = RichTextProperty.class, name = "rich_text"),
        @JsonSubTypes.Type(value = RollupProperty.class, name = "rollup"),
        @JsonSubTypes.Type(value = SelectProperty.class, name = "select"),
        @JsonSubTypes.Type(value = StatusProperty.class, name = "status"),
        @JsonSubTypes.Type(value = TitleProperty.class, name = "title"),
        @JsonSubTypes.Type(value = UniqueIdProperty.class, name = "unique_id"),
        @JsonSubTypes.Type(value = UrlProperty.class, name = "url"),
        @JsonSubTypes.Type(value = VerificationProperty.class, name = "verification")
})
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class PageProperty extends NotionListType<ListedPageProperty> {

  @JsonProperty("id")
  private String id;

  public abstract String getType();

  @JsonIgnore
  public boolean isNumber() {
    return "number".equals(getType());
  }

  public NumberProperty asNumber() {
    return (NumberProperty) this;
  }

  @JsonIgnore
  public boolean isTitle() {
    return "title".equals(getType());
  }

  @JsonIgnore
  public TitleProperty asTitle() {
    return (TitleProperty) this;
  }

  @JsonIgnore
  public boolean isRichText() {
    return "rich_text".equals(getType());
  }

  public RichTextProperty asRichText() {
    return (RichTextProperty) this;
  }

  @JsonIgnore
  public boolean isSelect() {
    return "select".equals(getType());
  }

  public SelectProperty asSelect() {
    return (SelectProperty) this;
  }

  @JsonIgnore
  public boolean isMultiSelect() {
    return "multi_select".equals(getType());
  }

  public MultiSelectProperty asMultiSelect() {
    return (MultiSelectProperty) this;
  }

  @JsonIgnore
  public boolean isCheckbox() {
    return "checkbox".equals(getType());
  }

  public CheckboxProperty asCheckbox() {
    return (CheckboxProperty) this;
  }

  @JsonIgnore
  public boolean isCreatedTime() {
    return "created_time".equals(getType());
  }

  public CreatedTimeProperty asCreatedTime() {
    return (CreatedTimeProperty) this;
  }

  @JsonIgnore
  public boolean isLastEditedTime() {
    return "last_edited_time".equals(getType());
  }

  public LastEditedTimeProperty asLastEditedTime() {
    return (LastEditedTimeProperty) this;
  }

  @JsonIgnore
  public boolean isCreatedBy() {
    return "created_by".equals(getType());
  }

  public CreatedByProperty asCreatedBy() {
    return (CreatedByProperty) this;
  }

  @JsonIgnore
  public boolean isLastEditedBy() {
    return "last_edited_by".equals(getType());
  }

  public LastEditedByProperty asLastEditedBy() {
    return (LastEditedByProperty) this;
  }

  @JsonIgnore
  public boolean isDate() {
    return "date".equals(getType());
  }

  public DateProperty asDate() {
    return (DateProperty) this;
  }

  @JsonIgnore
  public boolean isEmail() {
    return "email".equals(getType());
  }

  public EmailProperty asEmail() {
    return (EmailProperty) this;
  }

  @JsonIgnore
  public boolean isPhoneNumber() {
    return "phone_number".equals(getType());
  }

  public PhoneNumberProperty asPhoneNumber() {
    return (PhoneNumberProperty) this;
  }

  @JsonIgnore
  public boolean isFiles() {
    return "files".equals(getType());
  }

  public FilesProperty asFiles() {
    return (FilesProperty) this;
  }

  @JsonIgnore
  public boolean isPeople() {
    return "people".equals(getType());
  }

  public PeopleProperty asPeople() {
    return (PeopleProperty) this;
  }

  @JsonIgnore
  public boolean isRelation() {
    return "relation".equals(getType());
  }

  public RelationProperty asRelation() {
    return (RelationProperty) this;
  }

  @JsonIgnore
  public boolean isRollup() {
    return "rollup".equals(getType());
  }

  public RollupProperty asRollup() {
    return (RollupProperty) this;
  }

  @JsonIgnore
  public boolean isFormula() {
    return "formula".equals(getType());
  }

  public FormulaProperty asFormula() {
    return (FormulaProperty) this;
  }

  @JsonIgnore
  public boolean isStatus() {
    return "status".equals(getType());
  }

  public StatusProperty asStatus() {
    return (StatusProperty) this;
  }

  @JsonIgnore
  public boolean isUrl() {
    return "url".equals(getType());
  }

  public UrlProperty asUrl() {
    return (UrlProperty) this;
  }

  @JsonIgnore
  public boolean isButton() {
    return "button".equals(getType());
  }

  public ButtonProperty asButton() {
    return (ButtonProperty) this;
  }

  @JsonIgnore
  public boolean isPlace() {
    return "place".equals(getType());
  }

  public PlaceProperty asPlace() {
    return (PlaceProperty) this;
  }

  @JsonIgnore
  public boolean isVerification() {
    return "verification".equals(getType());
  }

  public VerificationProperty asVerification() {
    return (VerificationProperty) this;
  }

  @JsonIgnore
  public boolean isUniqueId() {
    return "unique_id".equals(getType());
  }

  public UniqueIdProperty asUniqueId() {
    return (UniqueIdProperty) this;
  }

  @JsonIgnore
  public boolean isPropertyItem() {
    return "property_item".equals(getType());
  }

  public PropertyItem asPropertyItem() {
    return (PropertyItem) this;
  }
}
