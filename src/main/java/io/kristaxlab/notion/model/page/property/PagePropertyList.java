package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.kristaxlab.notion.model.common.NotionList;
import lombok.Getter;
import lombok.Setter;

/**
 * A page property list: the paginated response the property retrieve endpoint returns for a
 * paginated property ({@code relation}, {@code rich_text}, {@code title}, {@code people}, {@code
 * rollup}).
 *
 * @param <I> the property item metadata type
 * @param <L> the listed item type held by {@code results}
 * @see io.kristaxlab.notion.endpoints.PagesEndpoint#retrievePaginatedProperty
 */
@Getter
@Setter
@JsonDeserialize(using = PagePropertyListDeserializer.class)
public abstract sealed class PagePropertyList<I extends PropertyItem, L extends ListedItem>
    extends NotionList<L> implements PageProperty
    permits RelationPropertyList,
        RichTextPropertyList,
        TitlePropertyList,
        PeoplePropertyList,
        RollupPropertyList,
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

  public RollupPropertyList asRollupList() {
    return (RollupPropertyList) this;
  }
}
