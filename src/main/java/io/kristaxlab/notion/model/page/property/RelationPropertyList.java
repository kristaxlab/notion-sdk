package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.kristaxlab.notion.model.common.BlockReference;

/**
 * Typed marker for paginated relation property retrieve.
 */
@JsonDeserialize(using = JsonDeserializer.None.class)
public final class RelationPropertyList extends PagePropertyList<RelationPropertyItem, BlockReference> {
}
