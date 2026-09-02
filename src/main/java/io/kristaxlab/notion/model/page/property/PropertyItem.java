package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * Nested {@code property_item} metadata on list-shaped property retrieve responses.
 *
 * <p>Notion also includes an empty object keyed by the property type (e.g. {@code "rich_text": {}})
 * which varies by type and is ignored here.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    defaultImpl = UnknownPropertyItem.class,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = PeoplePropertyItem.class, name = "people"),
  @JsonSubTypes.Type(value = RelationPropertyItem.class, name = "relation"),
  @JsonSubTypes.Type(value = RichTextPropertyItem.class, name = "rich_text"),
  @JsonSubTypes.Type(value = TitlePropertyItem.class, name = "title")
})
@Getter
@Setter
public sealed class PropertyItem
    permits PeoplePropertyItem,
        RelationPropertyItem,
        RichTextPropertyItem,
        TitlePropertyItem,
        UnknownPropertyItem {

  private String id;

  /* Property kind, e.g. {@code rich_text}, {@code relation}, {@code title}. */
  private String type;

  private String nextUrl;

  public <P extends PropertyItem> P as(Class<P> type) {
    Objects.requireNonNull(type, "type must not be null");
    return type.cast(this);
  }
}
