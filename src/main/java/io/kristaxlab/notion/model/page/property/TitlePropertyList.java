package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/** Page property list of a {@code title} property. */
@JsonDeserialize(using = JsonDeserializer.None.class)
public final class TitlePropertyList extends PagePropertyList<TitlePropertyItem, ListedRichText> {}
