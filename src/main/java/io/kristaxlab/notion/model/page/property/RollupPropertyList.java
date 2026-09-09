package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * A page property list for a {@code rollup} property.
 *
 * <p>The {@code results} array contains listed items used by Notion to compute the rollup result.
 * The computed value is carried by the property item metadata ({@link RollupPropertyItem} — see
 * {@code getRollup()}).
 *
 * @see RollupPropertyItem
 */
@JsonDeserialize(using = JsonDeserializer.None.class)
public final class RollupPropertyList extends PagePropertyList<RollupPropertyItem, ListedItem> {}
