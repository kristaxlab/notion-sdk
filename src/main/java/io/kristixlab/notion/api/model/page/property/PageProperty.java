package io.kristixlab.notion.api.model.page.property;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.kristixlab.notion.api.model.common.NotionList;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    defaultImpl = UnknownProperty.class,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = CreatedByProperty.class, name = "created_by"), // read-only
  @JsonSubTypes.Type(value = CreatedTimeProperty.class, name = "created_time"), // read-only
  @JsonSubTypes.Type(value = LastEditedByProperty.class, name = "last_edited_by"), // read-only
  @JsonSubTypes.Type(value = LastEditedTimeProperty.class, name = "last_edited_time"), // read-only
  @JsonSubTypes.Type(value = TitleProperty.class, name = "title"),
  @JsonSubTypes.Type(value = UniqueIdProperty.class, name = "unique_id"), // read-only
  @JsonSubTypes.Type(value = VerificationProperty.class, name = "verification") // read-only
})
@Getter
@Setter
public abstract class PageProperty extends NotionList<PageProperty> {

  private String id;

  public abstract String getType();
}
