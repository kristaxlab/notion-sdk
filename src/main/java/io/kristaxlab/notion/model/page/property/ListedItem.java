package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    defaultImpl = ListedUnknown.class,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = ListedNumber.class, name = "number"),
  @JsonSubTypes.Type(value = ListedPeople.class, name = "people"),
  @JsonSubTypes.Type(value = ListedRelation.class, name = "relation"),
  @JsonSubTypes.Type(value = ListedRichText.class, name = "rich_text"),
  @JsonSubTypes.Type(value = ListedRichText.class, name = "title")
})
@Getter
@Setter
public sealed class ListedItem
    permits ListedNumber, ListedPeople, ListedRelation, ListedRichText, ListedUnknown {

  private String object;

  private String type;

  private String id;

  public <L extends ListedItem> L as(Class<L> type) {
    return type.cast(this);
  }
}
