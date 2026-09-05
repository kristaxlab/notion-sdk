package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Page property list of a {@code rollup} property. {@code results} are listed items whose type
 * depends on the rollup; the computed result lives on {@link #getPropertyItem()}.
 */
@JsonDeserialize(using = JsonDeserializer.None.class)
public final class RollupPropertyList extends PagePropertyList<RollupPropertyItem, ListedItem> {}
