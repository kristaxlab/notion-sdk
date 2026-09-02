package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/** Typed marker for paginated people property retrieve. */
@JsonDeserialize(using = JsonDeserializer.None.class)
public final class PeoplePropertyList extends PagePropertyList<PeoplePropertyItem, PropertyItem> {}
