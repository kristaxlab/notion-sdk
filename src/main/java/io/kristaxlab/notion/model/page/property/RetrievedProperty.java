package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

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
