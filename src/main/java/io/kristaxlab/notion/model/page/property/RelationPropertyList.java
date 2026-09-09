package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/** Page property list of a {@code relation} property. */
@JsonDeserialize(using = JsonDeserializer.None.class)
public final class RelationPropertyList
    extends PagePropertyList<RelationPropertyItem, ListedRelation> {}
