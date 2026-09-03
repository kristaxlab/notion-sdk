package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/** Fallback retrieved property list for a paginated property type this SDK does not model. */
@JsonDeserialize(using = JsonDeserializer.None.class)
public final class UnknownPropertyList extends PagePropertyList<PropertyItem, ListedItem> {}
