package io.kristaxlab.notion.model.page.property.list;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    defaultImpl = ListedUnknownProperty.class,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = ListedPeopleProperty.class, name = "people"),
  @JsonSubTypes.Type(value = ListedRelationProperty.class, name = "relation"),
  @JsonSubTypes.Type(value = ListedRichTextProperty.class, name = "rich_text"),
  @JsonSubTypes.Type(value = ListedTitleProperty.class, name = "title")
})
@Getter
@Setter
public class ListedPageProperty {
  private String object;

  private String id;

  private String type;

  @JsonIgnore
  public boolean isRichText() {
    return "rich_text".equals(type);
  }

  @JsonIgnore
  public boolean isPeople() {
    return "people".equals(type);
  }

  @JsonIgnore
  public boolean isRelation() {
    return "relation".equals(type);
  }

  @JsonIgnore
  public boolean isTitle() {
    return "title".equals(type);
  }

  public ListedRichTextProperty asRichText() {
    return (ListedRichTextProperty) this;
  }

  public ListedPeopleProperty asPeople() {
    return (ListedPeopleProperty) this;
  }

  public ListedRelationProperty asRelation() {
    return (ListedRelationProperty) this;
  }

  public ListedTitleProperty asTitle() {
    return (ListedTitleProperty) this;
  }
}
