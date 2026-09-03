package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * What the property retrieve endpoint returns: either a retrieved property value ({@link
 * PageProperty}) for a non-paginated property, or a retrieved property list ({@link
 * PagePropertyList}) for a paginated one.
 *
 * @see io.kristaxlab.notion.endpoints.PagesEndpoint#retrieveProperty
 */
@JsonDeserialize(using = RetrievedPropertyDeserializer.class)
public sealed interface RetrievedProperty permits PageProperty, PagePropertyList {

  String getType();

  default <T extends PageProperty> T asValue(Class<T> type) {
    return type.cast(this);
  }

  default <T extends PagePropertyList> T asList(Class<T> type) {
    return type.cast(this);
  }
}
