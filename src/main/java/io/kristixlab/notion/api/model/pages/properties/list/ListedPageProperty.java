package io.kristixlab.notion.api.model.pages.properties.list;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        defaultImpl = ListedUnknownProperty.class,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ListedPeopleProperty.class, name = "people"),
        @JsonSubTypes.Type(value = ListedRelationProperty.class, name = "relation"),
        @JsonSubTypes.Type(value = ListedRichTextProperty.class, name = "rich_text"),
        @JsonSubTypes.Type(value = ListedTitleProperty.class, name = "title")
})
@Data
public class ListedPageProperty {
  @JsonProperty("object")
  private String object;
  @JsonProperty("id")
  private String id;
  @JsonProperty("type")
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
