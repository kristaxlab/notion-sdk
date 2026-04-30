package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.kristaxlab.notion.model.common.NotionList;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    defaultImpl = UnknownProperty.class,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = ButtonProperty.class, name = "button"),
  @JsonSubTypes.Type(value = CheckboxProperty.class, name = "checkbox"),
  @JsonSubTypes.Type(value = CreatedByProperty.class, name = "created_by"), // read-only
  @JsonSubTypes.Type(value = CreatedTimeProperty.class, name = "created_time"), // read-only
  @JsonSubTypes.Type(value = DateProperty.class, name = "date"),
  @JsonSubTypes.Type(value = EmailProperty.class, name = "email"),
  @JsonSubTypes.Type(value = FilesProperty.class, name = "files"),
  @JsonSubTypes.Type(value = FormulaProperty.class, name = "formula"),
  @JsonSubTypes.Type(value = LastEditedByProperty.class, name = "last_edited_by"), // read-only
  @JsonSubTypes.Type(value = LastEditedTimeProperty.class, name = "last_edited_time"), // read-only
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
  @JsonSubTypes.Type(value = UniqueIdProperty.class, name = "unique_id"), // read-only
  @JsonSubTypes.Type(value = UrlProperty.class, name = "url"),
  @JsonSubTypes.Type(value = VerificationProperty.class, name = "verification") // read-only
})
@Getter
@Setter
public abstract class PageProperty extends NotionList<PageProperty> {

  private String id;

  public abstract String getType();
}
