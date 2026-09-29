package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * What the property retrieve endpoint returns: either a page property value ({@link
 * PagePropertyValue}) for a non-paginated property, or a page property list ({@link
 * PagePropertyList}) for a paginated one.
 *
 * @see io.kristaxlab.notion.endpoints.PagesEndpoint#retrieveProperty
 */
@JsonDeserialize(using = PagePropertyDeserializer.class)
public sealed interface PageProperty permits PagePropertyValue, PagePropertyList {

  String getType();

  default <T extends PagePropertyValue> T asValue(Class<T> type) {
    return type.cast(this);
  }

  default <T extends PagePropertyList> T asList(Class<T> type) {
    return type.cast(this);
  }
}
