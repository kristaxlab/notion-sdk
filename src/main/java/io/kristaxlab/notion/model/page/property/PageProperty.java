package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.kristaxlab.notion.model.BaseNotionObject;
import lombok.Getter;
import lombok.Setter;

/**
 * An embedded property value, as held by the property map of a {@link
 * io.kristaxlab.notion.model.page.Page}.
 *
 * <p>Also models the retrieved property value that {@link
 * io.kristaxlab.notion.endpoints.PagesEndpoint#retrieveProperty} returns for a non-paginated
 * property. For a paginated property, that method returns a {@link PagePropertyList} instead.
 */
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
@JsonDeserialize(using = JsonDeserializer.None.class)
public abstract non-sealed class PageProperty extends BaseNotionObject
    implements RetrievedProperty {

  private String id;

  public abstract String getType();

  public <P extends PageProperty> P as(Class<P> type) {
    return type.cast(this);
  }
}
