package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.kristaxlab.notion.model.common.NotionList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonDeserialize(using = PagePropertyListDeserializer.class)
public abstract sealed class PagePropertyList<I extends PropertyItem, L> extends NotionList<L>
    implements RetrievedProperty
    permits RelationPropertyList,
        RichTextPropertyList,
        TitlePropertyList,
        PeoplePropertyList,
        UnknownPropertyList {

  private I propertyItem;

  @Override
  public String getType() {
    return PropertyType.PROPERTY_ITEM.type();
  }

  public TitlePropertyList asTitleList() {
    return (TitlePropertyList) this;
  }

  public RichTextPropertyList asRichTextList() {
    return (RichTextPropertyList) this;
  }

  public RelationPropertyList asRelationList() {
    return (RelationPropertyList) this;
  }

  public PeoplePropertyList asPeopleList() {
    return (PeoplePropertyList) this;
  }
}
