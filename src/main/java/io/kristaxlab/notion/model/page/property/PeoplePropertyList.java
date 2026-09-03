package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/** Retrieved property list of a {@code people} property. */
@JsonDeserialize(using = JsonDeserializer.None.class)
public final class PeoplePropertyList extends PagePropertyList<PeoplePropertyItem, ListedPeople> {}
